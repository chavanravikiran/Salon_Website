package website.salon.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import website.salon.dto.TestimonialDtos.TestimonialFilterRequest;
import website.salon.entity.Testimonial;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public final class TestimonialSpecifications {

    private TestimonialSpecifications() {
    }

    public static Specification<Testimonial> build(TestimonialFilterRequest filter) {
        return (root, query, cb) -> {
            if (filter == null) {
                return cb.conjunction();
            }
            List<Predicate> predicates = new ArrayList<>();
            if (filter.approved() != null) {
                predicates.add(cb.equal(root.get("approved"), filter.approved()));
            }
            if (filter.minRating() != null) {
                predicates.add(cb.ge(root.get("rating"), filter.minRating()));
            }
            if (filter.customerName() != null && !filter.customerName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("customerName")), "%" + filter.customerName().toLowerCase() + "%"));
            }
            if (filter.from() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"),
                        filter.from().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            if (filter.to() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"),
                        filter.to().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
