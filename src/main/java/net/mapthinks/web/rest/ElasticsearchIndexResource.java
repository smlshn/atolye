package net.mapthinks.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.mapthinks.security.AuthoritiesConstants;
import net.mapthinks.security.SecurityUtils;
import net.mapthinks.service.ElasticsearchIndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.net.URISyntaxException;

/**
 * REST controller for managing Elasticsearch index.
 */
@RestController
@RequestMapping("/api/elasticsearch/index")
public class ElasticsearchIndexResource {

    private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexResource.class);

    @Resource
    private ElasticsearchIndexService elasticsearchIndexService;

    /**
     * POST  /elasticsearch/index -> Reindex all Elasticsearch documents
     */
    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    @PreAuthorize("hasAuthority('" + AuthoritiesConstants.ADMIN + "')")
    public ResponseEntity<String> reindexAll() throws URISyntaxException {
        log.info("REST request to reindex Elasticsearch by user : {}", SecurityUtils.getCurrentUserLogin());
        elasticsearchIndexService.reindexAll();
        return new ResponseEntity<>("Request accepted, performing full Elasticsearch reindexing.", HttpStatus.ACCEPTED);
    }
}
