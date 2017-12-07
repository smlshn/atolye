package net.mapthinks.repository.search;

import net.mapthinks.domain.MaintainInstance;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MaintainInstance entity.
 */
public interface MaintainInstanceSearchRepository extends ElasticsearchRepository<MaintainInstance, Long> {
}
