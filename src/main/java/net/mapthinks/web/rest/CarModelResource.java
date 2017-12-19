package net.mapthinks.web.rest;

import net.mapthinks.domain.CarModel;
import net.mapthinks.repository.CarModelRepository;
import net.mapthinks.repository.search.CarModelSearchRepository;
import net.mapthinks.web.rest.common.AbstractPageableResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing CarModel.
 */
@RestController
@RequestMapping("/api/car-models")
public class CarModelResource  extends AbstractPageableResource<CarModel,CarModelRepository,CarModelSearchRepository> {

    private final Logger log = LoggerFactory.getLogger(CarModelResource.class);

    private static final String ENTITY_NAME = "carModel";

    public CarModelResource(CarModelRepository carModelRepository, CarModelSearchRepository carModelSearchRepository) {
        super(carModelRepository,carModelSearchRepository);
    }

}
