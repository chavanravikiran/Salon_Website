package website.salon.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import website.salon.common.PagedResponse;
import website.salon.dto.ServiceDtos.ServiceFilterRequest;
import website.salon.dto.ServiceDtos.ServiceRequest;
import website.salon.dto.ServiceDtos.ServiceResponse;
import website.salon.service.SalonServiceService;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final SalonServiceService service;

    public ServiceController(SalonServiceService service) {
        this.service = service;
    }

    @GetMapping
    public List<ServiceResponse> getAll(@RequestParam(required = false, defaultValue = "false") boolean all) {
        return all ? service.findAll() : service.findAllActive();
    }

    @GetMapping("/search")
    public PagedResponse<ServiceResponse> search(@ModelAttribute ServiceFilterRequest filter,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                  @RequestParam(defaultValue = "desc") String sortDir) {
        return service.search(filter, page, size, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public ServiceResponse getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<ServiceResponse> create(@Valid @RequestBody ServiceRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    public ServiceResponse update(@PathVariable Long id, @Valid @RequestBody ServiceRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
