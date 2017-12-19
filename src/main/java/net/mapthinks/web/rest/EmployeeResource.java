package net.mapthinks.web.rest;

import net.mapthinks.domain.Employee;
import net.mapthinks.repository.EmployeeRepository;
import net.mapthinks.repository.search.EmployeeSearchRepository;
import net.mapthinks.web.rest.common.AbstractPageableResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Employee.
 */
@RestController
@RequestMapping("/api/employees")
public class EmployeeResource extends AbstractPageableResource<Employee,EmployeeRepository,EmployeeSearchRepository> {

    private final Logger log = LoggerFactory.getLogger(EmployeeResource.class);

    private static final String ENTITY_NAME = "employee";

    public EmployeeResource(EmployeeRepository employeeRepository, EmployeeSearchRepository employeeSearchRepository) {
        super(employeeRepository,employeeSearchRepository);
    }

}
