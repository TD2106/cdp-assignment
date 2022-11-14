package com.duyle.routing_server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class RoundRobinServerSelector implements ServerSelector {
    private final Logger logger = LoggerFactory.getLogger(RoundRobinServerSelector.class);

    private final List<ServerInstance> serverInstances;
    private AtomicInteger current = new AtomicInteger(-1);
    private int totalServers = 0;
    private ReadWriteLock lock;

    public RoundRobinServerSelector(List<ServerInstance> instances) {
        serverInstances = new ArrayList<>(instances);
        totalServers = instances.size();
        lock = new ReentrantReadWriteLock(true);
    }

    @Override
    public ServerInstance next() {
        if (serverInstances.size() == 0) return null;
        lock.readLock().lock();
        int currentIdx = current.updateAndGet(val -> (val + 1) % totalServers);
        var serverInstance = serverInstances.get(currentIdx);
        lock.readLock().unlock();
        return serverInstance;
    }

    @Override
    public void updateCurrentServers(List<ServerInstance> currentServerInstances) {
        logger.info("Current instances: {}", currentServerInstances);
        var addedInstances = currentServerInstances.stream()
                .filter(serverInstance -> !this.serverInstances.contains(serverInstance))
                .collect(Collectors.toList());
        var deletedInstances = this.serverInstances.stream()
                .filter(serverInstance -> !currentServerInstances.contains(serverInstance))
                .collect(Collectors.toList());
        if (addedInstances.size() != 0 || deletedInstances.size() != 0) {
            lock.writeLock().lock();
            this.serverInstances.removeAll(deletedInstances);
            this.serverInstances.addAll(addedInstances);
            this.totalServers = this.serverInstances.size();
            lock.writeLock().unlock();
        }
        logger.info("Updated instances: {}", currentServerInstances);
    }
}
