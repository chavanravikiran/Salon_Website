package website.salon.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import website.salon.common.PagedResponse;
import website.salon.dto.StaffDtos.StaffFilterRequest;
import website.salon.dto.StaffDtos.StaffRequest;
import website.salon.dto.StaffDtos.StaffResponse;
import website.salon.service.StaffService;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService service;

    public StaffController(StaffService service) {
        this.service = service;
    }

    @GetMapping
    public List<StaffResponse> getAll(@RequestParam(required = false, defaultValue = "false") boolean all) {
        return all ? service.findAll() : service.findAllActive();
    }

    @GetMapping("/search")
    public PagedResponse<StaffResponse> search(@ModelAttribute StaffFilterRequest filter,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(defaultValue = "id") String sortBy,
                                                @RequestParam(defaultValue = "desc") String sortDir) {
        return service.search(filter, page, size, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public StaffResponse getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<StaffResponse> create(@Valid @RequestBody StaffRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    public StaffResponse update(@PathVariable Long id, @Valid @RequestBody StaffRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
