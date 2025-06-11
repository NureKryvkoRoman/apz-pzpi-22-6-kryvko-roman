package ua.nure.kryvko.roman.apz.sensor;

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
import ua.nure.kryvko.roman.apz.user.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SensorController.class)
@Import(SensorControllerTest.TestConfig.class)
public class SensorControllerTest {

    static class TestConfig {
        @Bean
        public SensorService sensorService() {
            return Mockito.mock(SensorService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SensorService sensorService;

    @Test
    void createSensor_shouldReturnSavedSensor() throws Exception {
        Greenhouse greenhouse = new Greenhouse(new User(), LocalDateTime.now(), "Greenhouse A", 52.0f, 13.0f);
        Sensor sensor = new Sensor(greenhouse, true, LocalDateTime.now(), SensorType.TEMPERATURE, "Temp Sensor");
        Mockito.when(sensorService.saveSensor(any(Sensor.class))).thenReturn(sensor);

        mockMvc.perform(post("/api/sensors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sensor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Temp Sensor"))
                .andExpect(jsonPath("$.sensorType").value("TEMPERATURE"))
                .andExpect(jsonPath("$.isActive").value(true));
    }

    @Test
    void getSensorById_shouldReturnSensorIfFound() throws Exception {
        Greenhouse greenhouse = new Greenhouse(new User(), LocalDateTime.now(), "Greenhouse A", 52.0f, 13.0f);
        Sensor sensor = new Sensor(greenhouse, true, LocalDateTime.now(), SensorType.TEMPERATURE, "Temp Sensor");
        Mockito.when(sensorService.getSensorById(1)).thenReturn(Optional.of(sensor));

        mockMvc.perform(get("/api/sensors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Temp Sensor"))
                .andExpect(jsonPath("$.sensorType").value("TEMPERATURE"))
                .andExpect(jsonPath("$.isActive").value(true));
    }

    @Test
    void getSensorById_shouldReturnNotFoundIfNotFound() throws Exception {
        Mockito.when(sensorService.getSensorById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/sensors/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateSensor_shouldReturnUpdatedSensor() throws Exception {
        Greenhouse greenhouse = new Greenhouse(new User(), LocalDateTime.now(), "Greenhouse A", 52.0f, 13.0f);
        Sensor sensor = new Sensor(greenhouse, true, LocalDateTime.now(), SensorType.TEMPERATURE, "Temp Sensor");
        sensor.setId(1);
        Mockito.when(sensorService.updateSensor(any(Sensor.class))).thenReturn(sensor);

        mockMvc.perform(put("/api/sensors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sensor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Temp Sensor"))
                .andExpect(jsonPath("$.sensorType").value("TEMPERATURE"))
                .andExpect(jsonPath("$.isActive").value(true));
    }

    @Test
    void deleteSensor_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/sensors/1"))
                .andExpect(status().isNoContent());
    }
}
