package mipt.hw;

import mipt.hw.Ship.ShipCapacity;
import mipt.hw.Ship.ShipGenerator;
import mipt.hw.Ship.ShipType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Observer {
    Observer() {
        init();
    }

    public void init() {
        executorPool = Executors.newCachedThreadPool();
        shipGenerator = new ShipGenerator(executorPool);
        tunnel = new Tunnel();

        piers = new HashMap<>();
        shipsCount = new HashMap<>();
        shipsNumCounter = 1;
    }

    public void setPriers(long breadShips, long bananaShips, long clothesShips) {
        // Create.
        addPier(ShipType.BREAD, breadShips);
        addPier(ShipType.BANANA, bananaShips);
        addPier(ShipType.CLOTHES, clothesShips);

        // Launch.
        for (Pier pier: piers.values()) {
            executorPool.execute(pier);
        }
    }

    private void addPier(ShipType type, long shipsCount) {
        if (shipsCount != 0) {
            piers.put(type, new Pier(type, shipsCount));
        }
    }

    public void LaunchShips(ShipType type, ShipCapacity capacity, long count) {
        for (long i = 0; i < count; ++i) {
            shipGenerator.CreateShip(type, capacity, piers.get(type), shipsNumCounter, tunnel);
            ++shipsNumCounter;
        }
    }

    public void awaitProcess() {
        long timeout = 100;
        executorPool.shutdown();
        try {
            boolean terminated = executorPool.awaitTermination(timeout, TimeUnit.SECONDS);
            if (!terminated) {
                throw new RuntimeException("timeout in awaitProcess (" + timeout + " seconds)");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    ExecutorService executorPool;
    ShipGenerator shipGenerator;
    Tunnel tunnel = new Tunnel();

    // Reused parameters.
    Map<ShipType, Pier> piers = null;
    Map<ShipType, Map<ShipCapacity, Long>> shipsCount = null;
    long shipsNumCounter = 1;
}
