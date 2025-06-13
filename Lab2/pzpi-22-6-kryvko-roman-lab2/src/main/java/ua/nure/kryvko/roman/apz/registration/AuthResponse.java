package ua.nure.kryvko.roman.apz.registration;

public class AuthResponse {
    Integer id;
    String accessToken;
    String refreshToken;
    String email;
    String username;
    String error;

    public AuthResponse(String error) {
        this.error = error;
    }

    public AuthResponse(Integer id, String accessToken, String refreshToken, String email, String username) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.email = email;
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
