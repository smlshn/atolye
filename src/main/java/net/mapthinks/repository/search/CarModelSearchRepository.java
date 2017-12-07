package net.mapthinks.repository.search;

import net.mapthinks.domain.CarModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CarModel entity.
 */
public interface CarModelSearchRepository extends ElasticsearchRepository<CarModel, Long> {
}
