package net.mapthinks.web.rest;

import net.mapthinks.domain.Town;
import net.mapthinks.repository.TownRepository;
import net.mapthinks.repository.search.TownSearchRepository;
import net.mapthinks.web.rest.common.AbstractPageableResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Town.
 */
@RestController
@RequestMapping("/api/towns")
public class TownResource extends AbstractPageableResource<Town,TownRepository,TownSearchRepository> {

    private final Logger log = LoggerFactory.getLogger(TownResource.class);

    private static final String ENTITY_NAME = "town";

    public TownResource(TownRepository townRepository, TownSearchRepository townSearchRepository) {
        super(townRepository,townSearchRepository);
    }

}
