package com.duyle.routing_server;

import java.util.List;

public interface ServerSelector {
    ServerInstance next();

    void removeServers(List<ServerInstance> serverInstances);

    void addServers(List<ServerInstance> serverInstances);
}
