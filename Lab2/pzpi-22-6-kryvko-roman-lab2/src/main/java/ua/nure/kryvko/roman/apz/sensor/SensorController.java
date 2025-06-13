package ua.nure.kryvko.roman.apz.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.nure.kryvko.roman.apz.registration.CustomUserDetails;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {

    private final SensorService sensorService;

    @Autowired
    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @PostMapping
    public ResponseEntity<Sensor> createSensor(@RequestBody Sensor sensor) {
        try {
            Sensor savedSensor = sensorService.saveSensor(sensor);
            return ResponseEntity.ok(savedSensor);
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
    public ResponseEntity<Sensor> getSensorById(@PathVariable Integer id) {
        Optional<Sensor> sensor = sensorService.getSensorById(id);
        return sensor.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("greenhouse/{greenhouseId}")
    @PreAuthorize("@authorizationService.canAccessGreenhouse(#greenhouseId, authentication)")
    public ResponseEntity<List<Sensor>> getSensorsByGreenhouse(@PathVariable Integer greenhouseId) {
        List<Sensor> sensors = sensorService.getSensorByGreenhouse(greenhouseId);
        return ResponseEntity.ok(sensors);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessSensor(#id, authentication)")
    public ResponseEntity<Sensor> updateSensor(@PathVariable Integer id, @RequestBody Sensor sensor) {
        try {
            sensor.setId(id);
            Sensor updatedSensor = sensorService.updateSensor(sensor);
            return ResponseEntity.ok(updatedSensor);
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
