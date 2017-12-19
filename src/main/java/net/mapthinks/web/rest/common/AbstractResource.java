package net.mapthinks.web.rest.common;

import com.codahale.metrics.annotation.Timed;
import net.mapthinks.domain.base.AbstractBaseEntity;
import net.mapthinks.web.rest.util.HeaderUtil;
import net.mapthinks.web.rest.util.TypeResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.beans.Introspector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.simpleQueryStringQuery;

/**
 * REST controller for managing T.
 */
public abstract class AbstractResource<T extends AbstractBaseEntity,SR extends ElasticsearchRepository<T,Long>,R extends JpaRepository<T,Long>> {

    protected Logger log;

    protected Class<T> clazz;

    protected R repo;

    protected ElasticsearchRepository<T,Long> searchRepo;

    protected String restPathName;

    public AbstractResource(R repo, SR searchRepo) {
        Class<?>[] typeArguments = TypeResolver.resolveRawArguments(AbstractResource.class, getClass());
        this.repo = repo;
        this.clazz = (Class<T>) typeArguments[0];
        this.searchRepo = searchRepo;
        this.restPathName = Introspector.decapitalize(clazz.getSimpleName()+"s");
        this.log = LoggerFactory.getLogger(clazz);
    }

    /**
     * POST -> Create a new entity.
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<T> create(@Valid @RequestBody T entity) throws URISyntaxException {
        log.debug("REST request to save "+restPathName+" : {}", entity);
        if (entity.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(restPathName, "idexists", "A new "+restPathName+" cannot already have an ID")).body(null);
        }
        T result = repo.save(entity);
        searchRepo.save(result);
        return ResponseEntity.created(new URI("/api/"+restPathName+"/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(restPathName, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT -> Updates an existing entity.
     */
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<T> update(@Valid @RequestBody T entity) throws URISyntaxException {
        log.debug("REST request to update "+restPathName+" : {}", entity);
        if (entity.getId() == null) {
            return create(entity);
        }
        T result = repo.save(entity);
        searchRepo.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(restPathName, entity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /entitys/:id -> get the "id" entity.
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<T> get(@PathVariable Long id) {
        log.debug("REST request to get "+restPathName+"  : {}", id);
        T entity = repo.findOne(id);
        return Optional.ofNullable(entity)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /entitys/:id -> delete the "id" entity.
     */
    @DeleteMapping(value = "/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete "+restPathName+" : {}", id);
        repo.delete(id);
        searchRepo.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(restPathName, id.toString())).build();
    }

    /**
     * SEARCH  /_search/entitys/:query -> search for the entity corresponding
     * to the query.
     */
    @GetMapping(value = "/_search/{query}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<T> search(@PathVariable String query) {
        log.debug("REST request to search "+restPathName+" for query {}", query);


        return StreamSupport
            .stream(searchRepo.search(simpleQueryStringQuery("*"+query+"*").analyzeWildcard(true)).spliterator(), false)
            .collect(Collectors.toList());
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public R getRepo() {
        return repo;
    }

    public void setRepo(R repo) {
        this.repo = repo;
    }

    public ElasticsearchRepository<T, Long> getSearchRepo() {
        return searchRepo;
    }

    public void setSearchRepo(ElasticsearchRepository<T, Long> searchRepo) {
        this.searchRepo = searchRepo;
    }
}
