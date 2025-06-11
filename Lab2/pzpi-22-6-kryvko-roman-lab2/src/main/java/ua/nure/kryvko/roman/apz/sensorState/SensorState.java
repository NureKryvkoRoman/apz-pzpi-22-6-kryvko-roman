package ua.nure.kryvko.roman.apz.sensorState;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import ua.nure.kryvko.roman.apz.sensor.Sensor;

import java.util.Date;

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "sensor_state")
public class SensorState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sensor_id", referencedColumnName = "id")
    @JsonBackReference
    Sensor sensor;

    @NotNull
    Date timestamp;

    @NotNull
    Float value;

    @NotNull
    String unit;

    public SensorState() {}

    public SensorState(Sensor sensor, Date timestamp, Float value, String unit) {
        this.sensor = sensor;
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

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
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
