package ua.nure.kryvko.roman.apz.greenhouse;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ua.nure.kryvko.roman.apz.sensor.Sensor;
import ua.nure.kryvko.roman.apz.sensor.SensorRepository;
import ua.nure.kryvko.roman.apz.sensor.SensorType;
import ua.nure.kryvko.roman.apz.sensorState.SensorState;
import ua.nure.kryvko.roman.apz.sensorState.SensorStateRepository;
import ua.nure.kryvko.roman.apz.user.User;
import ua.nure.kryvko.roman.apz.user.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class GreenhouseService {

    private final GreenhouseRepository greenhouseRepository;
    private final UserRepository userRepository;
    private final SensorStateRepository sensorStateRepository;
    private final SensorRepository sensorRepository;

    @Autowired
    public GreenhouseService(GreenhouseRepository greenhouseRepository,
                             UserRepository userRepository,
                             SensorStateRepository sensorStateRepository,
                             SensorRepository sensorRepository) {
        this.greenhouseRepository = greenhouseRepository;
        this.userRepository = userRepository;
        this.sensorStateRepository = sensorStateRepository;
        this.sensorRepository = sensorRepository;
    }

    public double calculateGDD(int greenhouseId, double baseTemperature, LocalDate from, LocalDate to) {
        List<Sensor> tempSensors = sensorRepository.findByGreenhouseIdAndSensorType(greenhouseId, SensorType.TEMPERATURE);
        if (tempSensors.isEmpty()) return 0;

        List<Float> dailyGDDs = new ArrayList<>();

        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.plusDays(1).atStartOfDay();

            List<Float> dayTemps = new ArrayList<>();
            for (Sensor sensor : tempSensors) {
                List<SensorState> states = sensorStateRepository.findBySensorIdAndTimestampBetween(sensor.getId(), start, end);
                for (SensorState s : states) {
                    dayTemps.add(s.getValue());
                }
            }

            if (!dayTemps.isEmpty()) {
                double tMin = Collections.min(dayTemps);
                double tMax = Collections.max(dayTemps);
                double gdd = ((tMax + tMin) / 2) - baseTemperature;
                dailyGDDs.add(Math.max(0, (float) gdd)); // GDD can't be negative
            }
        }

        return dailyGDDs.stream().mapToDouble(Float::doubleValue).sum();
    }

    public double calculateDewPoint(int greenhouseId, LocalDate date) {
        List<Sensor> tempSensors = sensorRepository.findByGreenhouseIdAndSensorType(greenhouseId, SensorType.TEMPERATURE);
        List<Sensor> humiditySensors = sensorRepository.findByGreenhouseIdAndSensorType(greenhouseId, SensorType.HUMIDITY);

        if (tempSensors.isEmpty() || humiditySensors.isEmpty()) return 0;

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        List<Float> tempValues = new ArrayList<>();
        List<Float> humidityValues = new ArrayList<>();

        for (Sensor sensor : tempSensors) {
            List<SensorState> states = sensorStateRepository.findBySensorIdAndTimestampBetween(sensor.getId(), start, end);
            for (SensorState s : states) tempValues.add(s.getValue());
        }

        for (Sensor sensor : humiditySensors) {
            List<SensorState> states = sensorStateRepository.findBySensorIdAndTimestampBetween(sensor.getId(), start, end);
            for (SensorState s : states) humidityValues.add(s.getValue());
        }

        if (tempValues.isEmpty() || humidityValues.isEmpty()) return 0;

        double avgTemp = tempValues.stream().mapToDouble(Float::doubleValue).average().orElse(0);
        double avgRH = humidityValues.stream().mapToDouble(Float::doubleValue).average().orElse(0);

        // Approximate dew point formula
        return avgTemp - ((100 - avgRH) / 5);
    }

    @Transactional
    public Greenhouse saveGreenhouse(Greenhouse greenhouse) {
        User owner = userRepository.findById(greenhouse.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Ensure the user is a managed entity
        greenhouse.setUser(owner);
        return greenhouseRepository.save(greenhouse);
    }

    public Optional<Greenhouse> getGreenhouseById(Integer id) {
        return greenhouseRepository.findById(id);
    }

    public Optional<GreenhouseSummary> getGreenhouseSummaryById(Integer id) {
        return greenhouseRepository.findSummaryById(id);
    }

    @Transactional
    public Greenhouse updateGreenhouse(Integer id, Greenhouse greenhouse) {
        greenhouse.setId(id);
        if (greenhouse.getId() == null || !greenhouseRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Greenhouse ID not found for update.");
        }

        User owner = userRepository.findById(greenhouse.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        greenhouse.setUser(owner);
        return greenhouseRepository.save(greenhouse);
    }

    @Transactional
    public void deleteGreenhouseById(Integer id) {
        if (!greenhouseRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Greenhouse ID not found for deletion.");
        }
        greenhouseRepository.deleteById(id);
    }

    public List<GreenhouseSummary> getGreenhousesSummaryByUserId(Integer userId) {
        return greenhouseRepository.findSummaryByUserId(userId);
    }

    public List<Greenhouse> getGreenhousesByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                        .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return greenhouseRepository.findByUser(user);
    }

    public List<Greenhouse> getGreenhousesByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return greenhouseRepository.findByUser(user);
    }
}
