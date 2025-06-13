package ua.nure.kryvko.roman.apz.automationAction;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/automation-actions")
public class AutomationActionController {

    private final AutomationActionService automationActionService;

    public AutomationActionController(AutomationActionService automationActionService) {
        this.automationActionService = automationActionService;
    }

    @PostMapping
    public ResponseEntity<AutomationAction> createAutomationAction(@RequestBody AutomationAction action) {
        try {
            AutomationAction createdAction = automationActionService.createAutomationAction(action);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAction);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessAutomationAction(#id, authentication)")
    public ResponseEntity<AutomationAction> getAutomationActionById(@PathVariable Integer id) {
        return automationActionService.getAutomationActionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessAutomationAction(#id, authentication)")
    public ResponseEntity<AutomationAction> updateAutomationAction(@PathVariable Integer id,
                                                       @RequestBody AutomationAction automationAction) {
        try {
            AutomationAction updatedAction = automationActionService.updateAutomationAction(id, automationAction);
            return new ResponseEntity<>(updatedAction, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessAutomationAction(#id, authentication)")
    public ResponseEntity<Void> deleteAutomationAction(@PathVariable Integer id) {
        try {
            automationActionService.deleteAutomationAction(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AutomationAction>> getAllAutomationActions() {
        return ResponseEntity.ok(automationActionService.getAllAutomationActions());
    }
}