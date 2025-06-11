package ua.nure.kryvko.roman.apz.automationRuleDetails;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import ua.nure.kryvko.roman.apz.automationRule.AutomationRule;

import java.sql.Time;
import java.time.Duration;

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "automation_rule_details")
public class AutomationRuleDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "automation_rule_id", referencedColumnName = "id")
    AutomationRule automationRule;

    @Nullable
    Time startTime;

    @Nullable
    Duration interval;

    @Nullable
    Float maxValue;

    @Nullable
    Float minValue;

    public AutomationRuleDetails() {}

    public AutomationRuleDetails(AutomationRule automationRule, @Nullable Time startTime, @Nullable Duration interval, @Nullable Float maxValue, @Nullable Float minValue) {
        this.automationRule = automationRule;
        this.startTime = startTime;
        this.interval = interval;
        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AutomationRule getAutomationRule() {
        return automationRule;
    }

    public void setAutomationRule(AutomationRule automationRule) {
        this.automationRule = automationRule;
    }

    @Nullable
    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(@Nullable Time startTime) {
        this.startTime = startTime;
    }

    @Nullable
    public Duration getInterval() {
        return interval;
    }

    public void setInterval(@Nullable Duration interval) {
        this.interval = interval;
    }

    @Nullable
    public Float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(@Nullable Float maxValue) {
        this.maxValue = maxValue;
    }

    @Nullable
    public Float getMinValue() {
        return minValue;
    }

    public void setMinValue(@Nullable Float minValue) {
        this.minValue = minValue;
    }
}
