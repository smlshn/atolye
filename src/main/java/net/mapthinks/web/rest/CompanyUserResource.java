package net.mapthinks.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.mapthinks.domain.CompanyUser;

import net.mapthinks.repository.CompanyUserRepository;
import net.mapthinks.repository.search.CompanyUserSearchRepository;
import net.mapthinks.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CompanyUser.
 */
@RestController
@RequestMapping("/api")
public class CompanyUserResource {

    private final Logger log = LoggerFactory.getLogger(CompanyUserResource.class);

    private static final String ENTITY_NAME = "companyUser";

    private final CompanyUserRepository companyUserRepository;

    private final CompanyUserSearchRepository companyUserSearchRepository;

    public CompanyUserResource(CompanyUserRepository companyUserRepository, CompanyUserSearchRepository companyUserSearchRepository) {
        this.companyUserRepository = companyUserRepository;
        this.companyUserSearchRepository = companyUserSearchRepository;
    }

    /**
     * POST  /company-users : Create a new companyUser.
     *
     * @param companyUser the companyUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new companyUser, or with status 400 (Bad Request) if the companyUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/company-users")
    @Timed
    public ResponseEntity<CompanyUser> createCompanyUser(@RequestBody CompanyUser companyUser) throws URISyntaxException {
        log.debug("REST request to save CompanyUser : {}", companyUser);
        if (companyUser.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new companyUser cannot already have an ID")).body(null);
        }
        CompanyUser result = companyUserRepository.save(companyUser);
        companyUserSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/company-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /company-users : Updates an existing companyUser.
     *
     * @param companyUser the companyUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated companyUser,
     * or with status 400 (Bad Request) if the companyUser is not valid,
     * or with status 500 (Internal Server Error) if the companyUser couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/company-users")
    @Timed
    public ResponseEntity<CompanyUser> updateCompanyUser(@RequestBody CompanyUser companyUser) throws URISyntaxException {
        log.debug("REST request to update CompanyUser : {}", companyUser);
        if (companyUser.getId() == null) {
            return createCompanyUser(companyUser);
        }
        CompanyUser result = companyUserRepository.save(companyUser);
        companyUserSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, companyUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /company-users : get all the companyUsers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of companyUsers in body
     */
    @GetMapping("/company-users")
    @Timed
    public List<CompanyUser> getAllCompanyUsers() {
        log.debug("REST request to get all CompanyUsers");
        return companyUserRepository.findAll();
        }

    /**
     * GET  /company-users/:id : get the "id" companyUser.
     *
     * @param id the id of the companyUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the companyUser, or with status 404 (Not Found)
     */
    @GetMapping("/company-users/{id}")
    @Timed
    public ResponseEntity<CompanyUser> getCompanyUser(@PathVariable Long id) {
        log.debug("REST request to get CompanyUser : {}", id);
        CompanyUser companyUser = companyUserRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(companyUser));
    }

    /**
     * DELETE  /company-users/:id : delete the "id" companyUser.
     *
     * @param id the id of the companyUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/company-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompanyUser(@PathVariable Long id) {
        log.debug("REST request to delete CompanyUser : {}", id);
        companyUserRepository.delete(id);
        companyUserSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/company-users?query=:query : search for the companyUser corresponding
     * to the query.
     *
     * @param query the query of the companyUser search
     * @return the result of the search
     */
    @GetMapping("/_search/company-users")
    @Timed
    public List<CompanyUser> searchCompanyUsers(@RequestParam String query) {
        log.debug("REST request to search CompanyUsers for query {}", query);
        return StreamSupport
            .stream(companyUserSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
