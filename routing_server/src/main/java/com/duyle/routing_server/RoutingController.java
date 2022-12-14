package com.duyle.routing_server;

import com.fasterxml.jackson.databind.JsonNode;
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
    @Autowired
    private RoutingService routingService;

    @PostMapping(value = "**")
    public JsonNode routeRequest(@RequestBody JsonNode body, HttpServletRequest request) {
        return routingService.routePostRequest(request, body);
    }
}
