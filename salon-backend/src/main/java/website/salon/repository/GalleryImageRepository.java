package website.salon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import website.salon.entity.GalleryImage;

public interface GalleryImageRepository extends JpaRepository<GalleryImage, Long>, JpaSpecificationExecutor<GalleryImage> {
}
