package net.mapthinks.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.mapthinks.domain.Town;

import net.mapthinks.repository.TownRepository;
import net.mapthinks.repository.search.TownSearchRepository;
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
 * REST controller for managing Town.
 */
@RestController
@RequestMapping("/api")
public class TownResource {

    private final Logger log = LoggerFactory.getLogger(TownResource.class);

    private static final String ENTITY_NAME = "town";

    private final TownRepository townRepository;

    private final TownSearchRepository townSearchRepository;

    public TownResource(TownRepository townRepository, TownSearchRepository townSearchRepository) {
        this.townRepository = townRepository;
        this.townSearchRepository = townSearchRepository;
    }

    /**
     * POST  /towns : Create a new town.
     *
     * @param town the town to create
     * @return the ResponseEntity with status 201 (Created) and with body the new town, or with status 400 (Bad Request) if the town has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/towns")
    @Timed
    public ResponseEntity<Town> createTown(@Valid @RequestBody Town town) throws URISyntaxException {
        log.debug("REST request to save Town : {}", town);
        if (town.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new town cannot already have an ID")).body(null);
        }
        Town result = townRepository.save(town);
        townSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/towns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /towns : Updates an existing town.
     *
     * @param town the town to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated town,
     * or with status 400 (Bad Request) if the town is not valid,
     * or with status 500 (Internal Server Error) if the town couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/towns")
    @Timed
    public ResponseEntity<Town> updateTown(@Valid @RequestBody Town town) throws URISyntaxException {
        log.debug("REST request to update Town : {}", town);
        if (town.getId() == null) {
            return createTown(town);
        }
        Town result = townRepository.save(town);
        townSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, town.getId().toString()))
            .body(result);
    }

    /**
     * GET  /towns : get all the towns.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of towns in body
     */
    @GetMapping("/towns")
    @Timed
    public ResponseEntity<List<Town>> getAllTowns(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Towns");
        Page<Town> page = townRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/towns");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /towns/:id : get the "id" town.
     *
     * @param id the id of the town to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the town, or with status 404 (Not Found)
     */
    @GetMapping("/towns/{id}")
    @Timed
    public ResponseEntity<Town> getTown(@PathVariable Long id) {
        log.debug("REST request to get Town : {}", id);
        Town town = townRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(town));
    }

    /**
     * DELETE  /towns/:id : delete the "id" town.
     *
     * @param id the id of the town to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/towns/{id}")
    @Timed
    public ResponseEntity<Void> deleteTown(@PathVariable Long id) {
        log.debug("REST request to delete Town : {}", id);
        townRepository.delete(id);
        townSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/towns?query=:query : search for the town corresponding
     * to the query.
     *
     * @param query the query of the town search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/towns")
    @Timed
    public ResponseEntity<List<Town>> searchTowns(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Towns for query {}", query);
        Page<Town> page = townSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/towns");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
