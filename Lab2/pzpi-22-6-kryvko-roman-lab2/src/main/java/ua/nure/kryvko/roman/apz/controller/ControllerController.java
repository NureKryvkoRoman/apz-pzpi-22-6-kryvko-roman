package ua.nure.kryvko.roman.apz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/controllers")
public class ControllerController {

    private final ControllerService controllerService;

    @Autowired
    public ControllerController(ControllerService controllerService) {
        this.controllerService = controllerService;
    }

    @PostMapping
    public ResponseEntity<Controller> createController(@RequestBody Controller controller) {
        try {
            Controller savedController = controllerService.createController(controller);
            return new ResponseEntity<>(savedController, HttpStatus.CREATED);
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
    public ResponseEntity<Controller> getControllerById(@PathVariable Integer id) {
        return controllerService.getControllerById(id)
                .map(controller -> new ResponseEntity<>(controller, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Controller> getAllControllers() {
        return controllerService.getAllControllers();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessController(#id, authentication)")
    public ResponseEntity<Controller> updateController(@PathVariable Integer id, @RequestBody Controller controller) {
        try {
            Controller newController = controllerService.updateController(id, controller);
            return ResponseEntity.ok(newController);
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