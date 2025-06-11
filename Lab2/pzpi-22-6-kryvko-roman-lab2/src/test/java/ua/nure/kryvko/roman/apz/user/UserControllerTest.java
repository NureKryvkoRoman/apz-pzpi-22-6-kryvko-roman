package ua.nure.kryvko.roman.apz.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(UserControllerTest.TestConfig.class)
class UserControllerTest {
    static class TestConfig {
        @Bean
        public UserService userService() {
            return mock(UserService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        reset(userService);
        user = new User("login1", "test@mail.com", "12345678", UserRole.USER);
        user.setId(1);
    }

    @Test
    void findAll_shouldReturnListOfUsers() throws Exception {
        List<User> users = List.of(user);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(user.getId()))
                .andExpect(jsonPath("$[0].login").value(user.getLogin()))
                .andExpect(jsonPath("$[0].email").value(user.getEmail()));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void findById_shouldReturnUserWhenFound() throws Exception {
        when(userService.getUserById(1)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/user/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.login").value(user.getLogin()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));

        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void findById_shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        when(userService.getUserById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/user/{id}", 1))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void create_shouldSaveUserAndReturnCreatedStatus() throws Exception {
        User newUser = new User("login2", "newuser@mail.com", "password", UserRole.USER);
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newUser)))
                .andExpect(status().isCreated());

        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    void update_shouldUpdateUserAndReturnNoContentStatus() throws Exception {
        User updatedUser = new User("updatedLogin", "updated@mail.com", "newpassword", UserRole.USER);
        mockMvc.perform(patch("/api/user/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedUser)))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).updateUser(any(User.class), eq(1));
    }

    @Test
    void delete_shouldDeleteUserAndReturnNoContentStatus() throws Exception {
        mockMvc.perform(delete("/api/user/{id}", 1))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1);
    }
}