package com.duyle.routing_server;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoundRobinServerSelector implements ServerSelector {
    private final List<ServerInstance> serverInstances;
    private int current = -1;
    private int totalServers = 0;

    public RoundRobinServerSelector(List<ServerInstance> instances) {
        serverInstances = new ArrayList<>(instances);
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
    public void updateCurrentServers(List<ServerInstance> currentServerInstances) {
        var addedInstances = currentServerInstances.stream()
                .filter(serverInstance -> !this.serverInstances.contains(serverInstance))
                .collect(Collectors.toList());
        var deletedInstances = this.serverInstances.stream()
                .filter(serverInstance -> !currentServerInstances.contains(serverInstance))
                .collect(Collectors.toList());

        if (addedInstances.size() != 0 || deletedInstances.size() != 0) {
            synchronized (this.serverInstances) {
                this.serverInstances.removeAll(deletedInstances);
                this.serverInstances.addAll(addedInstances);
                this.totalServers = this.serverInstances.size();
            }
        }
    }
}
