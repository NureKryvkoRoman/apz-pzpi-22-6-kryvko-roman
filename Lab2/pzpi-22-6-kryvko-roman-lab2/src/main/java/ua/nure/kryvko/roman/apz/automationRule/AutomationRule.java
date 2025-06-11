package ua.nure.kryvko.roman.apz.automationRule;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import ua.nure.kryvko.roman.apz.automationAction.AutomationAction;
import ua.nure.kryvko.roman.apz.automationRuleDetails.AutomationRuleDetails;
import ua.nure.kryvko.roman.apz.greenhouse.Greenhouse;

import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "automation_rule")
public class AutomationRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "greenhouse_id", referencedColumnName = "id")
    Greenhouse greenhouse;

    @NotNull
    String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    AutomationRuleType automationRuleType;

    @OneToOne(mappedBy = "automationRule")
    AutomationRuleDetails automationRuleDetails;

    @OneToMany(mappedBy = "automationRule")
    List<AutomationAction> automationActions = new ArrayList<>();

    public AutomationRule() {}

    public AutomationRule(Greenhouse greenhouse, String name, AutomationRuleType automationRuleType) {
        this.greenhouse = greenhouse;
        this.name = name;
        this.automationRuleType = automationRuleType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Greenhouse getGreenhouse() {
        return greenhouse;
    }

    public void setGreenhouse(Greenhouse greenhouse) {
        this.greenhouse = greenhouse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AutomationRuleType getAutomationRuleType() {
        return automationRuleType;
    }

    public void setAutomationRuleType(AutomationRuleType automationRuleType) {
        this.automationRuleType = automationRuleType;
    }

    public AutomationRuleDetails getAutomationRuleDetails() {
        return automationRuleDetails;
    }

    public void setAutomationRuleDetails(AutomationRuleDetails automationRuleDetails) {
        this.automationRuleDetails = automationRuleDetails;
    }

    public List<AutomationAction> getAutomationActions() {
        return automationActions;
    }

    public void setAutomationActions(List<AutomationAction> automationActions) {
        this.automationActions = automationActions;
    }
}
