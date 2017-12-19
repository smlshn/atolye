package net.mapthinks.web.rest;

import net.mapthinks.domain.City;
import net.mapthinks.repository.CityRepository;
import net.mapthinks.repository.search.CitySearchRepository;
import net.mapthinks.web.rest.common.AbstractPageableResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing City.
 */
@RestController
@RequestMapping("/api/cities")
public class CityResource extends AbstractPageableResource<City,CityRepository,CitySearchRepository> {

    private final Logger log = LoggerFactory.getLogger(CityResource.class);

    private static final String ENTITY_NAME = "city";

    public CityResource(CityRepository cityRepository, CitySearchRepository citySearchRepository) {
        super(cityRepository,citySearchRepository);
    }

}
