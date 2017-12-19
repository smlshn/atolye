package net.mapthinks.web.rest.common;

import com.codahale.metrics.annotation.Timed;
import net.mapthinks.domain.base.AbstractBaseEntity;
import net.mapthinks.web.rest.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URISyntaxException;
import java.util.List;


public abstract class AbstractPageableResource<T extends AbstractBaseEntity,R extends JpaRepository<T,Long>,SR extends ElasticsearchRepository<T,Long>> extends AbstractResource<T,SR,R> {

    public AbstractPageableResource(R repo, SR searchRepo) {
        super(repo,searchRepo);
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<List<T>> getAll(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of Towns");
        Page<T> page = repo.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/"+restPathName);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}

