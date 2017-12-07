package net.mapthinks.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.mapthinks.domain.Maintenance;

import net.mapthinks.repository.MaintenanceRepository;
import net.mapthinks.repository.search.MaintenanceSearchRepository;
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
 * REST controller for managing Maintenance.
 */
@RestController
@RequestMapping("/api")
public class MaintenanceResource {

    private final Logger log = LoggerFactory.getLogger(MaintenanceResource.class);

    private static final String ENTITY_NAME = "maintenance";

    private final MaintenanceRepository maintenanceRepository;

    private final MaintenanceSearchRepository maintenanceSearchRepository;

    public MaintenanceResource(MaintenanceRepository maintenanceRepository, MaintenanceSearchRepository maintenanceSearchRepository) {
        this.maintenanceRepository = maintenanceRepository;
        this.maintenanceSearchRepository = maintenanceSearchRepository;
    }

    /**
     * POST  /maintenances : Create a new maintenance.
     *
     * @param maintenance the maintenance to create
     * @return the ResponseEntity with status 201 (Created) and with body the new maintenance, or with status 400 (Bad Request) if the maintenance has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/maintenances")
    @Timed
    public ResponseEntity<Maintenance> createMaintenance(@Valid @RequestBody Maintenance maintenance) throws URISyntaxException {
        log.debug("REST request to save Maintenance : {}", maintenance);
        if (maintenance.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new maintenance cannot already have an ID")).body(null);
        }
        Maintenance result = maintenanceRepository.save(maintenance);
        maintenanceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/maintenances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /maintenances : Updates an existing maintenance.
     *
     * @param maintenance the maintenance to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated maintenance,
     * or with status 400 (Bad Request) if the maintenance is not valid,
     * or with status 500 (Internal Server Error) if the maintenance couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/maintenances")
    @Timed
    public ResponseEntity<Maintenance> updateMaintenance(@Valid @RequestBody Maintenance maintenance) throws URISyntaxException {
        log.debug("REST request to update Maintenance : {}", maintenance);
        if (maintenance.getId() == null) {
            return createMaintenance(maintenance);
        }
        Maintenance result = maintenanceRepository.save(maintenance);
        maintenanceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, maintenance.getId().toString()))
            .body(result);
    }

    /**
     * GET  /maintenances : get all the maintenances.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of maintenances in body
     */
    @GetMapping("/maintenances")
    @Timed
    public ResponseEntity<List<Maintenance>> getAllMaintenances(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Maintenances");
        Page<Maintenance> page = maintenanceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/maintenances");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /maintenances/:id : get the "id" maintenance.
     *
     * @param id the id of the maintenance to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the maintenance, or with status 404 (Not Found)
     */
    @GetMapping("/maintenances/{id}")
    @Timed
    public ResponseEntity<Maintenance> getMaintenance(@PathVariable Long id) {
        log.debug("REST request to get Maintenance : {}", id);
        Maintenance maintenance = maintenanceRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(maintenance));
    }

    /**
     * DELETE  /maintenances/:id : delete the "id" maintenance.
     *
     * @param id the id of the maintenance to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/maintenances/{id}")
    @Timed
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Long id) {
        log.debug("REST request to delete Maintenance : {}", id);
        maintenanceRepository.delete(id);
        maintenanceSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/maintenances?query=:query : search for the maintenance corresponding
     * to the query.
     *
     * @param query the query of the maintenance search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/maintenances")
    @Timed
    public ResponseEntity<List<Maintenance>> searchMaintenances(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Maintenances for query {}", query);
        Page<Maintenance> page = maintenanceSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/maintenances");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
