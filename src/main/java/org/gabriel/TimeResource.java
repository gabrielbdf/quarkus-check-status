package org.gabriel;

import javax.print.attribute.standard.MediaName;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/time")

public class TimeResource {

    @RestClient
    @Inject
    TimeService service;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String currentDateTime() {
        return service.getWorldClock().getDatetime();
    }

    

    
}
