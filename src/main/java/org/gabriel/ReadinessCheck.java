package org.gabriel;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@Readiness
@ApplicationScoped
public class ReadinessCheck implements HealthCheck {

  @RestClient
  @Inject
  TimeService timeService;

  @Inject
  StatusSocket socket;

  private static final Logger LOGGER = Logger.getLogger("ListenerBean");

  private String status = "";

  @Scheduled(every = "2s")
  void increment() {
    var tmpStatus = this.call().getStatus().name();
    if(!this.status.equalsIgnoreCase(tmpStatus) ) {
       this.status = tmpStatus;
       socket.broadcast(this.status);
       LOGGER.info("Status: " + this.status);
    }
    
  }

  @Override
  public HealthCheckResponse call() {
    if (timeService.getWorldClock().getDatetime().equals(TimeService.MSG_ERROR)) {
      return HealthCheckResponse.down(timeService.getClass().getTypeName());
    } else {
      return HealthCheckResponse.up(timeService.getClass().getTypeName());
    }
  }


}
