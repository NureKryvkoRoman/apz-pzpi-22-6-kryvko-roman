package ua.nure.kryvko.roman.apz.automationAction;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ua.nure.kryvko.roman.apz.automationRule.AutomationRule;
import ua.nure.kryvko.roman.apz.automationRule.AutomationRuleRepository;
import ua.nure.kryvko.roman.apz.automationRuleDetails.AutomationRuleDetails;
import ua.nure.kryvko.roman.apz.controller.Controller;
import ua.nure.kryvko.roman.apz.controller.ControllerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AutomationActionService {

    private final AutomationActionRepository automationActionRepository;
    private final AutomationRuleRepository automationRuleRepository;
    private final ControllerRepository controllerRepository;

    public AutomationActionService(AutomationActionRepository automationActionRepository,
                                   AutomationRuleRepository automationRuleRepository,
                                   ControllerRepository controllerRepository) {
        this.automationActionRepository = automationActionRepository;
        this.automationRuleRepository = automationRuleRepository;
        this.controllerRepository = controllerRepository;
    }

    @Transactional
    public AutomationAction createAutomationAction(AutomationAction action) {
        AutomationRule ownerRule = automationRuleRepository.findById(action.getAutomationRule().getId())
                .orElseThrow(() -> new IllegalArgumentException("Automation rule does not exist."));

        Controller ownerController = controllerRepository.findById(action.getController().getId())
                .orElseThrow(() -> new IllegalArgumentException("Controller does not exist."));

        action.setAutomationRule(ownerRule);
        action.setController(ownerController);
        return automationActionRepository.save(action);
    }

    public Optional<AutomationAction> getAutomationActionById(Integer id) {
        return automationActionRepository.findById(id);
    }

    @Transactional
    public AutomationAction updateAutomationAction(Integer id, AutomationAction updatedAction) {
        updatedAction.setId(id);
        if (!automationActionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Automation action does not exist.");
        }
        AutomationRule ownerRule = automationRuleRepository.findById(updatedAction.getAutomationRule().getId())
                .orElseThrow(() -> new IllegalArgumentException("Automation rule does not exist."));

        Controller ownerController = controllerRepository.findById(updatedAction.getController().getId())
                .orElseThrow(() -> new IllegalArgumentException("Controller does not exist."));

        updatedAction.setAutomationRule(ownerRule);
        updatedAction.setController(ownerController);
        automationActionRepository.save(updatedAction);

        return updatedAction;
    }

    @Transactional
    public void deleteAutomationAction(Integer id) {
        if (!automationActionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Automation action does not exist.");
        }
        automationActionRepository.deleteById(id);
    }

    public List<AutomationAction> getAllAutomationActions() {
        return automationActionRepository.findAll();
    }

    /**
     * Processes an AutomationRule and its AutomationRuleDetails,
     * extracts actions that depends on this rule and runs corresponding controllers.
     * @param rule AutomationRule to process
     * @param details AutomationRuleDetails to process
     */
    public void invokeActionWithRule(AutomationRule rule, AutomationRuleDetails details) {
        List<AutomationAction> automationActionList = rule.getAutomationActions();
        for (AutomationAction action : automationActionList) {
            Controller controller = action.getController();
            if (controller.isActive())
                controller.run(details.getInterval());
        }
    }
}
