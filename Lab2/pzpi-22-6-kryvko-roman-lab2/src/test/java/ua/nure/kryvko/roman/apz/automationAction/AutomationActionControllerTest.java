package ua.nure.kryvko.roman.apz.automationAction;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ua.nure.kryvko.roman.apz.automationRule.AutomationRule;
import ua.nure.kryvko.roman.apz.controller.Controller;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AutomationActionController.class)
@Import(AutomationActionControllerTest.TestConfig.class)
public class AutomationActionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AutomationActionService automationActionService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public AutomationActionService automationActionService() {
            return mock(AutomationActionService.class);
        }
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAutomationAction_shouldReturnBadRequestIfRuleOrControllerDoesNotExist() throws Exception {
        AutomationAction action = new AutomationAction();
        action.setName("Test Action");
        action.setAutomationRule(new AutomationRule());
        action.getAutomationRule().setId(1);
        action.setController(new Controller());
        action.getController().setId(1);

        when(automationActionService.createAutomationAction(Mockito.any(AutomationAction.class)))
                .thenThrow(new IllegalArgumentException("Automation rule does not exist."));

        mockMvc.perform(post("/api/automation-actions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(action)))
                .andExpect(status().isBadRequest());

        verify(automationActionService, times(1)).createAutomationAction(Mockito.any(AutomationAction.class));
    }

    @Test
    void getAutomationActionById_shouldReturnNotFoundIfActionDoesNotExist() throws Exception {
        when(automationActionService.getAutomationActionById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/automation-actions/1"))
                .andExpect(status().isNotFound());

        verify(automationActionService, times(1)).getAutomationActionById(1);
    }

    @Test
    void deleteAutomationAction_shouldReturnNotFoundIfActionDoesNotExist() throws Exception {
        doThrow(new IllegalArgumentException("Automation action does not exist."))
                .when(automationActionService).deleteAutomationAction(1);

        mockMvc.perform(delete("/api/automation-actions/1"))
                .andExpect(status().isNotFound());

        verify(automationActionService, times(1)).deleteAutomationAction(1);
    }

    @Test
    void getAllAutomationActions_shouldReturnListOfActions() throws Exception {
        List<AutomationAction> actions = List.of(
                new AutomationAction("Action 1", new AutomationRule(), new Controller()),
                new AutomationAction("Action 2", new AutomationRule(), new Controller())
        );

        when(automationActionService.getAllAutomationActions()).thenReturn(actions);

        mockMvc.perform(get("/api/automation-actions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)));

        verify(automationActionService, times(1)).getAllAutomationActions();
    }
}
