package net.mapthinks.web.rest;

import net.mapthinks.domain.Maintenance;
import net.mapthinks.repository.MaintenanceRepository;
import net.mapthinks.repository.search.MaintenanceSearchRepository;
import net.mapthinks.web.rest.common.AbstractPageableResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Maintenance.
 */
@RestController
@RequestMapping("/api/maintenances")
public class MaintenanceResource extends AbstractPageableResource<Maintenance,MaintenanceRepository,MaintenanceSearchRepository> {

    private final Logger log = LoggerFactory.getLogger(MaintenanceResource.class);

    private static final String ENTITY_NAME = "maintenance";

    public MaintenanceResource(MaintenanceRepository maintenanceRepository, MaintenanceSearchRepository maintenanceSearchRepository) {
        super(maintenanceRepository,maintenanceSearchRepository);
    }

}
