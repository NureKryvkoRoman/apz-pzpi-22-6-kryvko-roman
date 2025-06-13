package ua.nure.kryvko.roman.apz.userinfo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.nure.kryvko.roman.apz.user.User;
import ua.nure.kryvko.roman.apz.user.UserService;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/userinfo")
public class UserInfoController {
    private final UserInfoService userInfoService;
    private final UserService userService;

    public UserInfoController(UserInfoService userInfoService, UserService userService) {
        this.userInfoService = userInfoService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @ResponseBody
    @PreAuthorize("@authorizationService.canAccessUserInfo(#id, authentication)")
    ResponseEntity<UserInfo> getById(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(userInfoService.getById(id), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    ResponseEntity<UserInfo> getByUserId(@PathVariable Integer id) {
        Optional<User> userOptional = userService.getUserById(id);
        if(userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(userInfoService.getByUser(userOptional.get()), HttpStatus.OK);
    }

    @GetMapping("/email")
    @PreAuthorize("hasRole('ADMIN') or #email == authentication.name")
    ResponseEntity<UserInfo> getByUserId(@RequestParam String email) {
        Optional<User> userOptional = userService.getUserByEmail(email);
        if(userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(userInfoService.getByUser(userOptional.get()), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Map<String, String>> create(@Valid @RequestBody UserInfo userInfo) {
        try {
            userInfoService.createUserInfo(userInfo);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of("error", "Error in input data"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Unexpected error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessUserInfo(#id, authentication)")
    ResponseEntity<Void> update(@Valid @RequestBody UserInfo userInfo, @PathVariable Integer id) {
        try {
            userInfoService.updateUserInfo(userInfo, id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessUserInfo(#id, authentication)")
    ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            userInfoService.deleteUserInfo(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
