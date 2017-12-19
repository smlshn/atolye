package net.mapthinks.web.rest;

import net.mapthinks.domain.MaintainInstance;
import net.mapthinks.repository.MaintainInstanceRepository;
import net.mapthinks.repository.search.MaintainInstanceSearchRepository;
import net.mapthinks.web.rest.common.AbstractPageableResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing MaintainInstance.
 */
@RestController
@RequestMapping("/api/maintain-instances")
public class MaintainInstanceResource extends AbstractPageableResource<MaintainInstance,MaintainInstanceRepository,MaintainInstanceSearchRepository> {

    private final Logger log = LoggerFactory.getLogger(MaintainInstanceResource.class);

    private static final String ENTITY_NAME = "maintainInstance";

    public MaintainInstanceResource(MaintainInstanceRepository maintainInstanceRepository, MaintainInstanceSearchRepository maintainInstanceSearchRepository) {
        super(maintainInstanceRepository,maintainInstanceSearchRepository);
    }

}
