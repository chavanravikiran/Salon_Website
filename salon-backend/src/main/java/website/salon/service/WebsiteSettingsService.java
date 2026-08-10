package website.salon.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import website.salon.dto.WebsiteSettingsDtos.WebsiteSettingsRequest;
import website.salon.dto.WebsiteSettingsDtos.WebsiteSettingsResponse;
import website.salon.entity.HeroMediaType;
import website.salon.entity.WebsiteSettings;
import website.salon.exception.ResourceNotFoundException;
import website.salon.repository.WebsiteSettingsRepository;

import java.time.Instant;

@Service
@Transactional
public class WebsiteSettingsService {

    private static final Long SETTINGS_ID = 1L;

    private final WebsiteSettingsRepository repository;
    private final FileStorageService fileStorageService;

    public WebsiteSettingsService(WebsiteSettingsRepository repository, FileStorageService fileStorageService) {
        this.repository = repository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional(readOnly = true)
    public WebsiteSettingsResponse getSettings() {
        return toResponse(getEntity());
    }

    public WebsiteSettingsResponse updateSettings(WebsiteSettingsRequest request) {
        WebsiteSettings entity = getEntity();
        entity.setWebsiteName(request.websiteName());
        entity.setPhone(request.phone());
        entity.setEmail(request.email());
        entity.setAddress(request.address());
        entity.setDescription(request.description());
        entity.setBusinessHours(request.businessHours());
        entity.setFacebookUrl(request.facebookUrl());
        entity.setInstagramUrl(request.instagramUrl());
        entity.setTwitterUrl(request.twitterUrl());
        entity.setYoutubeUrl(request.youtubeUrl());
        entity.setGoogleMapEmbedUrl(request.googleMapEmbedUrl());
        entity.setUpdatedAt(Instant.now());
        return toResponse(repository.save(entity));
    }

    public WebsiteSettingsResponse uploadLogo(MultipartFile file) {
        WebsiteSettings entity = getEntity();
        if (entity.getLogoImage() != null) {
            fileStorageService.delete(entity.getLogoImage());
        }
        entity.setLogoImage(fileStorageService.store(file));
        entity.setUpdatedAt(Instant.now());
        return toResponse(repository.save(entity));
    }

    public WebsiteSettingsResponse deleteLogo() {
        WebsiteSettings entity = getEntity();
        if (entity.getLogoImage() != null) {
            fileStorageService.delete(entity.getLogoImage());
            entity.setLogoImage(null);
            entity.setUpdatedAt(Instant.now());
        }
        return toResponse(repository.save(entity));
    }

    public WebsiteSettingsResponse uploadFavicon(MultipartFile file) {
        WebsiteSettings entity = getEntity();
        if (entity.getFaviconImage() != null) {
            fileStorageService.delete(entity.getFaviconImage());
        }
        entity.setFaviconImage(fileStorageService.store(file));
        entity.setUpdatedAt(Instant.now());
        return toResponse(repository.save(entity));
    }

    public WebsiteSettingsResponse deleteFavicon() {
        WebsiteSettings entity = getEntity();
        if (entity.getFaviconImage() != null) {
            fileStorageService.delete(entity.getFaviconImage());
            entity.setFaviconImage(null);
            entity.setUpdatedAt(Instant.now());
        }
        return toResponse(repository.save(entity));
    }

    public WebsiteSettingsResponse uploadHeroMedia(MultipartFile file) {
        WebsiteSettings entity = getEntity();
        if (entity.getHeroMediaUrl() != null) {
            fileStorageService.delete(entity.getHeroMediaUrl());
        }
        entity.setHeroMediaUrl(fileStorageService.store(file));
        entity.setHeroMediaType(UploadFileNaming.isVideo(file) ? HeroMediaType.VIDEO : HeroMediaType.IMAGE);
        entity.setUpdatedAt(Instant.now());
        return toResponse(repository.save(entity));
    }

    public WebsiteSettingsResponse deleteHeroMedia() {
        WebsiteSettings entity = getEntity();
        if (entity.getHeroMediaUrl() != null) {
            fileStorageService.delete(entity.getHeroMediaUrl());
            entity.setHeroMediaUrl(null);
            entity.setHeroMediaType(null);
            entity.setUpdatedAt(Instant.now());
        }
        return toResponse(repository.save(entity));
    }

    private WebsiteSettings getEntity() {
        return repository.findById(SETTINGS_ID)
                .orElseThrow(() -> new ResourceNotFoundException("Website settings not found"));
    }

    private WebsiteSettingsResponse toResponse(WebsiteSettings entity) {
        return new WebsiteSettingsResponse(
                entity.getId(),
                entity.getWebsiteName(),
                entity.getLogoImage(),
                entity.getFaviconImage(),
                entity.getHeroMediaUrl(),
                entity.getHeroMediaType(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getAddress(),
                entity.getDescription(),
                entity.getBusinessHours(),
                entity.getFacebookUrl(),
                entity.getInstagramUrl(),
                entity.getTwitterUrl(),
                entity.getYoutubeUrl(),
                entity.getGoogleMapEmbedUrl(),
                entity.getUpdatedAt()
        );
    }
}
