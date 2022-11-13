package com.duyle.routing_server;

import java.util.List;

public interface ServerSelector {
    ServerInstance next();

    void updateCurrentServers(List<ServerInstance> currentServerInstances);
}
