package ua.nure.kryvko.roman.apz.automationRule;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ua.nure.kryvko.roman.apz.automationAction.AutomationAction;
import ua.nure.kryvko.roman.apz.automationAction.AutomationActionService;
import ua.nure.kryvko.roman.apz.automationRuleDetails.AutomationRuleDetails;
import ua.nure.kryvko.roman.apz.greenhouse.Greenhouse;
import ua.nure.kryvko.roman.apz.greenhouse.GreenhouseRepository;
import ua.nure.kryvko.roman.apz.sensorState.SensorState;

import java.util.List;
import java.util.Optional;

@Service
public class AutomationRuleService {

    private final AutomationRuleRepository automationRuleRepository;
    private final GreenhouseRepository greenhouseRepository;
    private final AutomationActionService automationActionService;

    @Autowired
    public AutomationRuleService(AutomationRuleRepository automationRuleRepository,
                                 GreenhouseRepository greenhouseRepository, AutomationActionService automationActionService) {
        this.automationRuleRepository = automationRuleRepository;
        this.greenhouseRepository = greenhouseRepository;
        this.automationActionService = automationActionService;
    }

    @Transactional
    public AutomationRule createAutomationRule(AutomationRule automationRule) {
        Greenhouse owner = greenhouseRepository.findById(automationRule.getGreenhouse().getId())
                .orElseThrow(() -> new IllegalArgumentException("Greenhouse does not exist."));
        automationRule.setGreenhouse(owner);
        return automationRuleRepository.save(automationRule);
    }

    public List<AutomationRule> getAllAutomationRules() {
        return automationRuleRepository.findAll();
    }

    public Optional<AutomationRule> getAutomationRuleById(Integer id) {
        return automationRuleRepository.findById(id);
    }

    @Transactional
    public AutomationRule updateAutomationRule(Integer id, AutomationRule updatedAutomationRule) {
        updatedAutomationRule.setId(id);
        if (!automationRuleRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Automation Rule does not exist.");
        }

        Greenhouse owner = greenhouseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Greenhouse does not exist."));
        updatedAutomationRule.setGreenhouse(owner);

        return automationRuleRepository.save(updatedAutomationRule);
    }

    @Transactional
    public void deleteAutomationRule(Integer id) {
        if (!automationRuleRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Automation Rule does not exist.");
        }
        automationRuleRepository.deleteById(id);
    }

    /**
     * Checks sensor state against all the AutomationRules present in certain greenhouse,
     * passes the rule to AutomationActionService to invoke action, if the rule is applicable.
     * @param greenhouse
     * @param sensorState
     */
    public void checkSensorStateData(Greenhouse greenhouse, SensorState sensorState) {
        List<AutomationRule> rules = automationRuleRepository.findByGreenhouse(greenhouse);
        for (AutomationRule rule : rules) {
            // apply only to sensor-dependent rules
            if (rule.automationRuleType == AutomationRuleType.SENSOR) {
                AutomationRuleDetails details = rule.getAutomationRuleDetails();
                try {
                    if (details.getMaxValue() < sensorState.getValue() || details.getMinValue() > sensorState.getValue()) {
                        automationActionService.invokeActionWithRule(rule, details);
                    }
                } catch (NullPointerException e) {
                    // do nothing
                }
            }
        }
    }
}
