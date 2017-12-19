package net.mapthinks.service;

import com.codahale.metrics.annotation.Timed;
import net.mapthinks.config.ElasticsearchConfiguration;
import net.mapthinks.config.JacksonConfiguration;
import net.mapthinks.domain.*;
import net.mapthinks.repository.*;
import net.mapthinks.repository.search.*;
import org.elasticsearch.client.Client;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;

@Service
@AutoConfigureAfter(value = { JacksonConfiguration.class })
public class ElasticsearchIndexService {

    private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexService.class);

    @Resource
    private CarBrandRepository carBrandRepository;
    @Resource
    private CarBrandSearchRepository carBrandSearchRepository;

    @Resource
    private CarModelSearchRepository carModelSearchRepository;
    @Resource
    private CarModelRepository carModelRepository;

    @Resource
    private CarRepository carRepository;
    @Resource
    private CarSearchRepository carSearchRepository;

    @Resource
    private CityRepository cityRepository;
    @Resource
    private CitySearchRepository citySearchRepository;

    @Resource
    private CompanyRepository companyRepository;
    @Resource
    private CompanySearchRepository companySearchRepository;

    @Resource
    private CompanyUserRepository companyUserRepository;
    @Resource
    private CompanyUserSearchRepository companyUserSearchRepository;

    @Resource
    private CustomerRepository customerRepository;
    @Resource
    private CustomerSearchRepository customerSearchRepository;

    @Resource
    private EmployeeRepository employeeRepository;
    @Resource
    private EmployeeSearchRepository employeeSearchRepository;

    @Resource
    private MaintainInstanceRepository maintainInstanceRepository;
    @Resource
    private MaintainInstanceSearchRepository maintainInstanceSearchRepository;

    @Resource
    private MaintainTypeRepository maintainTypeRepository;
    @Resource
    private MaintainTypeSearchRepository maintainTypeSearchRepository;

    @Resource
    private MaintenanceRepository maintenanceRepository;
    @Resource
    private MaintenanceSearchRepository maintenanceSearchRepository;

    @Resource
    private OperationRepository operationRepository;
    @Resource
    private OperationSearchRepository operationSearchRepository;

    @Resource
    private SystemUserRepository systemUserRepository;
    @Resource
    private SystemUserSearchRepository systemUserSearchRepository;

    @Resource
    private TownRepository townRepository;
    @Resource
    private TownSearchRepository townSearchRepository;

    @Resource
    private Client client;

    @Resource
    private Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;

    @Async
    @Timed
    public void reindexAll() {

        reindexForClass(CarBrand.class, carBrandRepository, carBrandSearchRepository);
        reindexForClass(CarModel.class, carModelRepository, carModelSearchRepository);
        reindexForClass(Car.class, carRepository, carSearchRepository);
        reindexForClass(City.class, cityRepository, citySearchRepository);
        reindexForClass(Company.class, companyRepository, companySearchRepository);
        reindexForClass(CompanyUser.class, companyUserRepository, companyUserSearchRepository);
        reindexForClass(Customer.class, customerRepository, customerSearchRepository);
        reindexForClass(Employee.class, employeeRepository, employeeSearchRepository);
        reindexForClass(MaintainInstance.class, maintainInstanceRepository, maintainInstanceSearchRepository);
        reindexForClass(MaintainType.class, maintainTypeRepository, maintainTypeSearchRepository);
        reindexForClass(Maintenance.class, maintenanceRepository, maintenanceSearchRepository);
        reindexForClass(Operation.class, operationRepository, operationSearchRepository);
        reindexForClass(SystemUser.class, systemUserRepository, systemUserSearchRepository);
        reindexForClass(Town.class, townRepository, townSearchRepository);


        log.info("Elasticsearch: Successfully performed reindexing");
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public <T> void reindexForClass(Class<T> entityClass, JpaRepository<T, Long> jpaRepository,
                                     ElasticsearchRepository<T, Long> elasticsearchRepository) {
        ElasticsearchTemplate est = new ElasticsearchTemplate(client, new ElasticsearchConfiguration.CustomEntityMapper(jackson2ObjectMapperBuilder.createXmlMapper(false).build()));
        est.deleteIndex(entityClass);
        try {
            est.createIndex(entityClass);
        } catch (IndexAlreadyExistsException e) {
            // Do nothing. Index was already concurrently recreated by some other service.
        }
        est.putMapping(entityClass);
        if (jpaRepository.count() > 0) {
            try {
                Method m = jpaRepository.getClass().getMethod("findAllWithEagerRelationships");
                List<T> list = (List<T>) m.invoke(jpaRepository);
                elasticsearchRepository.save(list);
            } catch (Exception e) {
                List<T> list = jpaRepository.findAll();
                elasticsearchRepository.save(list);
            }
        }
        log.info("Elasticsearch: Indexed all rows for " + entityClass.getSimpleName());
    }
}
