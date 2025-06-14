package ua.nure.kryvko.roman.apz.sensorState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sensor-states")
public class SensorStateController {
    private static SensorStateResponse toDto(SensorState sensorState) {
        return new SensorStateResponse(
                sensorState.getId(),
                sensorState.getSensor().getId(),
                sensorState.getTimestamp(),
                sensorState.getValue(),
                sensorState.getUnit()
        );
    }

    private final SensorStateService sensorStateService;

    @Autowired
    public SensorStateController(SensorStateService sensorStateService) {
        this.sensorStateService = sensorStateService;
    }

    @PostMapping
    public ResponseEntity<SensorStateResponse> createSensorState(@RequestBody SensorState sensorState) {
        try {
            SensorState savedSensorState = sensorStateService.saveSensorState(sensorState);
            return new ResponseEntity<>(toDto(savedSensorState), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("sensor/{id}")
    @PreAuthorize("@authorizationService.canAccessSensor(#id, authentication)")
    public ResponseEntity<List<SensorStateResponse>> getSensorStatesBySensorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(sensorStateService.getSensorStateBySensorId(id).stream()
                    .map(SensorStateController::toDto)
                    .collect(Collectors.toList()));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("greenhouse/{id}")
    @PreAuthorize("@authorizationService.canAccessGreenhouse(#id, authentication)")
    public ResponseEntity<List<SensorStateResponse>> getSensorStatesByGreenhouseId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(sensorStateService.getSensorStateByGreenhouseId(id).stream()
                    .map(SensorStateController::toDto)
                    .collect(Collectors.toList()));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessSensorState(#id, authentication)")
    public ResponseEntity<SensorStateResponse> getSensorStateById(@PathVariable Integer id) {
        Optional<SensorState> sensorState = sensorStateService.getSensorStateById(id);
        if (sensorState.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(toDto(sensorState.get()));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessSensorState(#id, authentication)")
    public ResponseEntity<SensorStateResponse> updateSensorState(@PathVariable Integer id, @RequestBody SensorState sensorState) {
        try {
            sensorState.setId(id);
            SensorState updatedSensorState = sensorStateService.updateSensorState(sensorState);
            return ResponseEntity.ok(toDto(updatedSensorState));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessSensorState(#id, authentication)")
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
