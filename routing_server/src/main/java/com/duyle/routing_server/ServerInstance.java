package com.duyle.routing_server;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.net.URI;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@Setter
@Builder
@EqualsAndHashCode
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
