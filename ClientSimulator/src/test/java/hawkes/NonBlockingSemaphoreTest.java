package hawkes;

import org.junit.Test;

/**
 * Created by dharmeshsing on 1/01/17.
 */
public class NonBlockingSemaphoreTest {

    @Test
    public void testAcquire() throws Exception {
        NonBlockingSemaphore semaphore = new NonBlockingSemaphore(1);
        long startTime = System.currentTimeMillis();
        semaphore.acquire();
        while(!semaphore.acquire()){
            if(System.currentTimeMillis() - startTime > 10000){
                System.out.println("Timed Out");
                break;
            }
        }
    }
}