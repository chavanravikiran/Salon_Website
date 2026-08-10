package website.salon.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import website.salon.dto.GalleryDtos.GalleryFilterRequest;
import website.salon.entity.GalleryImage;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public final class GallerySpecifications {

    private GallerySpecifications() {
    }

    public static Specification<GalleryImage> build(GalleryFilterRequest filter) {
        return (root, query, cb) -> {
            if (filter == null) {
                return cb.conjunction();
            }
            List<Predicate> predicates = new ArrayList<>();
            if (filter.title() != null && !filter.title().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + filter.title().toLowerCase() + "%"));
            }
            if (filter.category() != null && !filter.category().isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("category")), filter.category().toLowerCase()));
            }
            if (filter.from() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("uploadedAt"),
                        filter.from().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            if (filter.to() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("uploadedAt"),
                        filter.to().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
