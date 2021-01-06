package hawkes;

import com.carrotsearch.hppc.IntObjectMap;

import client.ClientData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Created by dharmeshsing on 5/03/16.
 */
public class HawkesSimulation {

    private IntObjectMap<ClientData> clientData;
    private Properties properties = new Properties();
    private static final String PROPERTIES_FILE =  "simulation.properties";
    private String dataPath;

    public HawkesSimulation() throws Exception {
        loadProperties(properties,PROPERTIES_FILE);
        dataPath = properties.get("DATA_PATH").toString();
        clientData = ClientData.loadClientDataData(dataPath);
    }

    private List<HawkesData>  simulateHawkesTimes() throws Exception {
        Properties hawkesProps = new Properties();
        File dataFile = new File(dataPath + File.separator + "hawkesData.properties");
        loadProperties(hawkesProps,dataFile);

        double[] lambda0 = convertLambda((String)hawkesProps.get("LAMBDA"));
        double[][] alpha = convertAlpha((String)hawkesProps.get("ALPHA"));
        double[] beta = convertBeta((String)hawkesProps.get("BETA"));
        int horizon = Integer.parseInt((String)hawkesProps.get("HORIZON"));

        List<List<Double>> values = HawkesFunctions.simulateHawkes(lambda0, alpha, beta, horizon);
        return HawkesFunctions.getSortedHawkesData(values);
    }

    private List<HawkesData>  warmupHawkesTimes() throws Exception {
        Properties hawkesProps = new Properties();
        File dataFile = new File(dataPath + File.separator + "hawkesData.properties");
        loadProperties(hawkesProps,dataFile);

        double[] lambda0 = convertLambda((String)hawkesProps.get("LAMBDA"));
        double[][] alpha = convertAlpha((String)hawkesProps.get("ALPHA"));
        double[] beta = convertBeta((String)hawkesProps.get("BETA"));
        int horizon = 10;

        List<List<Double>> values = HawkesFunctions.simulateHawkes(lambda0, alpha, beta, horizon);
        return HawkesFunctions.getSortedHawkesData(values);
    }

    public double[] convertLambda(String lambda){
        String[] lambda_arr = lambda.split(",");
        double[] values = new double[lambda_arr.length];
        for(int i=0; i<lambda_arr.length; i++){
            values[i] = Double.valueOf(lambda_arr[i]).doubleValue();
        }

        return values;
    }

    public double[][] convertAlpha(String alphaText){
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

    public double[] convertBeta(String betaText){
        String[] beta_arr = betaText.split(",");
        double[] beta = new double[beta_arr.length];
        for (int r = 0; r < beta_arr.length; r++) {
            beta[r] = Double.parseDouble(beta_arr[r]);
        }

        return beta;
    }

    public double[][] convertBeta2DArray(String betaText){
        String[] beta_arr = betaText.split(",");
        double[][] beta = new double[8][8];
        for (int r = 0; r < 8; r++) {
            int index = 0;
            for (int c = 0; c < 8; c++) {
                beta[r][c] = Double.parseDouble(beta_arr[index++]);
            }
        }
        return beta;
    }

    public void process(int clientId,int securityId,boolean isWarmup) {
        try {
            List<HawkesData> times;
            if(isWarmup){
                System.out.println("Warming Up");
                times = warmupHawkesTimes();
            }else{
                System.out.println("Starting Simulation");
                times = simulateHawkesTimes();
            }

            Client client = new Client(clientData.get(clientId), times,securityId);
            client.init(properties);
            client.process();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void loadProperties(Properties properties,String propertiesFile) throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFile)) {

            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new IOException("Unable to load properties file " + propertiesFile);
            }
        }
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

    public static void main(String[] args) throws Exception {
        HawkesSimulation hawkesSimulation = new HawkesSimulation();
        int clientId = Integer.parseInt(args[0]);
        int securityId = Integer.parseInt(args[1]);

        String warmUp = null;
        if(args.length > 2) {
            warmUp = args[2];
        }

        boolean isWarmup = false;
        if(warmUp !=null && warmUp.equals("warmUp")){
            isWarmup = true;
        }

        hawkesSimulation.process(clientId, securityId,isWarmup);

    }
}
