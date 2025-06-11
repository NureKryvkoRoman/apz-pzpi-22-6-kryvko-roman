package ua.nure.kryvko.roman.apz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.nure.kryvko.roman.apz.greenhouse.GreenhouseRepository;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ControllerController.class)
@Import(ControllerControllerTest.TestConfig.class)
public class ControllerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ControllerService controllerService;

    @Autowired
    private GreenhouseRepository greenhouseRepository;

    static class TestConfig {
        @Bean
        public ControllerService controllerService() {
            return mock(ControllerService.class);
        }

        @Bean
        public GreenhouseRepository greenhouseRepository() {
            return mock(GreenhouseRepository.class);
        }
    }

    @BeforeEach
    void setUp() {
        reset(controllerService);
        reset(greenhouseRepository);
    }

    @Test
    void createController_shouldReturnBadRequestIfGreenhouseDoesNotExist() throws Exception {
        Controller controller = new Controller();
        controller.setName("Test Controller");

        when(controllerService.createController(any(Controller.class)))
                .thenThrow(new IllegalArgumentException("Greenhouse must exist to create a controller."));

        mockMvc.perform(post("/api/controllers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(controller)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getControllerById_shouldReturnControllerWhenExists() throws Exception {
        Controller controller = new Controller();
        controller.setId(1);
        controller.setName("Test Controller");

        when(controllerService.getControllerById(1)).thenReturn(Optional.of(controller));

        mockMvc.perform(get("/api/controllers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Controller"));
    }

    @Test
    void getControllerById_shouldReturnNotFoundWhenControllerDoesNotExist() throws Exception {
        when(controllerService.getControllerById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/controllers/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteController_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/controllers/1"))
                .andExpect(status().isNoContent());

        verify(controllerService, times(1)).deleteController(1);
    }
}
