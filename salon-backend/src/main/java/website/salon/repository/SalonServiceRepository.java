package website.salon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import website.salon.entity.SalonService;

import java.util.List;

public interface SalonServiceRepository extends JpaRepository<SalonService, Long>, JpaSpecificationExecutor<SalonService> {
    List<SalonService> findByActiveTrue();
}
