package net.mapthinks.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.mapthinks.domain.MaintainInstance;

import net.mapthinks.repository.MaintainInstanceRepository;
import net.mapthinks.repository.search.MaintainInstanceSearchRepository;
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
 * REST controller for managing MaintainInstance.
 */
@RestController
@RequestMapping("/api")
public class MaintainInstanceResource {

    private final Logger log = LoggerFactory.getLogger(MaintainInstanceResource.class);

    private static final String ENTITY_NAME = "maintainInstance";

    private final MaintainInstanceRepository maintainInstanceRepository;

    private final MaintainInstanceSearchRepository maintainInstanceSearchRepository;

    public MaintainInstanceResource(MaintainInstanceRepository maintainInstanceRepository, MaintainInstanceSearchRepository maintainInstanceSearchRepository) {
        this.maintainInstanceRepository = maintainInstanceRepository;
        this.maintainInstanceSearchRepository = maintainInstanceSearchRepository;
    }

    /**
     * POST  /maintain-instances : Create a new maintainInstance.
     *
     * @param maintainInstance the maintainInstance to create
     * @return the ResponseEntity with status 201 (Created) and with body the new maintainInstance, or with status 400 (Bad Request) if the maintainInstance has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/maintain-instances")
    @Timed
    public ResponseEntity<MaintainInstance> createMaintainInstance(@Valid @RequestBody MaintainInstance maintainInstance) throws URISyntaxException {
        log.debug("REST request to save MaintainInstance : {}", maintainInstance);
        if (maintainInstance.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new maintainInstance cannot already have an ID")).body(null);
        }
        MaintainInstance result = maintainInstanceRepository.save(maintainInstance);
        maintainInstanceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/maintain-instances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /maintain-instances : Updates an existing maintainInstance.
     *
     * @param maintainInstance the maintainInstance to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated maintainInstance,
     * or with status 400 (Bad Request) if the maintainInstance is not valid,
     * or with status 500 (Internal Server Error) if the maintainInstance couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/maintain-instances")
    @Timed
    public ResponseEntity<MaintainInstance> updateMaintainInstance(@Valid @RequestBody MaintainInstance maintainInstance) throws URISyntaxException {
        log.debug("REST request to update MaintainInstance : {}", maintainInstance);
        if (maintainInstance.getId() == null) {
            return createMaintainInstance(maintainInstance);
        }
        MaintainInstance result = maintainInstanceRepository.save(maintainInstance);
        maintainInstanceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, maintainInstance.getId().toString()))
            .body(result);
    }

    /**
     * GET  /maintain-instances : get all the maintainInstances.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of maintainInstances in body
     */
    @GetMapping("/maintain-instances")
    @Timed
    public List<MaintainInstance> getAllMaintainInstances() {
        log.debug("REST request to get all MaintainInstances");
        return maintainInstanceRepository.findAll();
        }

    /**
     * GET  /maintain-instances/:id : get the "id" maintainInstance.
     *
     * @param id the id of the maintainInstance to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the maintainInstance, or with status 404 (Not Found)
     */
    @GetMapping("/maintain-instances/{id}")
    @Timed
    public ResponseEntity<MaintainInstance> getMaintainInstance(@PathVariable Long id) {
        log.debug("REST request to get MaintainInstance : {}", id);
        MaintainInstance maintainInstance = maintainInstanceRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(maintainInstance));
    }

    /**
     * DELETE  /maintain-instances/:id : delete the "id" maintainInstance.
     *
     * @param id the id of the maintainInstance to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/maintain-instances/{id}")
    @Timed
    public ResponseEntity<Void> deleteMaintainInstance(@PathVariable Long id) {
        log.debug("REST request to delete MaintainInstance : {}", id);
        maintainInstanceRepository.delete(id);
        maintainInstanceSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/maintain-instances?query=:query : search for the maintainInstance corresponding
     * to the query.
     *
     * @param query the query of the maintainInstance search
     * @return the result of the search
     */
    @GetMapping("/_search/maintain-instances")
    @Timed
    public List<MaintainInstance> searchMaintainInstances(@RequestParam String query) {
        log.debug("REST request to search MaintainInstances for query {}", query);
        return StreamSupport
            .stream(maintainInstanceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
