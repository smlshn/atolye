package net.mapthinks.repository.search;

import net.mapthinks.domain.MaintainType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MaintainType entity.
 */
public interface MaintainTypeSearchRepository extends ElasticsearchRepository<MaintainType, Long> {
}
