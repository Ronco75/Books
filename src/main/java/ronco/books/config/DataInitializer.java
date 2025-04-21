package ronco.books.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ronco.books.model.User;
import ronco.books.service.UserService;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;

    @Autowired
    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        // Create admin user if it doesn't exist
        if (!userService.existsByUsername("admin")) {
            User adminUser = User.builder()
                    .username("admin")
                    .password("admin123")
                    .role("ROLE_ADMIN")
                    .build();
            userService.save(adminUser);
            System.out.println("Admin user created");
        }

        // Create regular user if it doesn't exist
        if (!userService.existsByUsername("user")) {
            User regularUser = User.builder()
                    .username("user")
                    .password("user123")
                    .role("ROLE_USER")
                    .build();
            userService.save(regularUser);
            System.out.println("Regular user created");
        }
    }
}