package mipt.hw;

import java.util.concurrent.locks.Lock;

public class LockGuard implements AutoCloseable {
    public LockGuard(Lock lock) {
        this.lock = lock;
        this.lock.lock();
    }

    @Override
    public void close() {
        lock.unlock();
    }

    private final Lock lock;
}
