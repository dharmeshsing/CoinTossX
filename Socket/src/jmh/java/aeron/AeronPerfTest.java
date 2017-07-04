package aeron;

import org.openjdk.jmh.annotations.*;
import uk.co.real_logic.aeron.driver.MediaDriver;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

/**
 * Created by dharmeshsing on 1/09/15.
 */
@State(Scope.Thread)
public class AeronPerfTest {
    private MediaDriver driver;
    private AeronPublisher aeronPublisher;
    private AeronPublisher multiCastPublisher;
    private UnsafeBuffer buffer;


    @Setup(Level.Trial)
    public void setup(){
        driver = MediaDriver.launchEmbedded();
        aeronPublisher = new AeronPublisher(driver.aeronDirectoryName());
        aeronPublisher.addPublication("udp://localhost:40123", 10);

        multiCastPublisher = new AeronPublisher(driver.aeronDirectoryName());
        multiCastPublisher.addPublication("aeron:udp?endpoint=224.0.1.1:40456", 10);

        buffer = new UnsafeBuffer(ByteBuffer.allocateDirect(12));
        String message = "Hello World!";
        buffer.putBytes(0, message.getBytes());
    }

    @TearDown
    public void tearDown(){
        aeronPublisher.stop();
        driver.close();
    }

    @Benchmark
    public long testOnePublisher() throws Exception {
        return aeronPublisher.send(buffer,"udp://localhost:40123", 10);
    }

    @Benchmark
    public long testMulticastPublisher() throws Exception {
        return multiCastPublisher.send(buffer,"aeron:udp?endpoint=224.0.1.1:40456", 10);
    }
}