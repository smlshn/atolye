package net.mapthinks.service.report;

import net.mapthinks.service.UserService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.mt.domain.officeTasks.CargoSticker;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Service Implementation for managing Customer.
 */
@Service
@Transactional
public class ReportService {
    private final Logger log = LoggerFactory.getLogger(ReportService.class);

    @Inject
    private ApplicationContext context;

    @Inject
    private DataSource dataSource;

    @Inject
    private UserService userService;

    @Inject
    private ReportFileService fileService;


    public byte[] getSalesOfferReportLong(ReportParameter parameter, boolean saveReport) {

        Map<String,Object> params = new HashMap<String,Object>();


        params.put("REPORT_RESOURCE_BUNDLE",getBundle(parameter.getStrValue()));
        params.put("SOFI_ID",parameter.getLongValue());
        params.put("SUBREPORT_DIR","reports/salesoffer/");


        String path = "reports/salesoffer/SalesOffer.jasper";

        byte[] report = getReport(params,parameter.getPermissions(), path);

        if(saveReport){
            try {
                fileService.saveSalesOfferReport(report,parameter.getLongValue());
            } catch (IOException e) {
                log.error("report pdf couldn't be saved due to invalid file path! "+e.getMessage());
            }
        }

        return report;
    }


    public byte[] getCargoSticker(CargoSticker cargoSticker) {

        Map<String,Object> params = new HashMap<String,Object>();

        params.put("SENDER_COMPANY",cargoSticker.getSender().getCompany());
        params.put("SENDER_CONNECTION_PERSON",cargoSticker.getSender().getConnection());
        params.put("SENDER_PHONE",cargoSticker.getSender().getPhone());

        params.put("SENDER_ADDRESS",cargoSticker.getSender().getAddress());
        params.put("SENDER_TOWN_CITY",cargoSticker.getSender().getTownCity());

        params.put("RECEIVER_COMPANY",cargoSticker.getReceiver().getCompany());
        params.put("RECEIVER_CONNECTION_PERSON",cargoSticker.getReceiver().getConnection());
        params.put("RECEIVER_PHONE",cargoSticker.getReceiver().getPhone());

        params.put("RECEIVER_ADDRESS",cargoSticker.getReceiver().getAddress());
        params.put("RECEIVER_TOWN_CITY",cargoSticker.getReceiver().getTownCity());

        params.put("REPORT_RESOURCE_BUNDLE",getBundle(cargoSticker.getLang()));

        String format = cargoSticker.getFormat()==null?"A4":cargoSticker.getFormat();
        String path = "reports/officeTasks/CargoSticker"+format+".jasper";

        return getReport(params, null, path);
    }

    public byte[] getPreviewFirstPage(ReportParameter parameter) {

        Map<String,Object> params = new HashMap<String,Object>();
        params.put("EXPL_ID",parameter.getLongValue());
        String path = "reports/preview/FirstPage.jasper";

        return getReport(params,parameter.getPermissions(), path);
    }

    public byte[] getPreviewExplanations(ReportParameter parameter) {

        Map<String,Object> params = new HashMap<String,Object>();
        params.put("EXPL_ID",parameter.getLongValue());
        String path = "reports/preview/Explanations.jasper";

        return getReport(params,parameter.getPermissions(), path);
    }

    public byte[] getPreviewProduct(ReportParameter parameter) {

        Map<String,Object> params = new HashMap<String,Object>();
        params.put("PROD_ID", parameter.getLongValue());

        if(parameter.getLongValueThird()!=null) {
            params.put("IMAGE_ID",parameter.getLongValueThird());
        }
        if(parameter.getLongValueSecond()!=null)
            params.put("EXPL_ID",parameter.getLongValueSecond());


        String path = "reports/preview/Products.jasper";

        return getReport(params,parameter.getPermissions(), path);
    }

    private byte[] getReport(Map<String,Object> params,SimplePdfExporterConfiguration permissions, String path){
        DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
        JRPropertiesUtil.getInstance(context).setProperty("net.sf.jasperreports.xpath.executer.factory",
            "net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");

        JRPropertiesUtil.getInstance(context).setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");
        JRPropertiesUtil.getInstance(context).setProperty("net.sf.jasperreports.default.font.name", "Helvetica");

        JRPropertiesUtil.getInstance(context).setProperty("net.sf.jasperreports.text.measurer.factory.single.line",
            "com.jaspersoft.jasperserver.api.engine.jasperreports.util.SingleLineTextMeasurerFactory");

        //params.put("REPORT_LOCALE",locale);

        Connection con=null;
        byte[] pdfByteArray=null;
        try {
            con = dataSource.getConnection();
            Resource resource = new ClassPathResource(path);
            InputStream resourceInputStream = resource.getInputStream();
            JasperPrint jasperPrint = JasperFillManager.fillReport(resourceInputStream, params,con );

            pdfByteArray = exportToPdf(jasperPrint,permissions);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                con.close();
            }
            catch (Exception ex) {

            }
        }
        return pdfByteArray;
    }

    public byte[] exportToPdf(JasperPrint jasperPrint,SimplePdfExporterConfiguration configuration) throws JRException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        JRPdfExporter exporter = createExporter(configuration);

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
        exporter.exportReport();

        return baos.toByteArray();
    }

    protected JRPdfExporter createExporter(SimplePdfExporterConfiguration configuration) {
        JRPdfExporter exporter =  new JRPdfExporter();
        if(configuration!=null) {
            configuration.setEncrypted(true);
            configuration.set128BitKey(true);
            exporter.setConfiguration(configuration);
        }
        return exporter;
    }

    protected ResourceBundle getBundle(String lang){
        String langKey = null;
        if(lang!=null && !lang.isEmpty())
            langKey = lang;
        else
            langKey = userService.getUserWithAuthorities().getLangKey();

        Locale locale = Locale.forLanguageTag(langKey);
        return ResourceBundle.getBundle("i18n/messages", locale );
    }
}
