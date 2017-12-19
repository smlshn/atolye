package net.mapthinks.web.rest;

import net.mapthinks.domain.Company;
import net.mapthinks.repository.CompanyRepository;
import net.mapthinks.repository.search.CompanySearchRepository;
import net.mapthinks.web.rest.common.AbstractPageableResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Company.
 */
@RestController
@RequestMapping("/api/companies")
public class CompanyResource extends AbstractPageableResource<Company,CompanyRepository,CompanySearchRepository>{

    private final Logger log = LoggerFactory.getLogger(CompanyResource.class);

    private static final String ENTITY_NAME = "company";

    public CompanyResource(CompanyRepository companyRepository, CompanySearchRepository companySearchRepository) {
        super(companyRepository,companySearchRepository);
    }

}
