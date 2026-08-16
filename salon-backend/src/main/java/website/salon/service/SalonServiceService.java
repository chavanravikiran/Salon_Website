package website.salon.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import website.salon.common.PagedResponse;
import website.salon.common.PageableFactory;
import website.salon.dto.ServiceDtos.ServiceFilterRequest;
import website.salon.dto.ServiceDtos.ServiceRequest;
import website.salon.dto.ServiceDtos.ServiceResponse;
import website.salon.entity.SalonService;
import website.salon.exception.ResourceNotFoundException;
import website.salon.repository.SalonServiceRepository;
import website.salon.specification.ServiceSpecifications;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class SalonServiceService {

    private static final Set<String> SORTABLE_FIELDS = Set.of("id", "name", "price", "durationMinutes", "category");

    private final SalonServiceRepository repository;
    private final FileStorageService fileStorageService;

    public SalonServiceService(SalonServiceRepository repository, FileStorageService fileStorageService) {
        this.repository = repository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional(readOnly = true)
    public PagedResponse<ServiceResponse> search(ServiceFilterRequest filter, int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageableFactory.build(page, size, sortBy, sortDir, SORTABLE_FIELDS, "id");
        return PagedResponse.from(repository.findAll(ServiceSpecifications.build(filter), pageable), this::toResponse);
    }

    @Transactional(readOnly = true)
    public List<ServiceResponse> findAllActive() {
        return repository.findByActiveTrue().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ServiceResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ServiceResponse findById(Long id) {
        return toResponse(getEntity(id));
    }

    public ServiceResponse create(ServiceRequest request) {
        SalonService entity = new SalonService();
        applyRequest(entity, request);
        return toResponse(repository.save(entity));
    }

    public ServiceResponse update(Long id, ServiceRequest request) {
        SalonService entity = getEntity(id);
        applyRequest(entity, request);
        return toResponse(repository.save(entity));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Service not found: " + id);
        }
        repository.deleteById(id);
    }

    private SalonService getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found: " + id));
    }

    private void applyRequest(SalonService entity, ServiceRequest request) {
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setPrice(request.price());
        entity.setDurationMinutes(request.durationMinutes());
        entity.setCategory(request.category());
        entity.setImageUrl(request.imageUrl());
        entity.setActive(request.active() != null ? request.active() : true);
    }

    private ServiceResponse toResponse(SalonService entity) {
        return new ServiceResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getDurationMinutes(),
                entity.getCategory(),
                fileStorageService.resolve(entity.getImageUrl()),
                entity.getActive()
        );
    }
}
