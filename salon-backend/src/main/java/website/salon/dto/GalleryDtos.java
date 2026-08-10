package website.salon.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.time.LocalDate;

public class GalleryDtos {

    public record GalleryImageRequest(
            String title,
            @NotBlank String imageUrl,
            String category
    ) {}

    public record GalleryImageResponse(
            Long id,
            String title,
            String imageUrl,
            String category,
            Instant uploadedAt
    ) {}

    public record GalleryFilterRequest(
            String title,
            String category,
            LocalDate from,
            LocalDate to
    ) {}
}
