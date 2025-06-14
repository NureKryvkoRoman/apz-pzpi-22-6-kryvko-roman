package ua.nure.kryvko.roman.apz.sensor;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import ua.nure.kryvko.roman.apz.greenhouse.Greenhouse;
import ua.nure.kryvko.roman.apz.sensorState.SensorState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "sensor")
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "greenhouse_id", referencedColumnName = "id")
    @JsonBackReference
    Greenhouse greenhouse;

    @NotNull
    boolean isActive;

    LocalDateTime installedAt;

    @Enumerated(EnumType.STRING)
    SensorType sensorType;

    String name;

    @OneToMany(mappedBy = "sensor")
    @JsonManagedReference
    List<SensorState> sensorStates = new ArrayList<>();

    public Sensor() {}

    public Sensor(Greenhouse greenhouse, boolean isActive, LocalDateTime installedAt, SensorType sensorType, String name) {
        this.greenhouse = greenhouse;
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

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public Greenhouse getGreenhouse() {
        return greenhouse;
    }

    public void setGreenhouse(Greenhouse greenhouse) {
        this.greenhouse = greenhouse;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<SensorState> getSensorStates() {
        return sensorStates;
    }

    public void setSensorStates(List<SensorState> sensorStates) {
        this.sensorStates = sensorStates;
    }

    public LocalDateTime getInstalledAt() {
        return installedAt;
    }

    public void setInstalledAt(LocalDateTime installedAt) {
        this.installedAt = installedAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
