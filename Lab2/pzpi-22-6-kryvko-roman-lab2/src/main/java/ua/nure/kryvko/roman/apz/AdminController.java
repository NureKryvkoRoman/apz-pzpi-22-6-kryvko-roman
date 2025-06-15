package ua.nure.kryvko.roman.apz;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @PostMapping("/backup")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<String> makeBackup() {
        try {
            String dbName = dbUrl.substring(dbUrl.lastIndexOf("/") + 1);
            String backupFile = "backup_" + LocalDateTime.now().toString().replace(":", "-") + ".sql";
            ProcessBuilder pb = new ProcessBuilder(
                    "pg_dump",
                    "-U", dbUser,
                    "-d", dbName,
                    "-F", "c",
                    "-f", backupFile
            );

            pb.environment().put("PGPASSWORD", dbPassword);
            pb.directory(new File(System.getProperty("user.home")));

            Process process = pb.start();

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                return ResponseEntity.ok("Backup created successfully: " + backupFile + " at " + LocalDateTime.now());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Backup failed with exit code: " + exitCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Backup failed: " + e.getMessage());
        }
    }

    @PostMapping("/restore")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<String> restoreBackup(@RequestParam String filename) {
        try {
            String dbName = dbUrl.substring(dbUrl.lastIndexOf("/") + 1);
            String backupPath = System.getProperty("user.home") + File.separator + filename;
            File file = new File(backupPath);
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Backup file not found");
            }
            ProcessBuilder pb = new ProcessBuilder(
                    "psql",
                    "-U", dbUser,
                    "-d", dbName,
                    "-f", backupPath
            );

            pb.environment().put("PGPASSWORD", dbPassword);

            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                return ResponseEntity.ok("Restore completed successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Restore failed with exit code: " + exitCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Restore failed: " + e.getMessage());
        }
    }
}
