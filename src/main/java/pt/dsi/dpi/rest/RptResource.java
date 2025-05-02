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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@Path("/rpt")
public class RptResource {
    @Inject
    ExcelService1 excelService;

    @GET
    @Path("/excel1")
    @Produces("application/vnd.ms-excel")
    public Response downloadExcel() {

        final String filename = "Sample1.xlsx";

        InputStream is = new ByteArrayInputStream(excelService.toByteArray());
        ResponseBuilder responseBuilder = Response.ok(is);
        responseBuilder.header("Content-Disposition", String.format("attachment; filename=\"%s\"", filename));
        return responseBuilder.build();
    }
}
