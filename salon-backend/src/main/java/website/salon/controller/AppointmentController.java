package website.salon.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import website.salon.common.PagedResponse;
import website.salon.dto.AppointmentDtos.AppointmentFilterRequest;
import website.salon.dto.AppointmentDtos.AppointmentRequest;
import website.salon.dto.AppointmentDtos.AppointmentResponse;
import website.salon.dto.AppointmentDtos.AppointmentStatusUpdateRequest;
import website.salon.service.AppointmentService;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @GetMapping
    public List<AppointmentResponse> getAll() {
        return service.findAll();
    }

    @GetMapping("/search")
    public PagedResponse<AppointmentResponse> search(@ModelAttribute AppointmentFilterRequest filter,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue = "appointmentDate") String sortBy,
                                                       @RequestParam(defaultValue = "desc") String sortDir) {
        return service.search(filter, page, size, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public AppointmentResponse getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> create(@Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PatchMapping("/{id}/status")
    public AppointmentResponse updateStatus(@PathVariable Long id, @Valid @RequestBody AppointmentStatusUpdateRequest request) {
        return service.updateStatus(id, request.status());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
