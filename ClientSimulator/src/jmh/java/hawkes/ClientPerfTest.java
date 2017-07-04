package hawkes;

import com.carrotsearch.hppc.DoubleArrayList;
import com.carrotsearch.hppc.IntArrayList;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.openjdk.jmh.annotations.*;

import java.util.Random;

/**
 * Created by dharmeshsing on 12/11/16.
 */
@State(Scope.Thread)
public class ClientPerfTest {

    private Random random;
    private IntArrayList volumeArr;
    private DoubleArrayList samples;
    private RandomDataGenerator generator = new RandomDataGenerator(new Well19937c());

    @Setup(Level.Trial)
    public void setUp(){
        random = new Random();
        volumeArr = new IntArrayList();
        samples = new DoubleArrayList();
    }

    @Benchmark
    public long testGetVolume() {
        long volume=0;
        getVolumeSamples(2000);
        if(volumeArr.size() > 0) {
            int index = random.ints(1, 0, volumeArr.size()).findAny().getAsInt();
            return volumeArr.get(index);
        }

        return volume;
    }

    public void getVolumeSamples(long offerQuantity){
        generateSamples(100000);
        int size = samples.size();
        volumeArr.clear();
        for(int i=0; i<size; i++){
            int value = (int)Math.ceil(samples.get(i));
            if(value >=offerQuantity && (value % 100 == 0)){
                volumeArr.add(value);
            }
        }
    }

    public void generateSamples(int sampleSize){
        samples.clear();
        samples.trimToSize();

        for (int i = 0; i < sampleSize; i++) {
            samples.add(generator.nextGaussian(1000,1));
        }
    }



}
