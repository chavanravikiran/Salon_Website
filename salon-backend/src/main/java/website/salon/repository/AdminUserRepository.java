package website.salon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import website.salon.entity.AdminUser;

import java.util.Optional;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    Optional<AdminUser> findByUsername(String username);
}
