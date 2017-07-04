package aeron;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.real_logic.aeron.driver.MediaDriver;
import uk.co.real_logic.aeron.logbuffer.FragmentHandler;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dharmeshsing on 7/08/15.
 */
public class AeronTest {

    private MediaDriver driver;

    @Before
    public void setup(){
        MediaDriver.loadPropertiesFile("mediaDriver.properties");
        driver = LowLatencyMediaDriver.startMediaDriver();
    }

    @After
    public void tearDown(){
        driver.close();
    }

    @Test
    public void testAeron() throws Exception{
        Thread pub = new Thread(()->{
            AeronPublisher aeronPublisher = new AeronPublisher(driver.aeronDirectoryName());
            aeronPublisher.addPublication("udp://localhost:40123", 10);

            UnsafeBuffer buffer = new UnsafeBuffer(ByteBuffer.allocateDirect(512));
            String message = "Hello World!";
            buffer.putBytes(0, message.getBytes());

            while(true) {
                aeronPublisher.send(buffer);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread sub = new Thread(()->{

            final FragmentHandler fragmentHandler =
                    (buffer, offset, length, header) ->
                    {
                        final byte[] data = new byte[length];
                        buffer.getBytes(offset, data);
                        System.out.println("Received message " + new String(data));

                    };

            AeronSubscriber aeronSubscriber = new AeronSubscriber(driver.aeronDirectoryName(),fragmentHandler);
            aeronSubscriber.addSubscriber("udp://localhost:40123", 10);
            aeronSubscriber.start();
        });
        pub.start();
        sub.start();

        Thread.sleep(10000);

        pub.interrupt();
        sub.interrupt();

        driver.close();

    }

    @Test
    public void testMulticastAeron() throws Exception{

        Thread pub = new Thread(()->{
            AeronPublisher aeronPublisher = new AeronPublisher(driver.aeronDirectoryName());
            aeronPublisher.addPublication("aeron:udp?endpoint=224.0.1.1:40456", 10);
            UnsafeBuffer buffer = new UnsafeBuffer(ByteBuffer.allocateDirect(512));

            int index = 0;
            while(true) {
                buffer.byteBuffer().clear();
                String message = "Hello World! " + index++;
                buffer.putBytes(0, message.getBytes());

                aeronPublisher.send(buffer);
                System.out.println("Sent ");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread sub1 = new Thread(()->{
            final FragmentHandler fragmentHandler =
                    (buffer, offset, length, header) ->
                    {
                        final byte[] data = new byte[length];
                        buffer.getBytes(offset, data);
                        System.out.println("Received message sub1 " + new String(data));

                    };

            AeronSubscriber aeronSubscriber = new AeronSubscriber(driver.aeronDirectoryName(),fragmentHandler);
            aeronSubscriber.addSubscriber("aeron:udp?endpoint=224.0.1.1:40456", 10);
            aeronSubscriber.start();
        });

        Thread sub2 = new Thread(()->{
            final FragmentHandler fragmentHandler =
                    (buffer, offset, length, header) ->
                    {
                        final byte[] data = new byte[length];
                        buffer.getBytes(offset, data);
                        System.out.println("Received message sub2 " + new String(data));

                    };

            AeronSubscriber aeronSubscriber = new AeronSubscriber(driver.aeronDirectoryName(),fragmentHandler);
            aeronSubscriber.addSubscriber("aeron:udp?endpoint=224.0.1.1:40456", 10);
            aeronSubscriber.start();
        });

        pub.start();
        sub1.start();
        sub2.start();

        Thread.sleep(10000);

        pub.interrupt();
        sub1.interrupt();
        sub2.interrupt();

        driver.close();

    }

    @Test
    public void testMulticastAeronDifferentDrivers() throws Exception{
        MediaDriver driver1 = LowLatencyMediaDriver.startMediaDriver();

        Thread pub = new Thread(()->{
            AeronPublisher aeronPublisher = new AeronPublisher(driver1.aeronDirectoryName());
            aeronPublisher.addPublication("aeron:udp?endpoint=224.0.1.1:40456", 10);
            UnsafeBuffer buffer = new UnsafeBuffer(ByteBuffer.allocateDirect(512));

            int index = 0;
            while(true) {
                buffer.byteBuffer().clear();
                String message = "Hello World! " + index++;
                buffer.putBytes(0, message.getBytes());

                aeronPublisher.send(buffer);
                System.out.println("Sent ");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        MediaDriver driver2 = LowLatencyMediaDriver.startMediaDriver();

        Thread sub1 = new Thread(()->{
            final FragmentHandler fragmentHandler =
                    (buffer, offset, length, header) ->
                    {
                        final byte[] data = new byte[length];
                        buffer.getBytes(offset, data);
                        System.out.println("Received message sub1 " + new String(data));

                    };

            AeronSubscriber aeronSubscriber = new AeronSubscriber(driver2.aeronDirectoryName(),fragmentHandler);
            aeronSubscriber.addSubscriber("aeron:udp?endpoint=224.0.1.1:40456", 10);
            aeronSubscriber.start();
        });

        MediaDriver driver3 = LowLatencyMediaDriver.startMediaDriver();

        Thread sub2 = new Thread(()->{
            final FragmentHandler fragmentHandler =
                    (buffer, offset, length, header) ->
                    {
                        final byte[] data = new byte[length];
                        buffer.getBytes(offset, data);
                        System.out.println("Received message sub2 " + new String(data));

                    };

            AeronSubscriber aeronSubscriber = new AeronSubscriber(driver3.aeronDirectoryName(),fragmentHandler);
            aeronSubscriber.addSubscriber("aeron:udp?endpoint=224.0.1.1:40456", 10);
            aeronSubscriber.start();
        });

        pub.start();
        sub1.start();
        sub2.start();

        Thread.sleep(10000);

        pub.interrupt();
        sub1.interrupt();
        sub2.interrupt();

        driver1.close();
        driver2.close();
        driver3.close();
    }


    @Test
    public void testMulticastAeronLateSubscriber() throws Exception{
        Thread pub = new Thread(()->{
            AeronPublisher aeronPublisher = new AeronPublisher(driver.aeronDirectoryName());
            aeronPublisher.addPublication("aeron:udp?endpoint=224.0.1.1:40456", 10);
            UnsafeBuffer buffer = new UnsafeBuffer(ByteBuffer.allocateDirect(512));

            int index = 0;
            while(true) {
                buffer.byteBuffer().clear();
                String message = "Hello World! " + index++;
                buffer.putBytes(0, message.getBytes());

                aeronPublisher.send(buffer);
                System.out.println("Sent ");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread sub1 = new Thread(()->{
            final FragmentHandler fragmentHandler =
                    (buffer, offset, length, header) ->
                    {
                        final byte[] data = new byte[length];
                        buffer.getBytes(offset, data);
                        System.out.println("Received message sub1 " + new String(data));

                    };

            AeronSubscriber aeronSubscriber = new AeronSubscriber(driver.aeronDirectoryName(),fragmentHandler);
            aeronSubscriber.addSubscriber("aeron:udp?endpoint=224.0.1.1:40456", 10);
            aeronSubscriber.start();
        });
        pub.start();
        sub1.start();
        System.out.println("Subscriber 1 started");

        Thread.sleep(10000);

        Thread sub2 = new Thread(()->{
            final FragmentHandler fragmentHandler =
                    (buffer, offset, length, header) ->
                    {
                        final byte[] data = new byte[length];
                        buffer.getBytes(offset, data);
                        System.out.println("Received message sub2 " + new String(data));

                    };

            AeronSubscriber aeronSubscriber = new AeronSubscriber(driver.aeronDirectoryName(),fragmentHandler);
            aeronSubscriber.addSubscriber("aeron:udp?endpoint=224.0.1.1:40456", 10);
            aeronSubscriber.start();
        });


        sub2.start();
        System.out.println("Subscriber 2 started");

        Thread.sleep(10000);

        pub.interrupt();
        sub1.interrupt();
        sub2.interrupt();

        driver.close();

    }

    @Test
    public void testAeronMutiplePublishers() throws Exception{

        List<Thread> pubs = new ArrayList<>();
        for(int i=0; i<10; i++) {
            String message = "Hello World! " + i;
            String dir = "/tmp/pub/" + i;
            pubs.add(new Thread(() -> {
                LowLatencyMediaDriver.startMediaDriver(dir);
                AeronPublisher aeronPublisher = new AeronPublisher(dir);
                aeronPublisher.addPublication("udp://localhost:40123", 10);

                UnsafeBuffer buffer = new UnsafeBuffer(ByteBuffer.allocateDirect(512));
                buffer.putBytes(0, message.getBytes());

                while (true) {
                    aeronPublisher.send(buffer);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }));
        }

        Thread sub = new Thread(()->{
            MediaDriver driver = LowLatencyMediaDriver.startMediaDriver("/tmp/pub/sub");

            final FragmentHandler fragmentHandler =
                    (buffer, offset, length, header) ->
                    {
                        final byte[] data = new byte[length];
                        buffer.getBytes(offset, data);
                        System.out.println("Received message " + new String(data));

                    };

            AeronSubscriber aeronSubscriber = new AeronSubscriber(driver.aeronDirectoryName(),fragmentHandler);
            aeronSubscriber.addSubscriber("udp://localhost:40123", 10);
            aeronSubscriber.start();
        });

        sub.start();

        Thread.sleep(100000);

        for(Thread pub : pubs){
            pub.start();
        }

        Thread.sleep(100000);

        for(Thread pub : pubs){
            pub.interrupt();
        }

        sub.interrupt();


    }

}