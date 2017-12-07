package net.mapthinks.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.mapthinks.domain.CarBrand;

import net.mapthinks.repository.CarBrandRepository;
import net.mapthinks.repository.search.CarBrandSearchRepository;
import net.mapthinks.web.rest.util.HeaderUtil;
import net.mapthinks.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
 * REST controller for managing CarBrand.
 */
@RestController
@RequestMapping("/api")
public class CarBrandResource {

    private final Logger log = LoggerFactory.getLogger(CarBrandResource.class);

    private static final String ENTITY_NAME = "carBrand";

    private final CarBrandRepository carBrandRepository;

    private final CarBrandSearchRepository carBrandSearchRepository;

    public CarBrandResource(CarBrandRepository carBrandRepository, CarBrandSearchRepository carBrandSearchRepository) {
        this.carBrandRepository = carBrandRepository;
        this.carBrandSearchRepository = carBrandSearchRepository;
    }

    /**
     * POST  /car-brands : Create a new carBrand.
     *
     * @param carBrand the carBrand to create
     * @return the ResponseEntity with status 201 (Created) and with body the new carBrand, or with status 400 (Bad Request) if the carBrand has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/car-brands")
    @Timed
    public ResponseEntity<CarBrand> createCarBrand(@Valid @RequestBody CarBrand carBrand) throws URISyntaxException {
        log.debug("REST request to save CarBrand : {}", carBrand);
        if (carBrand.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new carBrand cannot already have an ID")).body(null);
        }
        CarBrand result = carBrandRepository.save(carBrand);
        carBrandSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/car-brands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /car-brands : Updates an existing carBrand.
     *
     * @param carBrand the carBrand to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated carBrand,
     * or with status 400 (Bad Request) if the carBrand is not valid,
     * or with status 500 (Internal Server Error) if the carBrand couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/car-brands")
    @Timed
    public ResponseEntity<CarBrand> updateCarBrand(@Valid @RequestBody CarBrand carBrand) throws URISyntaxException {
        log.debug("REST request to update CarBrand : {}", carBrand);
        if (carBrand.getId() == null) {
            return createCarBrand(carBrand);
        }
        CarBrand result = carBrandRepository.save(carBrand);
        carBrandSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, carBrand.getId().toString()))
            .body(result);
    }

    /**
     * GET  /car-brands : get all the carBrands.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of carBrands in body
     */
    @GetMapping("/car-brands")
    @Timed
    public ResponseEntity<List<CarBrand>> getAllCarBrands(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CarBrands");
        Page<CarBrand> page = carBrandRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/car-brands");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /car-brands/:id : get the "id" carBrand.
     *
     * @param id the id of the carBrand to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the carBrand, or with status 404 (Not Found)
     */
    @GetMapping("/car-brands/{id}")
    @Timed
    public ResponseEntity<CarBrand> getCarBrand(@PathVariable Long id) {
        log.debug("REST request to get CarBrand : {}", id);
        CarBrand carBrand = carBrandRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(carBrand));
    }

    /**
     * DELETE  /car-brands/:id : delete the "id" carBrand.
     *
     * @param id the id of the carBrand to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/car-brands/{id}")
    @Timed
    public ResponseEntity<Void> deleteCarBrand(@PathVariable Long id) {
        log.debug("REST request to delete CarBrand : {}", id);
        carBrandRepository.delete(id);
        carBrandSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/car-brands?query=:query : search for the carBrand corresponding
     * to the query.
     *
     * @param query the query of the carBrand search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/car-brands")
    @Timed
    public ResponseEntity<List<CarBrand>> searchCarBrands(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of CarBrands for query {}", query);
        Page<CarBrand> page = carBrandSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/car-brands");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
