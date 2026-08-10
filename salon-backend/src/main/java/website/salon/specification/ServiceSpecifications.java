package website.salon.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import website.salon.dto.ServiceDtos.ServiceFilterRequest;
import website.salon.entity.SalonService;

import java.util.ArrayList;
import java.util.List;

public final class ServiceSpecifications {

    private ServiceSpecifications() {
    }

    public static Specification<SalonService> build(ServiceFilterRequest filter) {
        return (root, query, cb) -> {
            if (filter == null) {
                return cb.conjunction();
            }
            List<Predicate> predicates = new ArrayList<>();
            if (filter.name() != null && !filter.name().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.name().toLowerCase() + "%"));
            }
            if (filter.category() != null && !filter.category().isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("category")), filter.category().toLowerCase()));
            }
            if (filter.active() != null) {
                predicates.add(cb.equal(root.get("active"), filter.active()));
            }
            if (filter.minPrice() != null) {
                predicates.add(cb.ge(root.get("price"), filter.minPrice()));
            }
            if (filter.maxPrice() != null) {
                predicates.add(cb.le(root.get("price"), filter.maxPrice()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
