package ua.nure.kryvko.roman.apz.greenhouse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.nure.kryvko.roman.apz.user.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GreenhouseController.class)
@Import(GreenhouseControllerTest.TestConfig.class)
public class GreenhouseControllerTest {
    static class TestConfig {
        @Bean
        public GreenhouseService greenhouseService() {
            return mock(GreenhouseService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GreenhouseService greenhouseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createGreenhouse_shouldReturnSavedGreenhouse() throws Exception {
        Greenhouse greenhouse = new Greenhouse(new User(), LocalDateTime.now(), "Greenhouse A", 52.0f, 13.0f);
        Mockito.when(greenhouseService.saveGreenhouse(any(Greenhouse.class))).thenReturn(greenhouse);

        mockMvc.perform(post("/api/greenhouses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(greenhouse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Greenhouse A"))
                .andExpect(jsonPath("$.latitude").value(52.0))
                .andExpect(jsonPath("$.longitude").value(13.0));
    }

    @Test
    void getGreenhouseById_shouldReturnGreenhouseIfFound() throws Exception {
        Greenhouse greenhouse = new Greenhouse(new User(), LocalDateTime.now(), "Greenhouse A", 52.0f, 13.0f);
        Mockito.when(greenhouseService.getGreenhouseById(1)).thenReturn(Optional.of(greenhouse));

        mockMvc.perform(get("/api/greenhouses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Greenhouse A"))
                .andExpect(jsonPath("$.latitude").value(52.0))
                .andExpect(jsonPath("$.longitude").value(13.0));
    }

    @Test
    void getGreenhouseById_shouldReturnNotFoundIfNotFound() throws Exception {
        Mockito.when(greenhouseService.getGreenhouseById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/greenhouses/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateGreenhouse_shouldReturnUpdatedGreenhouse() throws Exception {
        Greenhouse greenhouse = new Greenhouse(new User(), LocalDateTime.now(), "Greenhouse B", 53.0f, 14.0f);
        Mockito.when(greenhouseService.updateGreenhouse(1, any(Greenhouse.class))).thenReturn(greenhouse);

        mockMvc.perform(put("/api/greenhouses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(greenhouse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Greenhouse B"))
                .andExpect(jsonPath("$.latitude").value(53.0))
                .andExpect(jsonPath("$.longitude").value(14.0));
    }

    @Test
    void deleteGreenhouse_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/greenhouses/1"))
                .andExpect(status().isNoContent());
    }
}
