package website.salon.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import website.salon.service.FileStorageService;

import java.net.URI;

/**
 * Fallback for any stray direct hit on /uploads/** (e.g. an old bookmarked or hardcoded link).
 * The normal path is server-side resolution in each service's response mapping - see FileStorageService#resolve.
 */
@RestController
@ConditionalOnProperty(prefix = "app.storage", name = "type", havingValue = "s3")
public class S3UploadRedirectController {

    private final FileStorageService fileStorageService;

    public S3UploadRedirectController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Void> redirectToS3(@PathVariable String filename) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(fileStorageService.resolve("/uploads/" + filename)))
                .build();
    }
}
