package aeron;

import uk.co.real_logic.aeron.driver.MediaDriver;
import uk.co.real_logic.aeron.driver.ThreadingMode;
import uk.co.real_logic.agrona.concurrent.BackoffIdleStrategy;
import uk.co.real_logic.agrona.concurrent.BusySpinIdleStrategy;
import uk.co.real_logic.agrona.concurrent.SigIntBarrier;

/**
 * Created by dharmeshsing on 7/08/15.
 */
public class LowLatencyMediaDriver {
    public static void main(final String[] args) throws Exception {
        MediaDriver.loadPropertiesFile("mediaDriver.properties");

        MediaDriver.Context ctx = new MediaDriver.Context()
                .threadingMode(ThreadingMode.SHARED)
//                .conductorIdleStrategy(new BackoffIdleStrategy(1, 1, 1, 1))
//                .receiverIdleStrategy(new BusySpinIdleStrategy())
//                .senderIdleStrategy(new BusySpinIdleStrategy())
                .aeronDirectoryName(args[0]);

        ctx.driverTimeoutMs(1000000);

        try (final MediaDriver ignored = MediaDriver.launch(ctx)) {
            new SigIntBarrier().await();
            System.out.println("Shutdown Driver...");
        }
    }

    public static MediaDriver.Context getLowLatencyMediaDriver(String dirName){
        MediaDriver.Context ctx = new MediaDriver.Context()
                .threadingMode(ThreadingMode.DEDICATED)
                .conductorIdleStrategy(new BackoffIdleStrategy(1, 1, 1, 1))
                .receiverIdleStrategy(new BusySpinIdleStrategy())
                .senderIdleStrategy(new BusySpinIdleStrategy())
                .aeronDirectoryName(dirName);

        ctx.driverTimeoutMs(1000000);

        return ctx;

    }

    public static MediaDriver startMediaDriver(String dirName){
        MediaDriver.Context ctx = new MediaDriver.Context()
                .threadingMode(ThreadingMode.DEDICATED)
                .conductorIdleStrategy(new BackoffIdleStrategy(1, 1, 1, 1))
                .receiverIdleStrategy(new BusySpinIdleStrategy())
                .senderIdleStrategy(new BusySpinIdleStrategy())
                .aeronDirectoryName(dirName);

        ctx.driverTimeoutMs(1000000);

       return MediaDriver.launchEmbedded(ctx);
    }

    public static MediaDriver startMediaDriver(){
        MediaDriver.loadPropertiesFile("mediaDriver.properties");
        MediaDriver.Context ctx = new MediaDriver.Context()
                .threadingMode(ThreadingMode.SHARED);
//                .conductorIdleStrategy(new BackoffIdleStrategy(1, 1, 1, 1))
//                .receiverIdleStrategy(new BusySpinIdleStrategy())
//                .senderIdleStrategy(new BusySpinIdleStrategy());

        ctx.driverTimeoutMs(1000000);

        return MediaDriver.launchEmbedded(ctx);
    }
}
