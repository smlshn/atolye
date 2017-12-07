package net.mapthinks.repository;

import net.mapthinks.domain.SystemUser;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SystemUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {

}
