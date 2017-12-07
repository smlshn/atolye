package net.mapthinks.repository.search;

import net.mapthinks.domain.CarBrand;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CarBrand entity.
 */
public interface CarBrandSearchRepository extends ElasticsearchRepository<CarBrand, Long> {
}
