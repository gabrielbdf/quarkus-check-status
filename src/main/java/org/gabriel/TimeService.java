package org.gabriel;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient
public interface TimeService {

    String MSG_ERROR = "we have a problem";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(value = 80L)
    @Fallback(fallbackMethod = "fallback")
    WorldClock getWorldClock();

    default WorldClock fallback() {
        return new WorldClock(MSG_ERROR);
    }

}
