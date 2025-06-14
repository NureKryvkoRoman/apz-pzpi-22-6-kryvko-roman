package ua.nure.kryvko.roman.apz.controller;

import java.util.Date;

public class ControllerResponse {
    Integer id;
    Integer greenhouseId;
    boolean isActive;
    Date installedAt;
    String name;
    ControllerType controllerType;

    public ControllerResponse() {}

    public ControllerResponse(Integer id, Integer greenhouseId, boolean isActive,
                              Date installedAt, String name, ControllerType controllerType) {
        this.id = id;
        this.greenhouseId = greenhouseId;
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
}
