package website.salon.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import website.salon.dto.StaffDtos.StaffFilterRequest;
import website.salon.entity.Staff;

import java.util.ArrayList;
import java.util.List;

public final class StaffSpecifications {

    private StaffSpecifications() {
    }

    public static Specification<Staff> build(StaffFilterRequest filter) {
        return (root, query, cb) -> {
            if (filter == null) {
                return cb.conjunction();
            }
            List<Predicate> predicates = new ArrayList<>();
            if (filter.name() != null && !filter.name().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.name().toLowerCase() + "%"));
            }
            if (filter.specialty() != null && !filter.specialty().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("specialty")), "%" + filter.specialty().toLowerCase() + "%"));
            }
            if (filter.active() != null) {
                predicates.add(cb.equal(root.get("active"), filter.active()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
