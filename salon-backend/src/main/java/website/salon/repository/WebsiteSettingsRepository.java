package website.salon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import website.salon.entity.WebsiteSettings;

public interface WebsiteSettingsRepository extends JpaRepository<WebsiteSettings, Long> {
}
