package mipt.hw.Ship;

import mipt.hw.CustomLogger;
import mipt.hw.Pier;
import mipt.hw.Tunnel;

public class Ship implements Runnable {
    public Ship(ShipType type, ShipCapacity capacity, Tunnel tunnel, Pier pier, long num) {
        this.type = type;
        this.capacity = capacity;
        this.tunnel = tunnel;
        this.pier = pier;
        this.num = num;
    }

    @Override
    public void run() {
        tunnel.Accept(this);
        pier.addShip(this);
    }

    public ShipCapacity getCapacity() {
        return capacity;
    }

    public void createdLog() {
        CustomLogger.info("Ship " + num + ": created (type = " + pier.getType() + ", capacity = " + capacity + ").");
    }

    public void enterTunnelLog() {
        CustomLogger.info("Ship " + num + ": entered the tunnel.");
    }

    public void exitTunnelLog() {
        CustomLogger.info("Ship " + num + ": exited the tunnel.");
    }

    public void loadedLog(long leftShipsToHandle) {
        CustomLogger.info("Ship " + num + ": loaded. " + type + " ships left: " + leftShipsToHandle + ".");
    }

    private final ShipType type;
    private final ShipCapacity capacity;
    private final Tunnel tunnel;
    private final Pier pier;
    private final long num;
}
