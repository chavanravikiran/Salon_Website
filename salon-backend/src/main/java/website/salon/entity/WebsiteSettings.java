package website.salon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "website_settings")
@Getter
@Setter
@NoArgsConstructor
public class WebsiteSettings {

    @Id
    private Long id;

    @NotBlank
    @Column(length = 150)
    private String websiteName;

    private String logoImage;

    private String faviconImage;

    private String heroMediaUrl;

    @Enumerated(EnumType.STRING)
    private HeroMediaType heroMediaType;

    @Column(length = 30)
    private String phone;

    @Column(length = 150)
    private String email;

    @Column(length = 300)
    private String address;

    @Column(length = 2000)
    private String description;

    @Column(length = 300)
    private String businessHours;

    @Column(length = 300)
    private String facebookUrl;

    @Column(length = 300)
    private String instagramUrl;

    @Column(length = 300)
    private String twitterUrl;

    @Column(length = 300)
    private String youtubeUrl;

    @Column(length = 2000)
    private String googleMapEmbedUrl;

    private Instant updatedAt = Instant.now();
}
