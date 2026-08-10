package website.salon.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDate;

public class TestimonialDtos {

    public record TestimonialRequest(
            @NotBlank String customerName,
            @NotNull @Min(1) @Max(5) Integer rating,
            @NotBlank String reviewText
    ) {}

    public record TestimonialResponse(
            Long id,
            String customerName,
            Integer rating,
            String reviewText,
            Instant createdAt,
            Boolean approved
    ) {}

    public record TestimonialFilterRequest(
            Boolean approved,
            Integer minRating,
            String customerName,
            LocalDate from,
            LocalDate to
    ) {}
}
