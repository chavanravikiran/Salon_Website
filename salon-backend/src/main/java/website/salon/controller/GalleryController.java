package website.salon.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import website.salon.common.PagedResponse;
import website.salon.dto.GalleryDtos.GalleryFilterRequest;
import website.salon.dto.GalleryDtos.GalleryImageRequest;
import website.salon.dto.GalleryDtos.GalleryImageResponse;
import website.salon.service.GalleryImageService;

import java.util.List;

@RestController
@RequestMapping("/api/gallery")
public class GalleryController {

    private final GalleryImageService service;

    public GalleryController(GalleryImageService service) {
        this.service = service;
    }

    @GetMapping
    public List<GalleryImageResponse> getAll() {
        return service.findAll();
    }

    @GetMapping("/search")
    public PagedResponse<GalleryImageResponse> search(@ModelAttribute GalleryFilterRequest filter,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size,
                                                        @RequestParam(defaultValue = "uploadedAt") String sortBy,
                                                        @RequestParam(defaultValue = "desc") String sortDir) {
        return service.search(filter, page, size, sortBy, sortDir);
    }

    @PostMapping
    public ResponseEntity<GalleryImageResponse> create(@Valid @RequestBody GalleryImageRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    public GalleryImageResponse update(@PathVariable Long id, @Valid @RequestBody GalleryImageRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
