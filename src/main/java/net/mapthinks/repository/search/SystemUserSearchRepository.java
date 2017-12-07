package net.mapthinks.repository.search;

import net.mapthinks.domain.SystemUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SystemUser entity.
 */
public interface SystemUserSearchRepository extends ElasticsearchRepository<SystemUser, Long> {
}
