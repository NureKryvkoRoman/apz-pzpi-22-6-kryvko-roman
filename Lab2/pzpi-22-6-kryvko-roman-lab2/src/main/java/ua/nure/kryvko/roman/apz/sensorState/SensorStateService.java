package ua.nure.kryvko.roman.apz.sensorState;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ua.nure.kryvko.roman.apz.automationRule.AutomationRuleService;
import ua.nure.kryvko.roman.apz.greenhouse.Greenhouse;
import ua.nure.kryvko.roman.apz.greenhouse.GreenhouseRepository;
import ua.nure.kryvko.roman.apz.sensor.Sensor;
import ua.nure.kryvko.roman.apz.sensor.SensorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SensorStateService {

    private final SensorStateRepository sensorStateRepository;
    private final SensorRepository sensorRepository;
    private final GreenhouseRepository greenhouseRepository;
    private final AutomationRuleService automationRuleService;

    @Autowired
    public SensorStateService(SensorStateRepository sensorStateRepository, SensorRepository sensorRepository,
                              GreenhouseRepository greenhouseRepository, AutomationRuleService automationRuleService) {
        this.sensorStateRepository = sensorStateRepository;
        this.sensorRepository = sensorRepository;
        this.greenhouseRepository = greenhouseRepository;
        this.automationRuleService = automationRuleService;
    }

    @Transactional
    public SensorState saveSensorState(SensorState sensorState) {
        Sensor owner = sensorRepository.findById(sensorState.getSensor().getId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor not found"));
        sensorState.setSensor(owner);

        Greenhouse greenhouse = owner.getGreenhouse();
        automationRuleService.checkSensorStateData(greenhouse, sensorState);
        return sensorStateRepository.save(sensorState);
    }

    public Optional<SensorState> getSensorStateById(Integer id) {
        return sensorStateRepository.findById(id);
    }

    public List<SensorState> getSensorStateByGreenhouseId(Integer id) {
        Greenhouse greenhouse = greenhouseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Greenhouse not found."));
        List<Sensor> sensors = greenhouse.getSensors();
        List<SensorState> sensorStates = new ArrayList<>();
        for (Sensor sensor : sensors) {
            sensorStates.addAll(sensorStateRepository.findBySensor(sensor));
        }
        return sensorStates;
    }

    public List<SensorState> getSensorStateBySensorId(Integer id) {
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor not found."));
        return sensorStateRepository.findBySensor(sensor);
    }

    @Transactional
    public SensorState updateSensorState(SensorState sensorState) {
        if (sensorState.getId() == null || !sensorStateRepository.existsById(sensorState.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "SensorState ID not found for update.");
        }
        return sensorStateRepository.save(sensorState);
    }

    @Transactional
    public void deleteSensorStateById(Integer id) {
        if (!sensorStateRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "SensorState ID not found for update.");
        }

        sensorStateRepository.deleteById(id);
    }
}
