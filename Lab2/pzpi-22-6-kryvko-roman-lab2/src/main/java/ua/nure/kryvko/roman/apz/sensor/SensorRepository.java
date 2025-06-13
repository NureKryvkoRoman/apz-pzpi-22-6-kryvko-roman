package ua.nure.kryvko.roman.apz.sensor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, Integer> {
    List<Sensor> findByGreenhouseIdAndSensorType(Integer greenhouseId, SensorType sensorType);

    List<Sensor> findByGreenhouseId(Integer greenhouseId);
}
