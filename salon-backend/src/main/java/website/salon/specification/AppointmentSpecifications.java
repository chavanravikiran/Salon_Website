package website.salon.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import website.salon.dto.AppointmentDtos.AppointmentFilterRequest;
import website.salon.entity.Appointment;

import java.util.ArrayList;
import java.util.List;

public final class AppointmentSpecifications {

    private AppointmentSpecifications() {
    }

    public static Specification<Appointment> build(AppointmentFilterRequest filter) {
        return (root, query, cb) -> {
            if (filter == null) {
                return cb.conjunction();
            }
            List<Predicate> predicates = new ArrayList<>();
            if (filter.status() != null) {
                predicates.add(cb.equal(root.get("status"), filter.status()));
            }
            if (filter.serviceId() != null) {
                predicates.add(cb.equal(root.get("service").get("id"), filter.serviceId()));
            }
            if (filter.staffId() != null) {
                predicates.add(cb.equal(root.get("staff").get("id"), filter.staffId()));
            }
            if (filter.from() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("appointmentDate"), filter.from()));
            }
            if (filter.to() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("appointmentDate"), filter.to()));
            }
            if (filter.customer() != null && !filter.customer().isBlank()) {
                String pattern = "%" + filter.customer().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("customerName")), pattern),
                        cb.like(cb.lower(root.get("customerEmail")), pattern),
                        cb.like(cb.lower(root.get("customerPhone")), pattern)
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
