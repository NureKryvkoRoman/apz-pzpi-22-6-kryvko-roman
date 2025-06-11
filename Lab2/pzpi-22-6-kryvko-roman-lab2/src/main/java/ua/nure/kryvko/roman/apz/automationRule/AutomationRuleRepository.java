package ua.nure.kryvko.roman.apz.automationRule;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nure.kryvko.roman.apz.greenhouse.Greenhouse;

import java.util.List;

public interface AutomationRuleRepository extends JpaRepository<AutomationRule, Integer> {
    List<AutomationRule> findByGreenhouse(Greenhouse greenhouse);
}
