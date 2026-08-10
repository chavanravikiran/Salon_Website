package website.salon.dto;

import jakarta.validation.constraints.NotBlank;

public class StaffDtos {

    public record StaffRequest(
            @NotBlank String name,
            String specialty,
            String bio,
            String photoUrl,
            Boolean active
    ) {}

    public record StaffResponse(
            Long id,
            String name,
            String specialty,
            String bio,
            String photoUrl,
            Boolean active
    ) {}

    public record StaffFilterRequest(
            String name,
            String specialty,
            Boolean active
    ) {}
}
