package ua.nure.kryvko.roman.apz.automationRule;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ua.nure.kryvko.roman.apz.automationRule.AutomationRuleResponseMapper.toDto;

@RestController
@RequestMapping("/api/automation-rules")
public class AutomationRuleController {

    private final AutomationRuleService automationRuleService;

    @Autowired
    public AutomationRuleController(AutomationRuleService automationRuleService) {
        this.automationRuleService = automationRuleService;
    }

    @PostMapping
    public ResponseEntity<AutomationRuleResponse> createAutomationRule(@RequestBody AutomationRule automationRule) {
        try {
            AutomationRule createdRule = automationRuleService.createAutomationRule(automationRule);
            return ResponseEntity.status(HttpStatus.CREATED).body(toDto(createdRule));
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
    public ResponseEntity<List<AutomationRuleResponse>> getAllAutomationRules() {
        return ResponseEntity.ok(automationRuleService.getAllAutomationRules().stream()
                .map(AutomationRuleResponseMapper::toDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/greenhouse/{id}")
    @PreAuthorize("@authorizationService.canAccessGreenhouse(#id, authentication)")
    public ResponseEntity<List<AutomationRuleResponse>> getAutomationRuleByGreenhouseId(@PathVariable Integer id) {
        List<AutomationRule> automationRules = automationRuleService.getAutomationRulesByGreenhouseId(id);
        return ResponseEntity.ok(automationRules.stream()
                .map(AutomationRuleResponseMapper::toDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessAutomationRule(#id, authentication)")
    public ResponseEntity<AutomationRuleResponse> getAutomationRuleById(@PathVariable Integer id) {
        Optional<AutomationRule> automationRule = automationRuleService.getAutomationRuleById(id);
        if (automationRule.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(toDto(automationRule.get()));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessAutomationRule(#id, authentication)")
    public ResponseEntity<AutomationRuleResponse> updateAutomationRule(@PathVariable Integer id, @RequestBody AutomationRule automationRule) {
        try {
            AutomationRule updatedRule = automationRuleService.updateAutomationRule(id, automationRule);
            return ResponseEntity.ok(toDto(updatedRule));
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
