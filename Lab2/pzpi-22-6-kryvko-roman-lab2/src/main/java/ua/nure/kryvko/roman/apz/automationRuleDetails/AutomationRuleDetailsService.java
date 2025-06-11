package ua.nure.kryvko.roman.apz.automationRuleDetails;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ua.nure.kryvko.roman.apz.automationRule.AutomationRule;
import ua.nure.kryvko.roman.apz.automationRule.AutomationRuleRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AutomationRuleDetailsService {

    private final AutomationRuleDetailsRepository automationRuleDetailsRepository;
    private final AutomationRuleRepository automationRuleRepository;

    @Autowired
    public AutomationRuleDetailsService(AutomationRuleDetailsRepository automationRuleDetailsRepository,
                                        AutomationRuleRepository automationRuleRepository) {
        this.automationRuleDetailsRepository = automationRuleDetailsRepository;
        this.automationRuleRepository = automationRuleRepository;
    }

    @Transactional
    public AutomationRuleDetails createAutomationRuleDetails(AutomationRuleDetails automationRuleDetails) {
        AutomationRule owner = automationRuleRepository.findById(automationRuleDetails.getAutomationRule().getId())
                .orElseThrow(() -> new IllegalArgumentException("AutomationRule does not exist."));
        automationRuleDetails.setAutomationRule(owner);
        return automationRuleDetailsRepository.save(automationRuleDetails);
    }

    public List<AutomationRuleDetails> getAllAutomationRuleDetails() {
        return automationRuleDetailsRepository.findAll();
    }

    public AutomationRuleDetails getAutomationRuleDetailsById(Integer id) {
        return automationRuleDetailsRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("AutomationRuleDetails not found."));
    }

    @Transactional
    public AutomationRuleDetails updateAutomationRuleDetails(Integer id, AutomationRuleDetails automationRuleDetails) {
        automationRuleDetails.setId(id);
        if (!automationRuleDetailsRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "AutomationRuleDetail not found for update");
        }
        AutomationRule owner = automationRuleRepository.findById(automationRuleDetails.getAutomationRule().getId())
                .orElseThrow(() -> new IllegalArgumentException("AutomationRule does not exist."));
        automationRuleDetails.setAutomationRule(owner);
        return automationRuleDetailsRepository.save(automationRuleDetails);
    }

    @Transactional
    public void deleteAutomationRuleDetails(Integer id) {
        if (!automationRuleDetailsRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "AutomationRuleDetail not found for deletion");
        }
        automationRuleDetailsRepository.deleteById(id);
    }
}
