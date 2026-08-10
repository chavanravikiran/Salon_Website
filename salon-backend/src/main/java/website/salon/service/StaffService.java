package website.salon.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import website.salon.common.PagedResponse;
import website.salon.common.PageableFactory;
import website.salon.dto.StaffDtos.StaffFilterRequest;
import website.salon.dto.StaffDtos.StaffRequest;
import website.salon.dto.StaffDtos.StaffResponse;
import website.salon.entity.Staff;
import website.salon.exception.ResourceNotFoundException;
import website.salon.repository.StaffRepository;
import website.salon.specification.StaffSpecifications;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class StaffService {

    private static final Set<String> SORTABLE_FIELDS = Set.of("id", "name", "specialty");

    private final StaffRepository repository;

    public StaffService(StaffRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public PagedResponse<StaffResponse> search(StaffFilterRequest filter, int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageableFactory.build(page, size, sortBy, sortDir, SORTABLE_FIELDS, "id");
        return PagedResponse.from(repository.findAll(StaffSpecifications.build(filter), pageable), this::toResponse);
    }

    @Transactional(readOnly = true)
    public List<StaffResponse> findAllActive() {
        return repository.findByActiveTrue().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<StaffResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public StaffResponse findById(Long id) {
        return toResponse(getEntity(id));
    }

    public StaffResponse create(StaffRequest request) {
        Staff entity = new Staff();
        applyRequest(entity, request);
        return toResponse(repository.save(entity));
    }

    public StaffResponse update(Long id, StaffRequest request) {
        Staff entity = getEntity(id);
        applyRequest(entity, request);
        return toResponse(repository.save(entity));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Staff not found: " + id);
        }
        repository.deleteById(id);
    }

    Staff getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found: " + id));
    }

    private void applyRequest(Staff entity, StaffRequest request) {
        entity.setName(request.name());
        entity.setSpecialty(request.specialty());
        entity.setBio(request.bio());
        entity.setPhotoUrl(request.photoUrl());
        entity.setActive(request.active() != null ? request.active() : true);
    }

    private StaffResponse toResponse(Staff entity) {
        return new StaffResponse(
                entity.getId(),
                entity.getName(),
                entity.getSpecialty(),
                entity.getBio(),
                entity.getPhotoUrl(),
                entity.getActive()
        );
    }
}
