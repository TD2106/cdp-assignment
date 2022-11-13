package com.duyle.routing_server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    private final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private RoutingService routingService;

    @Scheduled(fixedDelay = 60000)
    private void updateApplicationServerInstances() {
        logger.info("Going to update the application server instances");
        routingService.updateApplicationServerInstances();
    }
}
