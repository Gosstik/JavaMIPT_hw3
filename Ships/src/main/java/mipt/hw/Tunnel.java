package mipt.hw;

import mipt.hw.Ship.Ship;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Tunnel {
    public void Accept(Ship ship) {
        GetLock();
        WorkInTunnel(ship);
        PutLock();
    }

    private void GetLock() {
        try(LockGuard fullLockGuard = new LockGuard(fullLock)) {
            while (shipsCount == 0) {
                isFull.await();
            }
        } catch (InterruptedException e) {
            InterruptedExceptionHandler.handle();
        }
        --shipsCount;
    }

    private void WorkInTunnel(Ship ship) {
        try {
            ship.enterTunnelLog();
            Thread.sleep(SLEEP_MILLIS);
            ship.exitTunnelLog();
        } catch (InterruptedException e) {
            InterruptedExceptionHandler.handle();
        }
    }

    private void PutLock() {
        fullLock.lock();
        shipsCount += 1;
        isFull.signal();
        fullLock.unlock();
    }

    static private final long SLEEP_MILLIS = 1000;

    private int shipsCount = 5;

    private final ReentrantLock fullLock = new ReentrantLock();
    private final Condition isFull = fullLock.newCondition();
}
