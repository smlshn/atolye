package net.mapthinks.web.rest;

import net.mapthinks.domain.Customer;
import net.mapthinks.repository.CustomerRepository;
import net.mapthinks.repository.search.CustomerSearchRepository;
import net.mapthinks.web.rest.common.AbstractPageableResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Customer.
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerResource extends AbstractPageableResource<Customer,CustomerRepository,CustomerSearchRepository> {

    private final Logger log = LoggerFactory.getLogger(CustomerResource.class);

    private static final String ENTITY_NAME = "customer";

    public CustomerResource(CustomerRepository customerRepository, CustomerSearchRepository customerSearchRepository) {
        super(customerRepository,customerSearchRepository);
    }

}
