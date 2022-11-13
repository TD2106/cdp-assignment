package com.duyle.routing_server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerSelectorTest {
    @Test
    public void testServerSelectorSingleThreadShouldWorksFineForAllFunction() {
        final var serverInstance1 = new ServerInstance("localhost", 8080, 5);
        final var serverInstance2 = new ServerInstance("localhost", 8081, 5);
        final var serverInstance3 = new ServerInstance("localhost", 8082, 5);

        final var selector = new RoundRobinServerSelector(Arrays.asList(serverInstance1, serverInstance2, serverInstance3));

        assertEquals(selector.next(), serverInstance1);
        assertEquals(selector.next(), serverInstance2);
        assertEquals(selector.next(), serverInstance3);

        final var serverInstance4 = new ServerInstance("localhost", 8083, 5);
        selector.updateCurrentServers(Arrays.asList(serverInstance1, serverInstance4));

        assertEquals(selector.next(), serverInstance4);
        assertEquals(selector.next(), serverInstance1);
        assertEquals(selector.next(), serverInstance4);
        assertEquals(selector.next(), serverInstance1);
    }

    @Test
    public void testServerSelectorMultiThreadShouldDistributeLoadEvenly() {
        final var serverInstance1 = new ServerInstance("localhost", 8080, 5);
        final var serverInstance2 = new ServerInstance("localhost", 8081, 5);
        final var serverInstance3 = new ServerInstance("localhost", 8082, 5);

        final var selector = new RoundRobinServerSelector(Arrays.asList(serverInstance1, serverInstance2, serverInstance3));

        var count1 = new AtomicInteger(0);
        var count2 = new AtomicInteger(0);
        var count3 = new AtomicInteger(0);

        IntStream.range(0, 900).boxed().parallel().forEach(val -> {
            var server = selector.next();
            if (server == serverInstance1) count1.incrementAndGet();
            else if (server == serverInstance2) count2.incrementAndGet();
            else count3.incrementAndGet();
        });
        assertEquals(300, count1.get());
        assertEquals(300, count2.get());
        assertEquals(300, count3.get());
    }

    @Test
    public void testServerSelectorMultiThreadShouldWorksFineWhenUpdateHost() {
        final var serverInstance1 = new ServerInstance("localhost", 8080, 5);
        final var serverInstance2 = new ServerInstance("localhost", 8081, 5);
        final var serverInstance3 = new ServerInstance("localhost", 8082, 5);
        final var serverInstance4 = new ServerInstance("localhost", 8083, 5);

        final var selector = new RoundRobinServerSelector(Arrays.asList(serverInstance1, serverInstance2, serverInstance3));

        var count1 = new AtomicInteger(0);
        var count2 = new AtomicInteger(0);
        var count3 = new AtomicInteger(0);
        var count4 = new AtomicInteger(0);

        IntStream.range(0, 900).boxed().parallel().forEach(val -> {
            var server = selector.next();
            if (server == serverInstance1) count1.incrementAndGet();
            else if (server == serverInstance2) count2.incrementAndGet();
            else if (server == serverInstance3) count3.incrementAndGet();
            else count4.incrementAndGet();

            if (val == 600) {
                selector.updateCurrentServers(Arrays.asList(serverInstance1, serverInstance4));
            }
        });

        Assertions.assertTrue(count1.get() > count2.get());
        Assertions.assertTrue(count1.get() > count3.get());
        Assertions.assertTrue(count4.get() < count2.get());
        Assertions.assertTrue(count4.get() < count3.get());

    }
}
