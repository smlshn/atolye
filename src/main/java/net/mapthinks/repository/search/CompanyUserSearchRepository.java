package net.mapthinks.repository.search;

import net.mapthinks.domain.CompanyUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CompanyUser entity.
 */
public interface CompanyUserSearchRepository extends ElasticsearchRepository<CompanyUser, Long> {
}
