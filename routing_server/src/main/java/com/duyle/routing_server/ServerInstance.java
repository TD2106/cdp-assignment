package com.duyle.routing_server;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ServerInstance {
    private String hostIp;
    private Integer port;
    private Integer weight;

    public String getUrl() {
        return hostIp + ":" + port;
    }
}
