package net.mapthinks.web.rest;

import net.mapthinks.domain.Car;
import net.mapthinks.repository.CarRepository;
import net.mapthinks.repository.search.CarSearchRepository;
import net.mapthinks.web.rest.common.AbstractPageableResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Car.
 */
@RestController
@RequestMapping("/api/cars")
public class CarResource extends AbstractPageableResource<Car,CarRepository,CarSearchRepository> {

    private final Logger log = LoggerFactory.getLogger(CarResource.class);

    private static final String ENTITY_NAME = "car";

    public CarResource(CarRepository carRepository, CarSearchRepository carSearchRepository) {
        super(carRepository,carSearchRepository);
    }

}
