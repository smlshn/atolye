package net.mapthinks.web.rest.common;

import com.codahale.metrics.annotation.Timed;
import net.mapthinks.domain.base.AbstractBaseEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * REST controller for managing T.
 */
public abstract class AbstractSimpleResource<T extends AbstractBaseEntity,SR extends ElasticsearchRepository<T,Long>,R extends JpaRepository<T,Long>> extends AbstractResource<T,SR,R> {

    public AbstractSimpleResource(R repo, SR searchRepo) {
        super(repo,searchRepo);
    }

    /**
     * GET  /entitys -> get all the entitys.
     */
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<T> getAll() {
        log.debug("REST request to get all "+restPathName);
        return repo.findAll();
    }

}

