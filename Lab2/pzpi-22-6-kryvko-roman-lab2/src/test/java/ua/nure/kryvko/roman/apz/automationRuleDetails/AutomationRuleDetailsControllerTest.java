package ua.nure.kryvko.roman.apz.automationRuleDetails;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.nure.kryvko.roman.apz.automationRule.AutomationRule;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AutomationRuleDetailsController.class)
@Import(AutomationRuleDetailsControllerTest.TestConfig.class)
public class AutomationRuleDetailsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AutomationRuleDetailsService automationRuleDetailsService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public AutomationRuleDetailsService automationRuleDetailsService() {
            return mock(AutomationRuleDetailsService.class);
        }
    }

    @Test
    void createAutomationRuleDetails_shouldReturnCreated() throws Exception {
        AutomationRule rule = new AutomationRule();
        rule.setId(1);
        AutomationRuleDetails details = new AutomationRuleDetails(rule, null, null, 10.0f, 5.0f);

        when(automationRuleDetailsService.createAutomationRuleDetails(any()))
                .thenReturn(details);

        mockMvc.perform(post("/api/automation-rule-details")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(details)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.maxValue").value(10.0f));

        verify(automationRuleDetailsService, times(1)).createAutomationRuleDetails(any());
    }

    @Test
    void getAutomationRuleDetailsById_shouldReturnDetails() throws Exception {
        AutomationRule rule = new AutomationRule();
        rule.setId(1);
        AutomationRuleDetails details = new AutomationRuleDetails(rule, null, null, 10.0f, 5.0f);
        details.setId(1);

        when(automationRuleDetailsService.getAutomationRuleDetailsById(1)).thenReturn(details);

        mockMvc.perform(get("/api/automation-rule-details/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.maxValue").value(10.0f));

        verify(automationRuleDetailsService, times(1)).getAutomationRuleDetailsById(1);
    }

    @Test
    void updateAutomationRuleDetails_shouldReturnUpdated() throws Exception {
        AutomationRule rule = new AutomationRule();
        rule.setId(1);
        AutomationRuleDetails details = new AutomationRuleDetails(rule, null, null, 10.0f, 5.0f);
        details.setId(1);

        when(automationRuleDetailsService.updateAutomationRuleDetails(eq(1), any()))
                .thenReturn(details);

        mockMvc.perform(put("/api/automation-rule-details/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(details)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(automationRuleDetailsService, times(1)).updateAutomationRuleDetails(eq(1), any());
    }

    @Test
    void deleteAutomationRuleDetails_shouldReturnNoContent() throws Exception {
        doNothing().when(automationRuleDetailsService).deleteAutomationRuleDetails(1);

        mockMvc.perform(delete("/api/automation-rule-details/1"))
                .andExpect(status().isNoContent());

        verify(automationRuleDetailsService, times(1)).deleteAutomationRuleDetails(1);
    }
}
