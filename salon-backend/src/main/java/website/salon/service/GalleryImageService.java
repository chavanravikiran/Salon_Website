package website.salon.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import website.salon.common.PagedResponse;
import website.salon.common.PageableFactory;
import website.salon.dto.GalleryDtos.GalleryFilterRequest;
import website.salon.dto.GalleryDtos.GalleryImageRequest;
import website.salon.dto.GalleryDtos.GalleryImageResponse;
import website.salon.entity.GalleryImage;
import website.salon.exception.ResourceNotFoundException;
import website.salon.repository.GalleryImageRepository;
import website.salon.specification.GallerySpecifications;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class GalleryImageService {

    private static final Set<String> SORTABLE_FIELDS = Set.of("id", "title", "category", "uploadedAt");

    private final GalleryImageRepository repository;
    private final FileStorageService fileStorageService;

    public GalleryImageService(GalleryImageRepository repository, FileStorageService fileStorageService) {
        this.repository = repository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional(readOnly = true)
    public PagedResponse<GalleryImageResponse> search(GalleryFilterRequest filter, int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageableFactory.build(page, size, sortBy, sortDir, SORTABLE_FIELDS, "uploadedAt");
        return PagedResponse.from(repository.findAll(GallerySpecifications.build(filter), pageable), this::toResponse);
    }

    @Transactional(readOnly = true)
    public List<GalleryImageResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public GalleryImageResponse create(GalleryImageRequest request) {
        GalleryImage entity = new GalleryImage();
        entity.setTitle(request.title());
        entity.setImageUrl(request.imageUrl());
        entity.setCategory(request.category());
        return toResponse(repository.save(entity));
    }

    public GalleryImageResponse update(Long id, GalleryImageRequest request) {
        GalleryImage entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery image not found: " + id));
        entity.setTitle(request.title());
        entity.setImageUrl(request.imageUrl());
        entity.setCategory(request.category());
        return toResponse(repository.save(entity));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Gallery image not found: " + id);
        }
        repository.deleteById(id);
    }

    private GalleryImageResponse toResponse(GalleryImage entity) {
        return new GalleryImageResponse(
                entity.getId(),
                entity.getTitle(),
                fileStorageService.resolve(entity.getImageUrl()),
                entity.getCategory(),
                entity.getUploadedAt()
        );
    }
}
