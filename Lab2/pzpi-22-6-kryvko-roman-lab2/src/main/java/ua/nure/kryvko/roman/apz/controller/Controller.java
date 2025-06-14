package ua.nure.kryvko.roman.apz.controller;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import ua.nure.kryvko.roman.apz.automationRule.AutomationRule;
import ua.nure.kryvko.roman.apz.greenhouse.Greenhouse;

import java.time.Duration;
import java.util.Date;

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "controller")
public class Controller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "greenhouse_id", referencedColumnName = "id")
    Greenhouse greenhouse;

    @NotNull
    boolean isActive;

    Date installedAt;

    String name;

    @Enumerated(EnumType.STRING)
    ControllerType controllerType;

    @Null
    @OneToOne(mappedBy = "controller", cascade = CascadeType.ALL)
    AutomationRule automationRule;

    public Controller() {}

    public Controller(Greenhouse greenhouse, boolean isActive, Date installedAt, String name, ControllerType controllerType) {
        this.greenhouse = greenhouse;
        this.isActive = isActive;
        this.installedAt = installedAt;
        this.name = name;
        this.controllerType = controllerType;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getInstalledAt() {
        return installedAt;
    }

    public void setInstalledAt(Date installedAt) {
        this.installedAt = installedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ControllerType getControllerType() {
        return controllerType;
    }

    public void setControllerType(ControllerType controllerType) {
        this.controllerType = controllerType;
    }

    public void run(Duration interval) {
        //TODO: implement
    }

    public void run() {
        //TODO: implement
    }

    public AutomationRule getAutomationRule() {
        return automationRule;
    }

    public void setAutomationRule(AutomationRule automationRule) {
        this.automationRule = automationRule;
    }
}
