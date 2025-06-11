package ua.nure.kryvko.roman.apz.sensorState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sensor-states")
public class SensorStateController {

    private final SensorStateService sensorStateService;

    @Autowired
    public SensorStateController(SensorStateService sensorStateService) {
        this.sensorStateService = sensorStateService;
    }

    @PostMapping
    public ResponseEntity<SensorState> createSensorState(@RequestBody SensorState sensorState) {
        try {
            SensorState savedSensorState = sensorStateService.saveSensorState(sensorState);
            return new ResponseEntity<>(savedSensorState, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("sensor/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<List<SensorState>> getSensorStatesBySensorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(sensorStateService.getSensorStateBySensorId(id));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("greenhouse/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<List<SensorState>> getSensorStatesByGreenhouseId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(sensorStateService.getSensorStateByGreenhouseId(id));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<SensorState> getSensorStateById(@PathVariable Integer id) {
        Optional<SensorState> sensorState = sensorStateService.getSensorStateById(id);
        return sensorState.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<SensorState> updateSensorState(@PathVariable Integer id, @RequestBody SensorState sensorState) {
        try {
            sensorState.setId(id);
            SensorState updatedSensorState = sensorStateService.updateSensorState(sensorState);
            return ResponseEntity.ok(updatedSensorState);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<Void> deleteSensorState(@PathVariable Integer id) {
        try {
            sensorStateService.deleteSensorStateById(id);
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
