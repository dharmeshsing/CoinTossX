package hawkes;

import com.carrotsearch.hppc.DoubleArrayList;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;

/**
 * Created by dharmeshsing on 18/02/16.
 */
public class HawkesFunctionsTest {

    @Test
    public void testSimulateHawkes() {
        DoubleArrayList values = HawkesFunctions.simulateHawkes(1.2, 0.6, 0.8, 100);
        assertNotNull("Hawkes values are null", values);
    }

    @Test
    public void testConvertToDateTime() {
        DoubleArrayList values = HawkesFunctions.simulateHawkes(1.2, 0.6, 0.8, 100);
        values.trimToSize();;
        HawkesFunctions.convertToDateTime(values.buffer, LocalTime.now() );
    }

    @Test
    public void testLineSpace() {
        double[] times = HawkesFunctions.linspace(0, 100, 100);
        assertNotNull("Lines space values are null", times);
    }

    @Test
    public void testCalculateIntensity() throws Exception{
        double mu = 1.2;
        double alpha = 0.6;
        double beta = 0.8;
        int horizon = 100;
        DoubleArrayList values = HawkesFunctions.simulateHawkes(mu, alpha, beta, horizon);
        values.trimToSize();

        double[] times = HawkesFunctions.linspace(0, horizon, 100);
        double[] intensity = HawkesFunctions.calculateIntensity(times,values.buffer,mu,alpha,beta);

        assertNotNull("Intensity values are null", intensity);
    }

    @Test
    public void test_8_hour_day_times() throws IOException {
        Properties hawkesProps = new Properties();
        File dataFile = new File("/Users/dharmeshsing/Documents/Masters/Software/data/hawkesData.properties");
        try {
            loadProperties(hawkesProps,dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        try {
            Files.delete(Paths.get("/Users/dharmeshsing/Documents/Masters/Software/test.txt"));
        }catch(Exception e){
            e.printStackTrace();
        }

        double[] lambda0 = convertLambda((String)hawkesProps.get("LAMBDA"));
        double[][] alpha = convertAlpha((String)hawkesProps.get("ALPHA"));
        double[] beta = convertBeta((String)hawkesProps.get("BETA"));
        int horizon = Integer.parseInt((String)hawkesProps.get("HORIZON"));

        List<List<Double>> values = HawkesFunctions.simulateHawkes(lambda0, alpha, beta, horizon);
        System.gc();
        List<HawkesData>  hawkesDataList =  HawkesFunctions.getSortedHawkesData(values);
        System.gc();
        LocalDateTime localDateTime = LocalDateTime.of(2016,9,16,7,0,0);
        sb.append(localDateTime.toString() + "\n");
        for(int i=0; i<hawkesDataList.size(); i++) {
            HawkesData hd = hawkesDataList.get(i);
            long start = System.nanoTime();
            long delay1 = hd.getDelay();
            long duration = 0;

            while(duration < delay1){
                duration = System.nanoTime() - start;
            }
            if(i != 0) {
               // long delay = hd.getDelay() - hawkesDataList.get(i-1).getDelay();
                localDateTime = localDateTime.plus(duration, ChronoUnit.NANOS);
                //System.out.println(localDateTime.toString());
                //sb.append(localDateTime.toString() + "\n");
            }
        }
        System.out.println(localDateTime);

        Files.write(Paths.get("/Users/dharmeshsing/Documents/Masters/Software/test.txt"),sb.toString().getBytes());


    }

    private double[] convertLambda(String lambda){
        String[] lambda_arr = lambda.split(",");
        double[] values = new double[lambda_arr.length];
        for(int i=0; i<lambda_arr.length; i++){
            values[i] = Double.valueOf(lambda_arr[i]).doubleValue();
        }

        return values;
    }

    private double[][] convertAlpha(String alphaText){
        String[] alpha_arr = alphaText.split(",");
        double[][] alpha = new double[8][8];
        for (int r = 0; r < 8; r++) {
            int index = 0;
            for (int c = 0; c < 8; c++) {
                alpha[r][c] = Double.parseDouble(alpha_arr[index++]);
            }
        }

        return alpha;
    }

    private double[] convertBeta(String betaText){
        String[] beta_arr = betaText.split(",");
        double[] beta = new double[beta_arr.length];
        for (int r = 0; r < beta_arr.length; r++) {
            beta[r] = Double.parseDouble(beta_arr[r]);
        }

        return beta;
    }

    private void loadProperties(Properties properties,File propertiesFile) throws IOException {
        try (InputStream inputStream = new FileInputStream(propertiesFile)) {

            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new IOException("Unable to load properties file " + propertiesFile);
            }
        }
    }
}