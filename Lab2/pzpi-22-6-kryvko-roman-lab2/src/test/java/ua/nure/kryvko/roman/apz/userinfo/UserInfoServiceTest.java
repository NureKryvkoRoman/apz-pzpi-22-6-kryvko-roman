package ua.nure.kryvko.roman.apz.userinfo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.nure.kryvko.roman.apz.user.User;
import ua.nure.kryvko.roman.apz.user.UserRepository;
import ua.nure.kryvko.roman.apz.user.UserRole;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserInfoServiceTest {

    @Mock
    private UserInfoRepository userInfoRepository;

    private UserInfoService userInfoService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userInfoService = new UserInfoService(userInfoRepository, userRepository);
    }

    @Test
    void getById() {
        User user = new User("login1",
                "test@mail.com",
                "12345678",
                UserRole.USER);
        UserInfo userInfo = new UserInfo(user,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "first name 1",
                "last name 1",
                "+38111111111");
        when(userInfoRepository.findById(1)).thenReturn(Optional.of(userInfo));

        UserInfo userInfoFound = userInfoService.getById(1);
        assertNotNull(userInfoFound);
        assertEquals(userInfo, userInfoFound);
    }

    @Test
    void getByUser() {
        User user = new User("login1",
                "test@mail.com",
                "12345678",
                UserRole.USER);
        UserInfo userInfo = new UserInfo(user,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "first name 1",
                "last name 1",
                "+38111111111");
        when(userInfoRepository.findByUser(user)).thenReturn(Optional.of(userInfo));

        UserInfo userInfoFound = userInfoService.getByUser(user);
        assertEquals(userInfo, userInfoFound);
    }

    @Test
    void createUserInfo() {
        User user1 = new User("login2",
                "test2@mail.com",
                "12345678",
                UserRole.USER);
        user1.setId(1);
        when(userRepository.findAll()).thenReturn(List.of(user1));
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));

        User user = userRepository.findAll().getFirst();
        UserInfo userInfo = new UserInfo(user,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "first name 2",
                "last name 2",
                "+38222222222");

        userInfoService.createUserInfo(userInfo);
        verify(userInfoRepository, times(1)).save(userInfo);

        when(userInfoRepository.findByUser(user)).thenReturn(Optional.of(userInfo));
        UserInfo retrievedUserInfo = userInfoService.getByUser(user);

        assertEquals(userInfo, retrievedUserInfo);
    }

    @Test
    void updateUserInfo() {
        User user = new User("login1", "test@mail.com", "12345678", UserRole.USER);
        UserInfo existingUserInfo = new UserInfo(user,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "Old FirstName",
                "Old LastName",
                "+380123456789");
        existingUserInfo.setId(1);

        when(userInfoRepository.findById(existingUserInfo.getId()))
                .thenReturn(Optional.of(existingUserInfo));

        UserInfo updatedUserInfo = new UserInfo(user,
                existingUserInfo.getCreatedAt(),
                existingUserInfo.getLastLogin(),
                "New FirstName",
                "New LastName",
                "+380987654321");
        updatedUserInfo.setId(1);

        userInfoService.updateUserInfo(updatedUserInfo, existingUserInfo.getId());

        verify(userInfoRepository).save(existingUserInfo);
        assertEquals("New FirstName", existingUserInfo.getFirstName());
        assertEquals("New LastName", existingUserInfo.getLastName());
        assertEquals("+380987654321", existingUserInfo.getPhoneNumber());
    }

    @Test
    void deleteUserInfo() {
        User user = new User("login2", "test2@mail.com", "12345678", UserRole.USER);
        UserInfo userInfo = new UserInfo(user,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "FirstName",
                "LastName",
                "+380987654321");

        when(userInfoRepository.findById(userInfo.getId()))
                .thenReturn(Optional.of(userInfo));

        userInfoService.deleteUserInfo(userInfo.getId());

        verify(userInfoRepository).deleteById(userInfo.getId());
    }
}