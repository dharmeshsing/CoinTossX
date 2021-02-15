package client;

import java.util.concurrent.atomic.AtomicInteger;

public class NonBlockingSemaphore {

    private final AtomicInteger counter;

    public NonBlockingSemaphore(int permits) {
        counter = new AtomicInteger(permits);
    }

    public boolean acquire() {
        while(true) {
            int count = counter.get();
            if (count <= 0) return false;
            if (counter.compareAndSet(count, count - 1))
                return true;
        }
    }

    public void release() {
        counter.incrementAndGet();
    }
}
