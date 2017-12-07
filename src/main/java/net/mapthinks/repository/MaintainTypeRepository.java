package net.mapthinks.repository;

import net.mapthinks.domain.MaintainType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MaintainType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaintainTypeRepository extends JpaRepository<MaintainType, Long> {

}
