package net.mapthinks.repository;

import net.mapthinks.domain.CarBrand;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CarBrand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarBrandRepository extends JpaRepository<CarBrand, Long> {

}
