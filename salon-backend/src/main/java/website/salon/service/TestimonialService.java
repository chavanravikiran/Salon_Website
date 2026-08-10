package website.salon.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import website.salon.common.PagedResponse;
import website.salon.common.PageableFactory;
import website.salon.dto.TestimonialDtos.TestimonialFilterRequest;
import website.salon.dto.TestimonialDtos.TestimonialRequest;
import website.salon.dto.TestimonialDtos.TestimonialResponse;
import website.salon.entity.Testimonial;
import website.salon.exception.ResourceNotFoundException;
import website.salon.repository.TestimonialRepository;
import website.salon.specification.TestimonialSpecifications;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class TestimonialService {

    private static final Set<String> SORTABLE_FIELDS = Set.of("id", "customerName", "rating", "createdAt");

    private final TestimonialRepository repository;

    public TestimonialService(TestimonialRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public PagedResponse<TestimonialResponse> search(TestimonialFilterRequest filter, int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageableFactory.build(page, size, sortBy, sortDir, SORTABLE_FIELDS, "createdAt");
        return PagedResponse.from(repository.findAll(TestimonialSpecifications.build(filter), pageable), this::toResponse);
    }

    @Transactional(readOnly = true)
    public List<TestimonialResponse> findApproved() {
        return repository.findByApprovedTrue().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<TestimonialResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public TestimonialResponse submit(TestimonialRequest request) {
        Testimonial entity = new Testimonial();
        entity.setCustomerName(request.customerName());
        entity.setRating(request.rating());
        entity.setReviewText(request.reviewText());
        entity.setApproved(false);
        return toResponse(repository.save(entity));
    }

    public TestimonialResponse setApproved(Long id, boolean approved) {
        Testimonial entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Testimonial not found: " + id));
        entity.setApproved(approved);
        return toResponse(repository.save(entity));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Testimonial not found: " + id);
        }
        repository.deleteById(id);
    }

    private TestimonialResponse toResponse(Testimonial entity) {
        return new TestimonialResponse(
                entity.getId(),
                entity.getCustomerName(),
                entity.getRating(),
                entity.getReviewText(),
                entity.getCreatedAt(),
                entity.getApproved()
        );
    }
}
