package net.mapthinks.web.rest;

import net.mapthinks.domain.CarBrand;
import net.mapthinks.repository.CarBrandRepository;
import net.mapthinks.repository.search.CarBrandSearchRepository;
import net.mapthinks.web.rest.common.AbstractPageableResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing CarBrand.
 */
@RestController
@RequestMapping("/api/car-brands")
public class CarBrandResource extends AbstractPageableResource<CarBrand,CarBrandRepository,CarBrandSearchRepository> {

    private final Logger log = LoggerFactory.getLogger(CarBrandResource.class);

    private static final String ENTITY_NAME = "carBrand";

    public CarBrandResource(CarBrandRepository carBrandRepository, CarBrandSearchRepository carBrandSearchRepository) {
        super(carBrandRepository,carBrandSearchRepository);
    }

}
