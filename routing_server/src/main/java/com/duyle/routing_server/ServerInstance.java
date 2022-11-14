package com.duyle.routing_server;

import lombok.*;

import java.net.URI;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class ServerInstance {
    private String hostIp;
    private int port;
    private int weight;

    public URI getUri() {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(hostIp)
                .port(port)
                .build()
                .toUri();
    }
}
