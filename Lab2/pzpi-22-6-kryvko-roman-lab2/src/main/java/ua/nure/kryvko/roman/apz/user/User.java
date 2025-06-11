package ua.nure.kryvko.roman.apz.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import ua.nure.kryvko.roman.apz.greenhouse.Greenhouse;
import ua.nure.kryvko.roman.apz.notification.Notification;
import ua.nure.kryvko.roman.apz.subscription.Subscription;
import ua.nure.kryvko.roman.apz.userinfo.UserInfo;

import java.util.List;

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "\"user\"")
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Integer id;

        @NotEmpty
        String login;

        @Email
        @NotEmpty
        String email;

        @NotEmpty
        String password;

        @Enumerated(EnumType.STRING)
        UserRole role;

        @OneToOne(mappedBy = "user")
        UserInfo userInfo;

        @OneToMany(mappedBy = "user")
        List<Subscription> subscriptions;

        @OneToMany(mappedBy = "user")
        List<Greenhouse> greenhouses;

        @OneToMany(mappedBy = "user")
        List<Notification> notifications;

        public User() {}

        public User(String login, String email, String password, UserRole role) {
                this.login = login;
                this.email = email;
                this.password = password;
                this.role = role;
        }

        public Integer getId() {
                return id;
        }

        public String getLogin() {
                return login;
        }

        public String getEmail() {
                return email;
        }

        public String getPassword() {
                return password;
        }

        public UserRole getRole() {
                return role;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public void setLogin(String login) {
                this.login = login;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public void setRole(UserRole role) {
                this.role = role;
        }
        public boolean hasRole(UserRole role) {
                return this.role == role;
        }
}
