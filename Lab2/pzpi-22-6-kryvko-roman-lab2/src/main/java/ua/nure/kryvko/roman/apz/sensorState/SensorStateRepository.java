package ua.nure.kryvko.roman.apz.sensorState;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nure.kryvko.roman.apz.greenhouse.Greenhouse;
import ua.nure.kryvko.roman.apz.sensor.Sensor;

import java.util.List;

public interface SensorStateRepository extends JpaRepository<SensorState, Integer> {
    List<SensorState> findBySensor (Sensor sensor);
}
