package ua.nure.kryvko.roman.apz.greenhouse;

import java.time.LocalDateTime;

public class GreenhouseSummary {
    Integer id;
    Integer userId;
    LocalDateTime createdAt;
    String name;
    Float latitude;
    Float longitude;
    Integer sensorCount;

    public GreenhouseSummary() {}

    public GreenhouseSummary(Integer id, Integer userId, LocalDateTime createdAt, String name,
                             Float latitude, Float longitude, Long sensorCount) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sensorCount = sensorCount != null ? sensorCount.intValue() : 0;
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

    public Integer getSensorCount() {
        return sensorCount;
    }

    public void setSensorCount(Integer sensorCount) {
        this.sensorCount = sensorCount;
    }
}
