package net.mapthinks.service.report;

import io.github.jhipster.config.JHipsterProperties;
import net.mapthinks.service.JpaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MacbookPro on 25.09.2016.
 */
@Service
public class ReportFileService {

    private final Logger log = LoggerFactory.getLogger(ReportFileService.class);

    @Inject
    private JHipsterProperties properties;

    @Inject
    private JpaService jpaService;

    public boolean saveSalesOfferReport(byte[] reportByte, Long sofiId) throws IOException{

        /*
        SalesOfferInstance sofi = (SalesOfferInstance) jpaService.findOne(new SalesOfferInstance(sofiId));
        SalesOffer sof = (SalesOffer) jpaService.findOne(sofi.getSalesOffer());

        String custName = sof.getCustomer().getName();
        if(custName.contains(" "))
            custName = custName.substring(0,custName.indexOf(" "));

        SimpleDateFormat fileFormatter = new SimpleDateFormat(properties.getFile().getFileDateFormat());
        String date = fileFormatter.format(new Date());

        String fileName = custName+"_"+date+"_"+sofi.getRevisionNo() + ".pdf";


        FileOutputStream fos=null;
        boolean fosSuccessed=false;
        try {
            fos = new FileOutputStream(createPriceOfferFolder()+"/"+fileName);
            fosSuccessed = true;
            fos.write(reportByte);
            fos.close();
        } catch (IOException e) {
            String fosResult="";

            e.getMessage().concat(
                    "\nFileOutput Sucessed: "+fosSuccessed+
                    "\nAbsolute FilePath: "+properties.getFile().getAbsoluteFolderPath()+
                    "\nProperties FilePath: "+properties.getFile().getPriceOfferFolderPath());
            throw e;
        }
        */
        return true;
    }

    private String createPriceOfferFolder() throws IOException{
        /*
        String absoluteFolderPath = properties.getFile().getAbsoluteFolderPath();
        createFolder(absoluteFolderPath);

        absoluteFolderPath += "/"+properties.getFile().getPriceOfferFolderPath();
        createFolder(absoluteFolderPath);

        SimpleDateFormat formatter = new SimpleDateFormat(properties.getFile().getFolderDateFormat());
        String date = formatter.format(new Date());

        return createFolder(absoluteFolderPath+"/"+date);
        */
        return null;
    }

    private String createFolder(String name){

        File directory = new File(String.valueOf(name));
        if (! directory.exists()){
            log.info("folder create (path): "+name);
            directory.mkdir();
        }

        return name;
    }
}
