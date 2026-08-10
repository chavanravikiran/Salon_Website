package website.salon.dto;

import jakarta.validation.constraints.NotBlank;
import website.salon.entity.HeroMediaType;

import java.time.Instant;

public class WebsiteSettingsDtos {

    public record WebsiteSettingsRequest(
            @NotBlank String websiteName,
            String phone,
            String email,
            String address,
            String description,
            String businessHours,
            String facebookUrl,
            String instagramUrl,
            String twitterUrl,
            String youtubeUrl,
            String googleMapEmbedUrl
    ) {}

    public record WebsiteSettingsResponse(
            Long id,
            String websiteName,
            String logoImage,
            String faviconImage,
            String heroMediaUrl,
            HeroMediaType heroMediaType,
            String phone,
            String email,
            String address,
            String description,
            String businessHours,
            String facebookUrl,
            String instagramUrl,
            String twitterUrl,
            String youtubeUrl,
            String googleMapEmbedUrl,
            Instant updatedAt
    ) {}
}
