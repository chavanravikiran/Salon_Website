package website.salon.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import website.salon.entity.AdminUser;
import website.salon.repository.AdminUserRepository;

@Component
public class AdminUserSeeder implements CommandLineRunner {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.default-username}")
    private String defaultUsername;

    @Value("${app.admin.default-password}")
    private String defaultPassword;

    public AdminUserSeeder(AdminUserRepository adminUserRepository, PasswordEncoder passwordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (adminUserRepository.findByUsername(defaultUsername).isEmpty()) {
            AdminUser adminUser = new AdminUser();
            adminUser.setUsername(defaultUsername);
            adminUser.setPassword(passwordEncoder.encode(defaultPassword));
            adminUser.setRole("ROLE_ADMIN");
            adminUserRepository.save(adminUser);
        }
    }
}
