package ua.nure.kryvko.roman.apz.automationAction;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import ua.nure.kryvko.roman.apz.automationRule.AutomationRule;
import ua.nure.kryvko.roman.apz.controller.Controller;

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "automation_action")
public class AutomationAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull
    String name;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "automation_rule_id", referencedColumnName = "id")
    AutomationRule automationRule;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "controller_id", referencedColumnName = "id")
    Controller controller;

    public AutomationAction(String name, AutomationRule automationRule, Controller controller) {
        this.name = name;
        this.automationRule = automationRule;
        this.controller = controller;
    }

    public AutomationAction() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AutomationRule getAutomationRule() {
        return automationRule;
    }

    public void setAutomationRule(AutomationRule automationRule) {
        this.automationRule = automationRule;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
