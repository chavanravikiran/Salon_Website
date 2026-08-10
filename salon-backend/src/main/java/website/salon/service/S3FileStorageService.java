package website.salon.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
@ConditionalOnProperty(prefix = "app.storage", name = "type", havingValue = "s3")
public class S3FileStorageService implements FileStorageService {

    private final S3Client s3Client;
    private final String bucket;
    private final String region;
    private final String publicBaseUrl;

    public S3FileStorageService(S3Client s3Client,
                                 @Value("${app.aws.s3.bucket}") String bucket,
                                 @Value("${app.aws.s3.region}") String region,
                                 @Value("${app.aws.s3.public-base-url:}") String publicBaseUrl) {
        this.s3Client = s3Client;
        this.bucket = bucket;
        this.region = region;
        this.publicBaseUrl = publicBaseUrl;
    }

    @Override
    public String store(MultipartFile file) {
        String filename = UploadFileNaming.generateFilename(file);
        String key = "uploads/" + filename;

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload file to S3", e);
        }

        if (!publicBaseUrl.isBlank()) {
            return publicBaseUrl.replaceAll("/$", "") + "/" + key;
        }
        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;
    }

    @Override
    public void delete(String url) {
        if (!StringUtils.hasText(url)) {
            return;
        }
        int uploadsIndex = url.lastIndexOf("uploads/");
        if (uploadsIndex < 0) {
            return;
        }
        String key = url.substring(uploadsIndex);
        s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
    }
}
