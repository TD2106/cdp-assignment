package com.duyle.routing_server;

import com.fasterxml.jackson.databind.JsonNode;

import javax.servlet.http.HttpServletRequest;

public interface RoutingService {
    JsonNode routePostRequest(HttpServletRequest request, JsonNode requestBody);

    void updateApplicationServerInstances();
}
