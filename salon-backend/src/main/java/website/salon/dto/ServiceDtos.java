package website.salon.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ServiceDtos {

    public record ServiceRequest(
            @NotBlank String name,
            String description,
            @NotNull @DecimalMin("0.0") BigDecimal price,
            @NotNull @Min(1) Integer durationMinutes,
            String category,
            String imageUrl,
            Boolean active
    ) {}

    public record ServiceResponse(
            Long id,
            String name,
            String description,
            BigDecimal price,
            Integer durationMinutes,
            String category,
            String imageUrl,
            Boolean active
    ) {}

    public record ServiceFilterRequest(
            String name,
            String category,
            Boolean active,
            BigDecimal minPrice,
            BigDecimal maxPrice
    ) {}
}
