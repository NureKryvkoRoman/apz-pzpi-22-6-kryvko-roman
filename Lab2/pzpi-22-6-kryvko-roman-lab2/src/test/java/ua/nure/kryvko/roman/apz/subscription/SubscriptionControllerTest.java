package ua.nure.kryvko.roman.apz.subscription;

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

@WebMvcTest(SubscriptionController.class)
@Import(SubscriptionControllerTest.TestConfig.class)
public class SubscriptionControllerTest {
    static class TestConfig {
        @Bean
        public SubscriptionService subscriptionService() {
            return mock(SubscriptionService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createSubscription_shouldReturnSavedSubscription() throws Exception {
        Subscription subscription = new Subscription(new User(), LocalDateTime.now(), LocalDateTime.now().plusDays(30), SubscriptionStatus.ACTIVE);
        Mockito.when(subscriptionService.saveSubscription(any(Subscription.class))).thenReturn(subscription);

        mockMvc.perform(post("/api/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subscription)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void getSubscriptionById_shouldReturnSubscriptionIfFound() throws Exception {
        Subscription subscription = new Subscription(new User(), LocalDateTime.now(), LocalDateTime.now().plusDays(30), SubscriptionStatus.ACTIVE);
        Mockito.when(subscriptionService.getSubscriptionById(1)).thenReturn(Optional.of(subscription));

        mockMvc.perform(get("/api/subscriptions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void getSubscriptionById_shouldReturnNotFoundIfNotFound() throws Exception {
        Mockito.when(subscriptionService.getSubscriptionById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/subscriptions/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateSubscription_shouldReturnUpdatedSubscription() throws Exception {
        Subscription subscription = new Subscription(new User(), LocalDateTime.now(), LocalDateTime.now().plusDays(30), SubscriptionStatus.ACTIVE);
        subscription.setId(1);
        Mockito.when(subscriptionService.updateSubscription(1, any(Subscription.class))).thenReturn(subscription);

        mockMvc.perform(put("/api/subscriptions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subscription)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void deleteSubscription_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/subscriptions/1"))
                .andExpect(status().isNoContent());
    }
}
