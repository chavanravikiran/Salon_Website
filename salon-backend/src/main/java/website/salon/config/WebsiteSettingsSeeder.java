package website.salon.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import website.salon.entity.WebsiteSettings;
import website.salon.repository.WebsiteSettingsRepository;

@Component
public class WebsiteSettingsSeeder implements CommandLineRunner {

    private static final Long SETTINGS_ID = 1L;

    private final WebsiteSettingsRepository repository;

    public WebsiteSettingsSeeder(WebsiteSettingsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            WebsiteSettings settings = new WebsiteSettings();
            settings.setId(SETTINGS_ID);
            settings.setWebsiteName("Radiance Salon & Spa");
            settings.setPhone("+1 (555) 123-4567");
            settings.setEmail("hello@radiancesalon.com");
            settings.setAddress("123 Main Street, Your City");
            settings.setDescription("Your neighborhood destination for hair, skin and relaxation.");
            settings.setBusinessHours("Mon - Sat: 9:00 AM - 7:00 PM, Sunday: Closed");
            repository.save(settings);
        }
    }
}
