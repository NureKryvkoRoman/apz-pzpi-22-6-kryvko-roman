package ua.nure.kryvko.roman.apz.sensorState;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.nure.kryvko.roman.apz.greenhouse.Greenhouse;
import ua.nure.kryvko.roman.apz.sensor.Sensor;
import ua.nure.kryvko.roman.apz.sensor.SensorType;
import ua.nure.kryvko.roman.apz.user.User;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SensorStateController.class)
@Import(SensorStateControllerTest.TestConfig.class)
public class SensorStateControllerTest {

    static class TestConfig {
        @Bean
        public SensorStateService sensorStateService() {
            return Mockito.mock(SensorStateService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SensorStateService sensorStateService;

    @Test
    void createSensorState_shouldReturnSavedSensorState() throws Exception {
        Sensor sensor = new Sensor(new Greenhouse(new User(), LocalDateTime.now(), "Greenhouse A", 52.0f, 13.0f), true, LocalDateTime.now(), SensorType.TEMPERATURE, "Temp Sensor");
        SensorState sensorState = new SensorState(sensor, new Date(), 25.5f, "Celsius");
        Mockito.when(sensorStateService.saveSensorState(any(SensorState.class))).thenReturn(sensorState);

        mockMvc.perform(post("/api/sensor-states")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sensorState)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(25.5))
                .andExpect(jsonPath("$.unit").value("Celsius"))
                .andExpect(jsonPath("$.sensor.name").value("Temp Sensor"));
    }

    @Test
    void getSensorStateById_shouldReturnSensorStateIfFound() throws Exception {
        Sensor sensor = new Sensor(new Greenhouse(new User(), LocalDateTime.now(), "Greenhouse A", 52.0f, 13.0f), true, LocalDateTime.now(), SensorType.TEMPERATURE, "Temp Sensor");
        SensorState sensorState = new SensorState(sensor, new Date(), 25.5f, "Celsius");
        Mockito.when(sensorStateService.getSensorStateById(1)).thenReturn(Optional.of(sensorState));

        mockMvc.perform(get("/api/sensor-states/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(25.5))
                .andExpect(jsonPath("$.unit").value("Celsius"))
                .andExpect(jsonPath("$.sensor.name").value("Temp Sensor"));
    }

    @Test
    void getSensorStateById_shouldReturnNotFoundIfNotFound() throws Exception {
        Mockito.when(sensorStateService.getSensorStateById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/sensor-states/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateSensorState_shouldReturnUpdatedSensorState() throws Exception {
        Sensor sensor = new Sensor(new Greenhouse(new User(), LocalDateTime.now(), "Greenhouse A", 52.0f, 13.0f), true, LocalDateTime.now(), SensorType.TEMPERATURE, "Temp Sensor");
        SensorState sensorState = new SensorState(sensor, new Date(), 26.0f, "Celsius");
        sensorState.setId(1);
        Mockito.when(sensorStateService.updateSensorState(any(SensorState.class))).thenReturn(sensorState);

        mockMvc.perform(put("/api/sensor-states/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sensorState)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(26.0))
                .andExpect(jsonPath("$.unit").value("Celsius"))
                .andExpect(jsonPath("$.sensor.name").value("Temp Sensor"));
    }

    @Test
    void deleteSensorState_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/sensor-states/1"))
                .andExpect(status().isNoContent());
    }
}
