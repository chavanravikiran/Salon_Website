package website.salon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import website.salon.entity.Testimonial;

import java.util.List;

public interface TestimonialRepository extends JpaRepository<Testimonial, Long>, JpaSpecificationExecutor<Testimonial> {
    List<Testimonial> findByApprovedTrue();
}
