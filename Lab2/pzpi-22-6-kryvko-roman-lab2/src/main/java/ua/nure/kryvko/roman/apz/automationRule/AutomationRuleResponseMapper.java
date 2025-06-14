package ua.nure.kryvko.roman.apz.automationRule;

public class AutomationRuleResponseMapper {
    public static AutomationRuleResponse toDto(AutomationRule automationRule) {
        return new AutomationRuleResponse(
                automationRule.getName(),
                automationRule.getId(),
                automationRule.getGreenhouse().getId(),
                automationRule.getController().getId(),
                automationRule.getMinValue(),
                automationRule.getMaxValue()
        );
    }
}
