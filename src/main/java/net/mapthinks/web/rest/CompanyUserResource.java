package net.mapthinks.web.rest;

import net.mapthinks.domain.CompanyUser;
import net.mapthinks.repository.CompanyUserRepository;
import net.mapthinks.repository.search.CompanyUserSearchRepository;
import net.mapthinks.web.rest.common.AbstractPageableResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing CompanyUser.
 */
@RestController
@RequestMapping("/api/company-users")
public class CompanyUserResource extends AbstractPageableResource<CompanyUser,CompanyUserRepository,CompanyUserSearchRepository> {

    private final Logger log = LoggerFactory.getLogger(CompanyUserResource.class);

    private static final String ENTITY_NAME = "companyUser";

    public CompanyUserResource(CompanyUserRepository companyUserRepository, CompanyUserSearchRepository companyUserSearchRepository) {
        super(companyUserRepository,companyUserSearchRepository);
    }

}
