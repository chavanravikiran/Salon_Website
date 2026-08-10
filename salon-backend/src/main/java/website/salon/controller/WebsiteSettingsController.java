package website.salon.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import website.salon.dto.WebsiteSettingsDtos.WebsiteSettingsRequest;
import website.salon.dto.WebsiteSettingsDtos.WebsiteSettingsResponse;
import website.salon.service.WebsiteSettingsService;

@RestController
@RequestMapping("/api/settings")
public class WebsiteSettingsController {

    private final WebsiteSettingsService service;

    public WebsiteSettingsController(WebsiteSettingsService service) {
        this.service = service;
    }

    @GetMapping
    public WebsiteSettingsResponse get() {
        return service.getSettings();
    }

    @PutMapping
    public WebsiteSettingsResponse update(@Valid @RequestBody WebsiteSettingsRequest request) {
        return service.updateSettings(request);
    }

    @PostMapping("/logo")
    public WebsiteSettingsResponse uploadLogo(@RequestParam("file") MultipartFile file) {
        return service.uploadLogo(file);
    }

    @DeleteMapping("/logo")
    public WebsiteSettingsResponse deleteLogo() {
        return service.deleteLogo();
    }

    @PostMapping("/favicon")
    public WebsiteSettingsResponse uploadFavicon(@RequestParam("file") MultipartFile file) {
        return service.uploadFavicon(file);
    }

    @DeleteMapping("/favicon")
    public WebsiteSettingsResponse deleteFavicon() {
        return service.deleteFavicon();
    }

    @PostMapping("/hero-media")
    public WebsiteSettingsResponse uploadHeroMedia(@RequestParam("file") MultipartFile file) {
        return service.uploadHeroMedia(file);
    }

    @DeleteMapping("/hero-media")
    public WebsiteSettingsResponse deleteHeroMedia() {
        return service.deleteHeroMedia();
    }
}
