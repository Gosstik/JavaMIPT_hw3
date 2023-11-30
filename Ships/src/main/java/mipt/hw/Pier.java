package mipt.hw;

import mipt.hw.Ship.Ship;
import mipt.hw.Ship.ShipType;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Pier implements Runnable {
    public Pier(ShipType type, long leftShipsToHandle) {
        this.type = type;
        this.leftShipsToHandle = leftShipsToHandle;
    }

    public ShipType getType() {
        return type;
    }

    public long getLeftShipsToHandle() {
        return leftShipsToHandle;
    }

    @Override
    public void run() {
        while (leftShipsToHandle != 0) {
            waitWhileQueueEmpty();
            handleQueue();
        }
    }

    public synchronized void addShip(Ship ship) {
        queue.add(ship);
        notify();
    }

    private synchronized void waitWhileQueueEmpty() {
        while (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleQueue() {
        while (!queue.isEmpty()) {
            Ship ship = queue.poll();
            loadShip(ship);
            --leftShipsToHandle;
            ship.loadedLog(leftShipsToHandle);
        }
    }

    private void loadShip(Ship ship) {
        long timeToLoadMill = ship.getCapacity().getValue() * 100;
        try {
            Thread.sleep(timeToLoadMill);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private long leftShipsToHandle;
    private final BlockingQueue<Ship> queue = new LinkedBlockingQueue<>();
    private final ShipType type;
}
