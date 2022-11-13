package com.duyle.routing_server;

import java.util.List;

public class RoundRobinServerSelector implements ServerSelector {
    private final List<ServerInstance> serverInstances;
    private int current = -1;
    private int totalServers = 0;

    public RoundRobinServerSelector(List<ServerInstance> instances) {
        serverInstances = instances;
        totalServers = instances.size();
    }

    @Override
    public ServerInstance next() {
        if (serverInstances.size() == 0) return null;
        synchronized (serverInstances) {
            current = (current + 1) % totalServers;
            return serverInstances.get(current);
        }
    }

    @Override
    public void removeServers(List<ServerInstance> deletedServers) {
        synchronized (serverInstances) {
            serverInstances.removeAll(deletedServers);
            totalServers = serverInstances.size();
        }
    }

    @Override
    public void addServers(List<ServerInstance> addedServers) {
        synchronized (serverInstances) {
            serverInstances.addAll(addedServers);
            totalServers = serverInstances.size();
        }
    }
}
