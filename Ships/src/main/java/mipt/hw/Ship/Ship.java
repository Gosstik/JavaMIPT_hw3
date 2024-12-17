package mipt.hw.Ship;

import mipt.hw.CommonLogger;
import mipt.hw.Pier;
import mipt.hw.Tunnel;

public class Ship implements Runnable {
    public Ship(ShipType type, ShipCapacity capacity, Tunnel tunnel, Pier pier, long shipId) {
        this.type = type;
        this.capacity = capacity;
        this.tunnel = tunnel;
        this.pier = pier;
        id = shipId;
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
        CommonLogger.info("""
        Ship created.
        ShipId: %d
        type: %s
        capacity: %s
        """.formatted(id, type, capacity));
    }

    public void enterTunnelLog() {
        CommonLogger.info("""
        Ship entered the tunnel.
        ShipId: %d
        """.formatted(id));
    }

    public void exitTunnelLog() {
        CommonLogger.info("""
        Ship exited the tunnel.
        ShipId: %d
        """.formatted(id));
    }

    public void startLoadingLog() {
        CommonLogger.info("""
        Loading on the ship started.
        ShipId: %d
        type: %s
        capacity: %s
        """.formatted(id, type, capacity));
    }

    public void finishLoadingLog(long leftShipsToHandle) {
        CommonLogger.info("""
        Loading on the ship finished.
        ShipId: %d
        type: %s
        ships of same type left: %d
        """.formatted(id, type, leftShipsToHandle));
    }

    private final ShipType type;
    private final ShipCapacity capacity;
    private final Tunnel tunnel;
    private final Pier pier;
    private final long id;
}
