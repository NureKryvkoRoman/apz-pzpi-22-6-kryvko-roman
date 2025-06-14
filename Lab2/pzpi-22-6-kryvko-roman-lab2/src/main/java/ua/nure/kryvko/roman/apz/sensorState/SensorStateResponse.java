package ua.nure.kryvko.roman.apz.sensorState;

import java.util.Date;

public class SensorStateResponse {
    Integer id;
    Integer sensorId;
    Date timestamp;
    Float value;
    String unit;

    public SensorStateResponse() { }

    public SensorStateResponse(Integer id, Integer sensorId, Date timestamp, Float value, String unit) {
        this.id = id;
        this.sensorId = sensorId;
        this.timestamp = timestamp;
        this.value = value;
        this.unit = unit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSensorId() {
        return sensorId;
    }

    public void setSensorId(Integer sensorId) {
        this.sensorId = sensorId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
