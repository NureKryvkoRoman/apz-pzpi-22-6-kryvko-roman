package ua.nure.kryvko.roman.apz.greenhouse;

import ua.nure.kryvko.roman.apz.automationRule.AutomationRuleResponse;
import ua.nure.kryvko.roman.apz.controller.ControllerResponse;
import ua.nure.kryvko.roman.apz.notification.NotificationResponse;
import ua.nure.kryvko.roman.apz.sensor.SensorResponse;

import java.time.LocalDateTime;
import java.util.List;

public class GreenhouseResponse {
    Integer id;
    Integer userId;
    LocalDateTime createdAt;
    String name;
    Float latitude;
    Float longitude;
    List<SensorResponse> sensors;
    List<ControllerResponse> controllers;
    List<NotificationResponse> notifications;
    List<AutomationRuleResponse> automationRules;

    public GreenhouseResponse() {}

    public GreenhouseResponse(Integer id, Integer userId, LocalDateTime createdAt, String name,
                              Float latitude, Float longitude, List<SensorResponse> sensors,
                              List<ControllerResponse> controllers, List<NotificationResponse> notifications,
                              List<AutomationRuleResponse> automationRules) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sensors = sensors;
        this.controllers = controllers;
        this.notifications = notifications;
        this.automationRules = automationRules;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public List<SensorResponse> getSensors() {
        return sensors;
    }

    public void setSensors(List<SensorResponse> sensors) {
        this.sensors = sensors;
    }

    public List<ControllerResponse> getControllers() {
        return controllers;
    }

    public void setControllers(List<ControllerResponse> controllers) {
        this.controllers = controllers;
    }

    public List<NotificationResponse> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationResponse> notifications) {
        this.notifications = notifications;
    }

    public List<AutomationRuleResponse> getAutomationRules() {
        return automationRules;
    }

    public void setAutomationRules(List<AutomationRuleResponse> automationRules) {
        this.automationRules = automationRules;
    }
}
