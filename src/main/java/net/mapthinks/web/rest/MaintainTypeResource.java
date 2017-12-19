package net.mapthinks.web.rest;

import net.mapthinks.domain.MaintainType;
import net.mapthinks.repository.MaintainTypeRepository;
import net.mapthinks.repository.search.MaintainTypeSearchRepository;
import net.mapthinks.web.rest.common.AbstractPageableResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing MaintainType.
 */
@RestController
@RequestMapping("/api/maintain-types")
public class MaintainTypeResource extends AbstractPageableResource<MaintainType,MaintainTypeRepository,MaintainTypeSearchRepository> {

    private final Logger log = LoggerFactory.getLogger(MaintainTypeResource.class);

    private static final String ENTITY_NAME = "maintainType";

    public MaintainTypeResource(MaintainTypeRepository maintainTypeRepository, MaintainTypeSearchRepository maintainTypeSearchRepository) {
        super(maintainTypeRepository,maintainTypeSearchRepository);
    }

}
