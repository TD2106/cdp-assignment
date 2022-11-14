package com.duyle.routing_server;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class RoutingServiceImpl implements RoutingService {
    private static final String APPLICATION_SERVER_ID = "application-server";
    private final int DEFAULT_SERVER_WEIGHT = 5;
    private final Logger logger = LoggerFactory.getLogger(RoutingServiceImpl.class);

    private final RestTemplate restTemplate = new RestTemplate();

    private volatile ServerSelector serverSelector;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public JsonNode routePostRequest(String requestFullPath, JsonNode requestBody) {
        var currentServerSelector = getServerSelector();
        var serverInstance = currentServerSelector.next();
        if (serverInstance == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No application server found");
        }
        var uri = serverInstance.getUri().resolve(requestFullPath);
        logger.info("Forward the request to server: " + uri);
        try {
            var response = restTemplate.postForEntity(uri, requestBody, JsonNode.class);
            return response.getBody();
        } catch (HttpClientErrorException ex) {
            throw new ResponseStatusException(HttpStatus.valueOf(ex.getRawStatusCode()), ex.getMessage());
        }
    }

    @Override
    public void updateApplicationServerInstances() {
        if (serverSelector == null) return;

        var serviceInstances = discoveryClient.getInstances(APPLICATION_SERVER_ID);
        logger.info("Going to update the server selector with {} instances", serviceInstances.size());
        var currentInstances = serviceInstances.stream().map(this::toServerInfo)
                .collect(Collectors.toList());
        serverSelector.updateCurrentServers(currentInstances);
    }

    private ServerSelector getServerSelector() {
        if (serverSelector == null) {
            synchronized (this) {
                if (serverSelector == null) {
                    var serviceInstances = discoveryClient.getInstances(APPLICATION_SERVER_ID);
                    logger.info("Going to init the server selector with {} instances", serviceInstances.size());
                    var serverInstances = serviceInstances.stream()
                            .map(this::toServerInfo)
                            .collect(Collectors.toList());
                    serverSelector = new RoundRobinServerSelector(new ArrayList<>(serverInstances));
                }
            }
        }
        return serverSelector;
    }

    private ServerInstance toServerInfo(ServiceInstance serviceInstance) {
        return ServerInstance.builder()
                .hostIp(serviceInstance.getHost())
                .port(serviceInstance.getPort())
                .weight(DEFAULT_SERVER_WEIGHT)
                .build();
    }
}
