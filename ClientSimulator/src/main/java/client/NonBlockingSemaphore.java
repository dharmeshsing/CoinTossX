package client;

import java.util.concurrent.atomic.AtomicInteger;

/** Code from Peter Lawrey.
 *  http://stackoverflow.com/questions/12597407/non-reentrant-non-blocking-semaphore-in-java
 */
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
