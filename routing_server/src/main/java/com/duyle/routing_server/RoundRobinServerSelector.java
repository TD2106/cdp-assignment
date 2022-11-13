package com.duyle.routing_server;

import java.util.List;

public class RoundRobinServerSelector implements ServerSelector {
    private List<ServerInstance> serverInstances;

    public RoundRobinServerSelector(List<ServerInstance> instances) {
        serverInstances = instances;
    }

    @Override
    public ServerInstance next() {
        return null;
    }

    @Override
    public void removeServers(List<ServerInstance> serverInstances) {


    }

    @Override
    public void addServers(List<ServerInstance> serverInstances) {

    }
}
