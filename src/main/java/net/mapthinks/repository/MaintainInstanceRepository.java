package net.mapthinks.repository;

import net.mapthinks.domain.MaintainInstance;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MaintainInstance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaintainInstanceRepository extends JpaRepository<MaintainInstance, Long> {

}
