package website.salon.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@ConditionalOnProperty(prefix = "app.storage", name = "type", havingValue = "s3")
public class S3UploadRedirectController {

    private final String bucket;
    private final String region;
    private final String publicBaseUrl;

    public S3UploadRedirectController(@Value("${app.aws.s3.bucket}") String bucket,
                                       @Value("${app.aws.s3.region}") String region,
                                       @Value("${app.aws.s3.public-base-url:}") String publicBaseUrl) {
        this.bucket = bucket;
        this.region = region;
        this.publicBaseUrl = publicBaseUrl;
    }

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Void> redirectToS3(@PathVariable String filename) {
        String base = publicBaseUrl.isBlank()
                ? "https://" + bucket + ".s3." + region + ".amazonaws.com"
                : publicBaseUrl.replaceAll("/$", "");
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(base + "/uploads/" + filename))
                .build();
    }
}
