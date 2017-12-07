package net.mapthinks.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.mapthinks.domain.MaintainType;

import net.mapthinks.repository.MaintainTypeRepository;
import net.mapthinks.repository.search.MaintainTypeSearchRepository;
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
 * REST controller for managing MaintainType.
 */
@RestController
@RequestMapping("/api")
public class MaintainTypeResource {

    private final Logger log = LoggerFactory.getLogger(MaintainTypeResource.class);

    private static final String ENTITY_NAME = "maintainType";

    private final MaintainTypeRepository maintainTypeRepository;

    private final MaintainTypeSearchRepository maintainTypeSearchRepository;

    public MaintainTypeResource(MaintainTypeRepository maintainTypeRepository, MaintainTypeSearchRepository maintainTypeSearchRepository) {
        this.maintainTypeRepository = maintainTypeRepository;
        this.maintainTypeSearchRepository = maintainTypeSearchRepository;
    }

    /**
     * POST  /maintain-types : Create a new maintainType.
     *
     * @param maintainType the maintainType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new maintainType, or with status 400 (Bad Request) if the maintainType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/maintain-types")
    @Timed
    public ResponseEntity<MaintainType> createMaintainType(@Valid @RequestBody MaintainType maintainType) throws URISyntaxException {
        log.debug("REST request to save MaintainType : {}", maintainType);
        if (maintainType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new maintainType cannot already have an ID")).body(null);
        }
        MaintainType result = maintainTypeRepository.save(maintainType);
        maintainTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/maintain-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /maintain-types : Updates an existing maintainType.
     *
     * @param maintainType the maintainType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated maintainType,
     * or with status 400 (Bad Request) if the maintainType is not valid,
     * or with status 500 (Internal Server Error) if the maintainType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/maintain-types")
    @Timed
    public ResponseEntity<MaintainType> updateMaintainType(@Valid @RequestBody MaintainType maintainType) throws URISyntaxException {
        log.debug("REST request to update MaintainType : {}", maintainType);
        if (maintainType.getId() == null) {
            return createMaintainType(maintainType);
        }
        MaintainType result = maintainTypeRepository.save(maintainType);
        maintainTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, maintainType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /maintain-types : get all the maintainTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of maintainTypes in body
     */
    @GetMapping("/maintain-types")
    @Timed
    public List<MaintainType> getAllMaintainTypes() {
        log.debug("REST request to get all MaintainTypes");
        return maintainTypeRepository.findAll();
        }

    /**
     * GET  /maintain-types/:id : get the "id" maintainType.
     *
     * @param id the id of the maintainType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the maintainType, or with status 404 (Not Found)
     */
    @GetMapping("/maintain-types/{id}")
    @Timed
    public ResponseEntity<MaintainType> getMaintainType(@PathVariable Long id) {
        log.debug("REST request to get MaintainType : {}", id);
        MaintainType maintainType = maintainTypeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(maintainType));
    }

    /**
     * DELETE  /maintain-types/:id : delete the "id" maintainType.
     *
     * @param id the id of the maintainType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/maintain-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteMaintainType(@PathVariable Long id) {
        log.debug("REST request to delete MaintainType : {}", id);
        maintainTypeRepository.delete(id);
        maintainTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/maintain-types?query=:query : search for the maintainType corresponding
     * to the query.
     *
     * @param query the query of the maintainType search
     * @return the result of the search
     */
    @GetMapping("/_search/maintain-types")
    @Timed
    public List<MaintainType> searchMaintainTypes(@RequestParam String query) {
        log.debug("REST request to search MaintainTypes for query {}", query);
        return StreamSupport
            .stream(maintainTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
