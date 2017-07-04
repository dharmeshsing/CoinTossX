package hawkes;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;

/**
 * Created by dharmeshsing on 6/06/16.
 */
public class PriceTest {

    private Random random = new Random();

    @Test
    public void testPrice(){
        long bid = 0;
        long offer = 25057;
        long LOWEST_BID = 25000;
        long STARTING_BID = 25034;
        DoublePredicate pricePredicate = x -> x > bid && x <= offer && x >= LOWEST_BID;
        long meanPrice = bid != 0 ? bid : offer != 0 ? offer : STARTING_BID;
        long price = getPrice(meanPrice, pricePredicate);
        System.out.println(price);
    }

    public long getPrice(double price,DoublePredicate predicate){
        NormalDistribution priceND = new NormalDistribution(price, 200);
        double[] prices = priceND.sample(200000);
        prices = Arrays.stream(prices).filter(predicate).toArray();
        if(prices.length > 0) {
            int index = random.ints(1, 0, prices.length).findAny().getAsInt();
            return Double.valueOf(prices[index]).longValue();
        }

        System.out.println("Returning default");
        return  Double.valueOf(price).longValue();
    }

    @Test
    public void testVolume(){
        IntPredicate predicate = x -> x >= 0 && (x % 100 == 0);
        int vol = getVolume(200,predicate);
        System.out.println(vol);
    }

    private int getVolume(int volume,IntPredicate predicate){
        NormalDistribution volumenND = new NormalDistribution(volume, 1000);
        int[] volumes = Arrays.stream(volumenND.sample(200000)).mapToInt(e -> (int)Math.ceil(e)).filter(predicate).toArray();
        if(volumes.length > 0) {
            int index = random.ints(1, 0, volumes.length).findAny().getAsInt();
            return volumes[index];
        }

        return volume;
    }
}
