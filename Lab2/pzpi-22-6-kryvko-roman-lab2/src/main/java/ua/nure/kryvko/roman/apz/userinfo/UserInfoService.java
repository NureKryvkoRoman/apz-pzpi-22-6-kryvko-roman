package ua.nure.kryvko.roman.apz.userinfo;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ua.nure.kryvko.roman.apz.user.User;
import ua.nure.kryvko.roman.apz.user.UserRepository;

import java.util.Optional;

@Service
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;

    public UserInfoService(UserInfoRepository userInfoRepository, UserRepository userRepository) {
        this.userInfoRepository = userInfoRepository;
        this.userRepository = userRepository;
    }

    public boolean existsByUser(User user) {
        return userInfoRepository.existsByUser(user);
    }

    public UserInfo getById(Integer id) {
        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(id);
        if(userInfoOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return userInfoOptional.get();
    }

    public UserInfo getByUser(User user) {
        Optional<UserInfo> userInfoOptional = userInfoRepository.findByUser(user);
        if(userInfoOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return userInfoOptional.get();
    }

    @Transactional
    public void createUserInfo(UserInfo userInfo) {
        if (existsByUser(userInfo.getUser())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User info already exists!");
        }
        Integer userId = userInfo.getUser().getId();
        User managedUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        userInfo.setUser(managedUser);
        userInfoRepository.save(userInfo);
    }

    @Transactional
    public void updateUserInfo(UserInfo updatedUserInfo, Integer id) {
        Optional<UserInfo> optionalUserInfo = userInfoRepository.findById(id);
        if (optionalUserInfo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        UserInfo existingUserInfo = optionalUserInfo.get();

        existingUserInfo.setFirstName(updatedUserInfo.getFirstName());
        existingUserInfo.setLastName(updatedUserInfo.getLastName());
        existingUserInfo.setPhoneNumber(updatedUserInfo.getPhoneNumber());
        existingUserInfo.setLastLogin(updatedUserInfo.getLastLogin());

        userInfoRepository.save(existingUserInfo);
    }

    @Transactional
    public void deleteUserInfo(Integer id) {
        Optional<UserInfo> optionalUserInfo = userInfoRepository.findById(id);
        if (optionalUserInfo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        userInfoRepository.deleteById(id);
    }
}
