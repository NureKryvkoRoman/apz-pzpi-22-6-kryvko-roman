package ua.nure.kryvko.roman.apz.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private static UserResponse toDto(User user) {
        return new UserResponse(
                user.getId(),
                user.getLogin(),
                user.getEmail(),
                user.getRole()
        );
    }

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    List<UserResponse> findAll() {
        return userService.getAllUsers().stream().map(UserController::toDto).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @GetMapping("/{id}")
    ResponseEntity<UserResponse> findById(@PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);
        if(user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(toDto(user.get()), HttpStatus.OK);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> create(@Valid @RequestBody User user) {
        try {
            userService.saveUser(user);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    ResponseEntity<Void> update(@Valid @RequestBody User user, @PathVariable Integer id) {
        try {
            userService.updateUser(user, id);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
