package ua.nure.kryvko.roman.apz.userinfo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import ua.nure.kryvko.roman.apz.user.User;
import ua.nure.kryvko.roman.apz.user.UserRole;
import ua.nure.kryvko.roman.apz.user.UserService;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserInfoController.class)
@Import(UserInfoControllerTest.TestConfig.class)
class UserInfoControllerTest {
    static class TestConfig {
        @Bean
        public UserInfoService userInfoService() {
            return mock(UserInfoService.class);
        }
        @Bean
        public UserService userService() {
            return mock(UserService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        reset(userService);
        reset(userInfoService);
    }

    @Test
    void getById_shouldReturnUserInfoWhenFound() throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1);
        userInfo.setFirstName("John");
        userInfo.setLastName("Doe");

        when(userInfoService.getById(1)).thenReturn(userInfo);

        mockMvc.perform(get("/api/userinfo/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(userInfoService, times(1)).getById(1);
    }

    @Test
    void getById_shouldReturnNotFoundWhenUserInfoDoesNotExist() throws Exception {
        when(userInfoService.getById(1)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/userinfo/1"))
                .andExpect(status().isNotFound());

        verify(userInfoService, times(1)).getById(1);
    }

    @Test
    void getByUserId_shouldReturnUserInfoWhenFound() throws Exception {
        User user = new User("login", "test@mail.com", "12345678", UserRole.USER);
        user.setId(1);

        UserInfo userInfo = new UserInfo();
        userInfo.setId(1);
        userInfo.setUser(user);

        when(userService.getUserById(1)).thenReturn(Optional.of(user));
        when(userInfoService.getByUser(user)).thenReturn(userInfo);

        mockMvc.perform(get("/api/userinfo/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(userService, times(1)).getUserById(1);
        verify(userInfoService, times(1)).getByUser(user);
    }

    @Test
    void getByUserId_shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        when(userService.getUserById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/userinfo/user/1"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(1);
        verify(userInfoService, never()).getByUser(any());
    }

    @Test
    void create_shouldCreateUserInfo() throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.setFirstName("John");
        userInfo.setLastName("Doe");

        mockMvc.perform(post("/api/userinfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                       {
                           "user": { "id":1 },
                           "firstName": "John",
                           "lastName": "Doe"
                       }
                       """))
                .andExpect(status().isCreated());

        verify(userInfoService, times(1)).createUserInfo(any(UserInfo.class));
    }

    @Test
    void update_shouldUpdateUserInfo() throws Exception {
        UserInfo updatedUserInfo = new UserInfo();
        updatedUserInfo.setFirstName("Updated Name");

        mockMvc.perform(patch("/api/userinfo/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                       {
                           "user": { "id":1 },
                           "firstName": "Updated Name"
                       }
                       """))
                .andExpect(status().isNoContent());

        verify(userInfoService, times(1)).updateUserInfo(any(UserInfo.class), eq(1));
    }

    @Test
    void delete_shouldDeleteUserInfo() throws Exception {
        mockMvc.perform(delete("/api/userinfo/1"))
                .andExpect(status().isNoContent());

        verify(userInfoService, times(1)).deleteUserInfo(1);
    }
}