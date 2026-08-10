package website.salon.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import website.salon.common.PagedResponse;
import website.salon.dto.TestimonialDtos.TestimonialFilterRequest;
import website.salon.dto.TestimonialDtos.TestimonialRequest;
import website.salon.dto.TestimonialDtos.TestimonialResponse;
import website.salon.service.TestimonialService;

import java.util.List;

@RestController
@RequestMapping("/api/testimonials")
public class TestimonialController {

    private final TestimonialService service;

    public TestimonialController(TestimonialService service) {
        this.service = service;
    }

    @GetMapping("/public")
    public List<TestimonialResponse> getApproved() {
        return service.findApproved();
    }

    @GetMapping("/search")
    public PagedResponse<TestimonialResponse> search(@ModelAttribute TestimonialFilterRequest filter,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue = "createdAt") String sortBy,
                                                       @RequestParam(defaultValue = "desc") String sortDir) {
        return service.search(filter, page, size, sortBy, sortDir);
    }

    @GetMapping
    public List<TestimonialResponse> getAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<TestimonialResponse> submit(@Valid @RequestBody TestimonialRequest request) {
        return ResponseEntity.ok(service.submit(request));
    }

    @PatchMapping("/{id}/approve")
    public TestimonialResponse approve(@PathVariable Long id, @RequestParam(defaultValue = "true") boolean approved) {
        return service.setApproved(id, approved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
