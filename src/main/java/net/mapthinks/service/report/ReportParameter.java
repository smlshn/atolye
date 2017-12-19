package net.mapthinks.service.report;

import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

import java.util.Map;

/**
 * Created by MacbookPro on 28/05/16.
 */
public class ReportParameter {

    private Map<String,Object> parameters;
    private SimplePdfExporterConfiguration permissions;

    public SimplePdfExporterConfiguration getPermissions() {
        return permissions;
    }

    public void setPermissions(SimplePdfExporterConfiguration permissions) {
        this.permissions = permissions;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}
