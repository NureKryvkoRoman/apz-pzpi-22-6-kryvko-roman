package ua.nure.kryvko.roman.apz;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @PostMapping("/backup")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<String> makeBackup() {
        //TODO: implement
        return ResponseEntity.ok("Backed up at " + LocalDateTime.now().toString());
    }

    @PostMapping("/restore")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<String> restoreBackup() {
        //TODO: implement
        return ResponseEntity.ok("Restored backup at " + LocalDateTime.now().toString());
    }
}
