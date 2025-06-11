package ua.nure.kryvko.roman.apz.sensor;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ua.nure.kryvko.roman.apz.greenhouse.Greenhouse;
import ua.nure.kryvko.roman.apz.greenhouse.GreenhouseRepository;

import java.util.Optional;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;
    private final GreenhouseRepository greenhouseRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository, GreenhouseRepository greenhouseRepository) {
        this.sensorRepository = sensorRepository;
        this.greenhouseRepository = greenhouseRepository;
    }

    @Transactional
    public Sensor saveSensor(Sensor sensor) {
        Greenhouse owner = greenhouseRepository.findById(sensor.getGreenhouse().getId())
                .orElseThrow(() -> new IllegalArgumentException("Greenhouse for sensor not found"));
        sensor.setGreenhouse(owner);
        return sensorRepository.save(sensor);
    }

    public Optional<Sensor> getSensorById(Integer id) {
        return sensorRepository.findById(id);
    }

    @Transactional
    public Sensor updateSensor(Sensor sensor) {
        if (sensor.getId() == null || !sensorRepository.existsById(sensor.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor ID not found for update.");
        }
        return sensorRepository.save(sensor);
    }

    @Transactional
    public void deleteSensorById(Integer id) {
        if (!sensorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor ID not found for update.");
        }
        sensorRepository.deleteById(id);
    }
}
