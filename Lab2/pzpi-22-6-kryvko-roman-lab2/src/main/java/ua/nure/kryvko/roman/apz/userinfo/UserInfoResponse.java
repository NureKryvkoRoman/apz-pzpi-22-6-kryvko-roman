package ua.nure.kryvko.roman.apz.userinfo;

import java.time.LocalDateTime;

public class UserInfoResponse {
    Integer id;
    Integer userId;
    LocalDateTime createdAt;
    LocalDateTime lastLogin;
    String firstName;
    String lastName;
    String phoneNumber;

    public UserInfoResponse() {}

    public UserInfoResponse(Integer id, Integer userId, LocalDateTime createdAt,
                            LocalDateTime lastLogin, String firstName, String lastName, String phoneNumber) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
