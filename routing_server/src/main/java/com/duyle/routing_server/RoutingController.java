package com.duyle.routing_server;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class RoutingController {
    private final Logger logger = LoggerFactory.getLogger(RoutingController.class);

    @Autowired
    private RoutingService routingService;

    @PostMapping(value = "**")
    public JsonNode routeRequest(@RequestBody JsonNode body, HttpServletRequest request) {
        String fullPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        logger.info(fullPath);
        return routingService.routePostRequest(fullPath, body);
    }
}
