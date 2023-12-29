package mipt.hw.Ship;

import mipt.hw.Pier;
import mipt.hw.Tunnel;

import java.util.concurrent.ExecutorService;

public class ShipGenerator {
    private final ExecutorService pool;

    public ShipGenerator(ExecutorService pool) {
        this.pool = pool;
    }

    public void CreateShip(ShipType type, ShipCapacity capacity, Pier pier, long num, Tunnel tunnel) {
        Ship ship = new Ship(type, capacity, tunnel, pier, num);
        ship.createdLog();
        pool.execute(ship);
    }
}
