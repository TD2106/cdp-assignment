package com.duyle.routing_server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    @Autowired
    private RoutingService routingService;

    @Scheduled(fixedDelay = 60000)
    private void updateApplicationServerInstances() {
        routingService.updateApplicationServerInstances();
    }
}
