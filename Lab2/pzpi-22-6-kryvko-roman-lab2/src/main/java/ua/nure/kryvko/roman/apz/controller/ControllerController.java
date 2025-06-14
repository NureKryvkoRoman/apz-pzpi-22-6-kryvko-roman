package ua.nure.kryvko.roman.apz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static ua.nure.kryvko.roman.apz.controller.ControllerResponseMapper.toDto;

@RestController
@RequestMapping("/api/controllers")
public class ControllerController {

    private final ControllerService controllerService;

    @Autowired
    public ControllerController(ControllerService controllerService) {
        this.controllerService = controllerService;
    }

    @PostMapping
    public ResponseEntity<ControllerResponse> createController(@RequestBody Controller controller) {
        try {
            Controller savedController = controllerService.createController(controller);
            return new ResponseEntity<>(toDto(savedController), HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessController(#id, authentication)")
    public ResponseEntity<ControllerResponse> getControllerById(@PathVariable Integer id) {
        return controllerService.getControllerById(id)
                .map(controller -> new ResponseEntity<>(toDto(controller), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ControllerResponse> getAllControllers() {
        return controllerService.getAllControllers().stream().map(ControllerResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessController(#id, authentication)")
    public ResponseEntity<ControllerResponse> updateController(@PathVariable Integer id, @RequestBody Controller controller) {
        try {
            Controller newController = controllerService.updateController(id, controller);
            return ResponseEntity.ok(toDto(newController));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessController(#id, authentication)")
    public ResponseEntity<Void> deleteController(@PathVariable Integer id) {
        try {
            controllerService.deleteController(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}