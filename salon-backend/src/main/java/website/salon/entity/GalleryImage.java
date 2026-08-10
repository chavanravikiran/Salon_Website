package website.salon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "gallery_images")
@Getter
@Setter
@NoArgsConstructor
public class GalleryImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 150)
    private String title;

    @NotBlank
    private String imageUrl;

    @Size(max = 100)
    private String category;

    private Instant uploadedAt = Instant.now();
}
