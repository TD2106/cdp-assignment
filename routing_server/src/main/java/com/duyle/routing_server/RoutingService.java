package com.duyle.routing_server;

import com.fasterxml.jackson.databind.JsonNode;

public interface RoutingService {
    JsonNode routePostRequest(String requestFullPath, JsonNode requestBody);

    void updateApplicationServerInstances();
}
