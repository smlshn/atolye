package net.mapthinks.repository.search;

import net.mapthinks.domain.Town;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Town entity.
 */
public interface TownSearchRepository extends ElasticsearchRepository<Town, Long> {
}
