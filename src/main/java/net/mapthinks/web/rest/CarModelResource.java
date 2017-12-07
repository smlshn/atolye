package net.mapthinks.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.mapthinks.domain.CarModel;

import net.mapthinks.repository.CarModelRepository;
import net.mapthinks.repository.search.CarModelSearchRepository;
import net.mapthinks.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CarModel.
 */
@RestController
@RequestMapping("/api")
public class CarModelResource {

    private final Logger log = LoggerFactory.getLogger(CarModelResource.class);

    private static final String ENTITY_NAME = "carModel";

    private final CarModelRepository carModelRepository;

    private final CarModelSearchRepository carModelSearchRepository;

    public CarModelResource(CarModelRepository carModelRepository, CarModelSearchRepository carModelSearchRepository) {
        this.carModelRepository = carModelRepository;
        this.carModelSearchRepository = carModelSearchRepository;
    }

    /**
     * POST  /car-models : Create a new carModel.
     *
     * @param carModel the carModel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new carModel, or with status 400 (Bad Request) if the carModel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/car-models")
    @Timed
    public ResponseEntity<CarModel> createCarModel(@Valid @RequestBody CarModel carModel) throws URISyntaxException {
        log.debug("REST request to save CarModel : {}", carModel);
        if (carModel.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new carModel cannot already have an ID")).body(null);
        }
        CarModel result = carModelRepository.save(carModel);
        carModelSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/car-models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /car-models : Updates an existing carModel.
     *
     * @param carModel the carModel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated carModel,
     * or with status 400 (Bad Request) if the carModel is not valid,
     * or with status 500 (Internal Server Error) if the carModel couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/car-models")
    @Timed
    public ResponseEntity<CarModel> updateCarModel(@Valid @RequestBody CarModel carModel) throws URISyntaxException {
        log.debug("REST request to update CarModel : {}", carModel);
        if (carModel.getId() == null) {
            return createCarModel(carModel);
        }
        CarModel result = carModelRepository.save(carModel);
        carModelSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, carModel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /car-models : get all the carModels.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of carModels in body
     */
    @GetMapping("/car-models")
    @Timed
    public List<CarModel> getAllCarModels() {
        log.debug("REST request to get all CarModels");
        return carModelRepository.findAll();
        }

    /**
     * GET  /car-models/:id : get the "id" carModel.
     *
     * @param id the id of the carModel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the carModel, or with status 404 (Not Found)
     */
    @GetMapping("/car-models/{id}")
    @Timed
    public ResponseEntity<CarModel> getCarModel(@PathVariable Long id) {
        log.debug("REST request to get CarModel : {}", id);
        CarModel carModel = carModelRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(carModel));
    }

    /**
     * DELETE  /car-models/:id : delete the "id" carModel.
     *
     * @param id the id of the carModel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/car-models/{id}")
    @Timed
    public ResponseEntity<Void> deleteCarModel(@PathVariable Long id) {
        log.debug("REST request to delete CarModel : {}", id);
        carModelRepository.delete(id);
        carModelSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/car-models?query=:query : search for the carModel corresponding
     * to the query.
     *
     * @param query the query of the carModel search
     * @return the result of the search
     */
    @GetMapping("/_search/car-models")
    @Timed
    public List<CarModel> searchCarModels(@RequestParam String query) {
        log.debug("REST request to search CarModels for query {}", query);
        return StreamSupport
            .stream(carModelSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
