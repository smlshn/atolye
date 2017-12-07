package net.mapthinks.repository;

import net.mapthinks.domain.CarModel;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CarModel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarModelRepository extends JpaRepository<CarModel, Long> {

}
