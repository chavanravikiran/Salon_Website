package website.salon.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@ConditionalOnProperty(prefix = "app.storage", name = "type", havingValue = "s3")
public class S3Config {

    @Bean
    public S3Client s3Client(@Value("${app.aws.s3.region}") String region) {
        AwsCredentialsProvider credentialsProvider = DefaultCredentialsProvider.create();

        // Fail fast at startup with a clear message instead of only surfacing on the first upload.
        // Hosts without EC2/ECS-style instance roles (e.g. Render, Railway) need AWS_ACCESS_KEY_ID
        // and AWS_SECRET_ACCESS_KEY set explicitly - there is no IMDS/container role to fall back on.
        try {
            credentialsProvider.resolveCredentials();
        } catch (SdkClientException e) {
            throw new IllegalStateException(
                    "app.storage.type=s3 but no AWS credentials are available. Set AWS_ACCESS_KEY_ID and "
                            + "AWS_SECRET_ACCESS_KEY as environment variables on this host (required on platforms "
                            + "like Render/Railway that don't provide an EC2/ECS instance role).", e);
        }

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider)
                .build();
    }
}
