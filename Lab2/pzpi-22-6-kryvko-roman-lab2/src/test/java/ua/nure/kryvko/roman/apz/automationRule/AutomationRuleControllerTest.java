package ua.nure.kryvko.roman.apz.automationRule;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.nure.kryvko.roman.apz.controller.Controller;
import ua.nure.kryvko.roman.apz.greenhouse.Greenhouse;
import ua.nure.kryvko.roman.apz.greenhouse.GreenhouseRepository;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AutomationRuleController.class)
@Import(AutomationRuleControllerTest.TestConfig.class)
public class AutomationRuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GreenhouseRepository greenhouseRepository;

    @Autowired
    private AutomationRuleService automationRuleService;

    static class TestConfig {
        @Bean
        public AutomationRuleService automationRuleService() {
            return mock(AutomationRuleService.class);
        }

        @Bean
        public GreenhouseRepository greenhouseRepository() {
            return mock(GreenhouseRepository.class);
        }
    }

    @Test
    void createAutomationRule_shouldReturnCreatedWhenValid() throws Exception {
        Greenhouse greenhouse = new Greenhouse();
        Controller controller = new Controller();
        greenhouse.setId(1);

        AutomationRule automationRule = new AutomationRule(1, greenhouse, controller, "Test Rule", 0.1, 0.9);

        when(greenhouseRepository.existsById(1)).thenReturn(true);
        when(automationRuleService.createAutomationRule(any())).thenReturn(automationRule);

        mockMvc.perform(post("/api/automation-rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(automationRule)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Rule"));

        verify(automationRuleService, times(1)).createAutomationRule(any());
    }

    @Test
    void createAutomationRule_shouldReturnBadRequestWhenGreenhouseDoesNotExist() throws Exception {
        Greenhouse greenhouse = new Greenhouse();
        Controller controller = new Controller();
        greenhouse.setId(1);

        AutomationRule automationRule = new AutomationRule(1, greenhouse, controller, "Test Rule", 0.1, 0.9);

        when(automationRuleService.createAutomationRule(any(AutomationRule.class)))
                .thenThrow(new IllegalArgumentException("Greenhouse does not exist."));

        mockMvc.perform(post("/api/automation-rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(automationRule)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAutomationRuleById_shouldReturnNotFoundWhenRuleDoesNotExist() throws Exception {
        when(automationRuleService.getAutomationRuleById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/automation-rules/1"))
                .andExpect(status().isNotFound());

        verify(automationRuleService, times(1)).getAutomationRuleById(1);
    }
}
