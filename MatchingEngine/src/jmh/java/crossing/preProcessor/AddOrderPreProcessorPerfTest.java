package crossing.preProcessor;

import common.OrderType;
import crossing.preProcessor.MatchingPreProcessor.MATCHING_ACTION;
import crossing.strategy.MatchingLogic;
import orderBook.OrderBook;
import org.openjdk.jmh.annotations.*;

/**
 * Created by dharmeshsing on 9/07/15.
 */
@State(Scope.Thread)
public class AddOrderPreProcessorPerfTest {
    private AddOrderPreProcessor addOrderPreProcessor;

    @Setup(Level.Trial)
    public void setUp(){
        addOrderPreProcessor = new AddOrderPreProcessor();
    }


    @Benchmark
    public MATCHING_ACTION testAccept() {
        return addOrderPreProcessor.preProcess(OrderType.LIMIT, OrderBook.SIDE.BID, 0, 0, -1, -1,-1,-1, 100);
    }

}
