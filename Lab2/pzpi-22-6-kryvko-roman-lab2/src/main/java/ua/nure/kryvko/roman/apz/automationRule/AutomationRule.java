package ua.nure.kryvko.roman.apz.automationRule;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import ua.nure.kryvko.roman.apz.controller.Controller;
import ua.nure.kryvko.roman.apz.greenhouse.Greenhouse;

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "automation_rule")
public class AutomationRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "greenhouse_id", referencedColumnName = "id")
    Greenhouse greenhouse;

    @NotNull
    @OneToOne
    @JoinColumn(name = "controller_id", referencedColumnName = "id")
    Controller controller;

    @NotNull
    String name;

    @NotNull
    Double minValue;

    @NotNull
    Double maxValue;

    public AutomationRule() {}

    public AutomationRule(Integer id, Greenhouse greenhouse, Controller controller, String name, Double minValue, Double maxValue) {
        this.id = id;
        this.greenhouse = greenhouse;
        this.controller = controller;
        this.name = name;
        this.minValue = minValue;
        this.maxValue = maxValue;
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

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }
}
