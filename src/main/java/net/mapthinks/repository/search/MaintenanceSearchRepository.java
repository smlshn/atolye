package net.mapthinks.repository.search;

import net.mapthinks.domain.Maintenance;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Maintenance entity.
 */
public interface MaintenanceSearchRepository extends ElasticsearchRepository<Maintenance, Long> {
}
