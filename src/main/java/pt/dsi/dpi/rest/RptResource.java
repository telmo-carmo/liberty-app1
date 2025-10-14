package pt.dsi.dpi.rest;

import jakarta.inject.Inject;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

//import com.ibm.websphere.product.VersionInfo;



@Path("/rpt")
public class RptResource {
    @Inject
    ExcelService1 excelService;

    
    @GET
    @Path("/excel1")
    @Produces("application/vnd.ms-excel")
    public Response downloadExcel(@QueryParam("name") String name) {  //  http://localhost:9080/app1/v1/rpt/excel1?name=t1.xlsx

        String filename = "Sample1.xlsx";
        if (name != null && !name.isEmpty() && name.contains(".xl"))
            filename = name;
        InputStream is = new ByteArrayInputStream(excelService.toByteArray());
        ResponseBuilder responseBuilder = Response.ok(is);
        responseBuilder.header("Content-Disposition", String.format("attachment; filename=\"%s\"", filename));
        return responseBuilder.build();
    }


    @GET
    @Path("/info")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> getInfo() {
        Map<String, String> status = new HashMap<>();
        status.put("java.version", System.getProperty("java.version"));
        status.put("java.vendor", System.getProperty("java.vendor"));
        
        // Retrieve Open Liberty version, current Oct/2025: 25.0.0.10
        String libertyVersion =   "Open Liberty 25.0.0.10"; //VersionInfo.getVersion();
        status.put("openliberty.version", libertyVersion);

        Date now = new Date();
        String isoNow = java.time.format.DateTimeFormatter.ISO_INSTANT.format(now.toInstant());
        status.put("timestamp", isoNow);

        return status;
    }
    
}
