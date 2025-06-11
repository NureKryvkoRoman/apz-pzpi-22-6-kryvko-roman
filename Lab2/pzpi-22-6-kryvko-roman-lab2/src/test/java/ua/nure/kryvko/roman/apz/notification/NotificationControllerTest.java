package ua.nure.kryvko.roman.apz.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.nure.kryvko.roman.apz.user.User;
import ua.nure.kryvko.roman.apz.user.UserRepository;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
@Import(NotificationControllerTest.TestConfig.class)
public class NotificationControllerTest {

    static class TestConfig {
        @Bean
        public NotificationService notificationService() {
            return Mockito.mock(NotificationService.class);
        }

        @Bean
        public UserRepository userRepository() {
            return Mockito.mock(UserRepository.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        reset(userRepository);
        reset(notificationService);
    }

    @Test
    void createNotification_shouldReturnBadRequestIfUserDoesNotExist() throws Exception {
        User user = new User();
        user.setId(1);
        Notification notification = new Notification(user, null, "Test message", false, null, NotificationUrgency.INFO);

        when(notificationService.createNotification(any(Notification.class)))
                .thenThrow(new IllegalArgumentException("User must exist to create a notification."));

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notification)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNotification_shouldReturnCreatedNotification() throws Exception {
        User user = new User();
        user.setId(1);
        Mockito.when(userRepository.existsById(1)).thenReturn(true);

        Notification notification = new Notification(user, null, "Test message", false, null, NotificationUrgency.INFO);

        Mockito.when(notificationService.createNotification(any(Notification.class))).thenReturn(notification);

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notification)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Test message"))
                .andExpect(jsonPath("$.isRead").value(false));
    }

    @Test
    void getNotificationById_shouldReturnNotificationIfFound() throws Exception {
        User user = new User();
        Notification notification = new Notification(user, null, "Test message", false, new Date(), NotificationUrgency.WARNING);
        Mockito.when(notificationService.getNotificationById(1)).thenReturn(Optional.of(notification));

        mockMvc.perform(get("/api/notifications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Test message"));
    }

    @Test
    void getNotificationById_shouldReturnNotFoundIfNotFound() throws Exception {
        Mockito.when(notificationService.getNotificationById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/notifications/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateNotification_shouldReturnBadRequestIfNotificationNotFound() throws Exception {
        User user = new User();
        Notification notification = new Notification(user, null, "Updated message", false, new Date(), NotificationUrgency.CRITICAL);
        notification.setId(1);

        Mockito.when(notificationService.updateNotification(any(Notification.class))).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(put("/api/notifications/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notification)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteNotification_shouldReturnNoContentIfDeletedSuccessfully() throws Exception {
        mockMvc.perform(delete("/api/notifications/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteNotification_shouldReturnNotFoundIfNotificationNotFound() throws Exception {
        Mockito.doThrow(IllegalArgumentException.class).when(notificationService).deleteNotificationById(1);

        mockMvc.perform(delete("/api/notifications/1"))
                .andExpect(status().isNotFound());
    }
}
