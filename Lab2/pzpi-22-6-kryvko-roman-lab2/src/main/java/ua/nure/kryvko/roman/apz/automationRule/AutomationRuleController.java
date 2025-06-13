package ua.nure.kryvko.roman.apz.automationRule;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/automation-rules")
public class AutomationRuleController {

    private final AutomationRuleService automationRuleService;

    @Autowired
    public AutomationRuleController(AutomationRuleService automationRuleService) {
        this.automationRuleService = automationRuleService;
    }

    @PostMapping
    public ResponseEntity<AutomationRule> createAutomationRule(@RequestBody AutomationRule automationRule) {
        try {
            AutomationRule createdRule = automationRuleService.createAutomationRule(automationRule);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRule);
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
    public ResponseEntity<List<AutomationRule>> getAllAutomationRules() {
        return ResponseEntity.ok(automationRuleService.getAllAutomationRules());
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessAutomationRule(#id, authentication)")
    public ResponseEntity<AutomationRule> getAutomationRuleById(@PathVariable Integer id) {
        return automationRuleService.getAutomationRuleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessAutomationRule(#id, authentication)")
    public ResponseEntity<AutomationRule> updateAutomationRule(@PathVariable Integer id, @RequestBody AutomationRule automationRule) {
        try {
            AutomationRule updatedRule = automationRuleService.updateAutomationRule(id, automationRule);
            return ResponseEntity.ok(updatedRule);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessAutomationRule(#id, authentication)")
    public ResponseEntity<Void> deleteAutomationRule(@PathVariable Integer id) {
        try {
            automationRuleService.deleteAutomationRule(id);
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
