package website.salon.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import website.salon.entity.AppointmentStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentDtos {

    public record AppointmentRequest(
            @NotBlank String customerName,
            @NotBlank @Email String customerEmail,
            @NotBlank String customerPhone,
            @NotNull Long serviceId,
            Long staffId,
            @NotNull LocalDate appointmentDate,
            @NotNull LocalTime appointmentTime,
            String notes
    ) {}

    public record AppointmentStatusUpdateRequest(
            @NotNull AppointmentStatus status
    ) {}

    public record AppointmentResponse(
            Long id,
            String customerName,
            String customerEmail,
            String customerPhone,
            Long serviceId,
            String serviceName,
            Long staffId,
            String staffName,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            AppointmentStatus status,
            String notes,
            Instant createdAt
    ) {}

    public record AppointmentFilterRequest(
            AppointmentStatus status,
            Long serviceId,
            Long staffId,
            LocalDate from,
            LocalDate to,
            String customer
    ) {}
}
