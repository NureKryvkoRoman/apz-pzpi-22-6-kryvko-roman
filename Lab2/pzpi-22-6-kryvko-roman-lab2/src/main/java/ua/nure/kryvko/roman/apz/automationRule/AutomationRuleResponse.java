package ua.nure.kryvko.roman.apz.automationRule;

public class AutomationRuleResponse {
    Integer id;
    Integer greenhouseId;
    Integer controllerId;
    String name;
    Double minValue;
    Double maxValue;

    public AutomationRuleResponse() { }

    public AutomationRuleResponse(String name, Integer id, Integer greenhouseId,
                                  Integer controllerId, Double minValue, Double maxValue) {
        this.name = name;
        this.id = id;
        this.greenhouseId = greenhouseId;
        this.controllerId = controllerId;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGreenhouseId() {
        return greenhouseId;
    }

    public void setGreenhouseId(Integer greenhouseId) {
        this.greenhouseId = greenhouseId;
    }

    public Integer getControllerId() {
        return controllerId;
    }

    public void setControllerId(Integer controllerId) {
        this.controllerId = controllerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
