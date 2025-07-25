package ua.nure.kryvko.roman.apz.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ua.nure.kryvko.roman.apz.sensor.SensorResponseMapper.toDto;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {

    private final SensorService sensorService;

    @Autowired
    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @PostMapping
    public ResponseEntity<SensorResponse> createSensor(@RequestBody Sensor sensor) {
        try {
            Sensor savedSensor = sensorService.saveSensor(sensor);
            return ResponseEntity.ok(toDto(savedSensor));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessSensor(#id, authentication)")
    public ResponseEntity<SensorResponse> getSensorById(@PathVariable Integer id) {
        Optional<Sensor> sensor = sensorService.getSensorById(id);
        if (sensor.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(toDto(sensor.get()));
    }

    @GetMapping("greenhouse/{greenhouseId}")
    @PreAuthorize("@authorizationService.canAccessGreenhouse(#greenhouseId, authentication)")
    public ResponseEntity<List<SensorResponse>> getSensorsByGreenhouse(@PathVariable Integer greenhouseId) {
        List<Sensor> sensors = sensorService.getSensorByGreenhouse(greenhouseId);
        return ResponseEntity.ok(sensors.stream().map(SensorResponseMapper::toDto).collect(Collectors.toList()));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessSensor(#id, authentication)")
    public ResponseEntity<SensorResponse> updateSensor(@PathVariable Integer id, @RequestBody Sensor sensor) {
        try {
            sensor.setId(id);
            Sensor updatedSensor = sensorService.updateSensor(sensor);
            return ResponseEntity.ok(toDto(updatedSensor));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessSensor(#id, authentication)")
    public ResponseEntity<Void> deleteSensor(@PathVariable Integer id) {
        try {
            sensorService.deleteSensorById(id);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
