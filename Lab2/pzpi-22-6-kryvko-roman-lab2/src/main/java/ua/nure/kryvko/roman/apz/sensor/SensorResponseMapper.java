package ua.nure.kryvko.roman.apz.sensor;

public class SensorResponseMapper {
    public static SensorResponse toDto (Sensor sensor) {
        return new SensorResponse(
                sensor.getId(),
                sensor.getGreenhouse().getId(),
                sensor.getIsActive(),
                sensor.getInstalledAt(),
                sensor.getSensorType(),
                sensor.getName()
        );
    }
}
