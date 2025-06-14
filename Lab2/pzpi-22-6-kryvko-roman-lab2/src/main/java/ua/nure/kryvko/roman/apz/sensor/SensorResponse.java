package ua.nure.kryvko.roman.apz.sensor;

import java.time.LocalDateTime;

public class SensorResponse {
    Integer id;
    Integer greenhouseId;
    boolean isActive;
    LocalDateTime installedAt;
    SensorType sensorType;
    String name;
    //NOTE: we don't include sensor states, as there can be too many

    public SensorResponse() {}
    public SensorResponse(Integer id, Integer greenhouseId, boolean isActive, LocalDateTime installedAt,
                          SensorType sensorType, String name) {
        this.id = id;
        this.greenhouseId = greenhouseId;
        this.isActive = isActive;
        this.installedAt = installedAt;
        this.sensorType = sensorType;
        this.name = name;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getInstalledAt() {
        return installedAt;
    }

    public void setInstalledAt(LocalDateTime installedAt) {
        this.installedAt = installedAt;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
