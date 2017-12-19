package net.mapthinks.web.rest;

import net.mapthinks.domain.SystemUser;
import net.mapthinks.repository.SystemUserRepository;
import net.mapthinks.repository.search.SystemUserSearchRepository;
import net.mapthinks.web.rest.common.AbstractPageableResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing SystemUser.
 */
@RestController
@RequestMapping("/api/system-users")
public class SystemUserResource extends AbstractPageableResource<SystemUser,SystemUserRepository,SystemUserSearchRepository> {

    private final Logger log = LoggerFactory.getLogger(SystemUserResource.class);

    private static final String ENTITY_NAME = "systemUser";

    public SystemUserResource(SystemUserRepository systemUserRepository, SystemUserSearchRepository systemUserSearchRepository) {
        super(systemUserRepository,systemUserSearchRepository);
    }

}
