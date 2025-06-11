package ua.nure.kryvko.roman.apz.registration;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    private String email;

    private String login;

    @NotBlank(message = "Password is required")
    private String password;

    public LoginRequest(String email, String login, String password) {
        this.email = email;
        this.login = login;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

}