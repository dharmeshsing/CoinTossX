package hawkes;

import py4j.GatewayServer;
import util.CommonUtil;

import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 * Created by dharmeshsing on 20/01/17.
 */
public class HawkesEntryPoint {

    private double[] times;
    private double[] intensity1;
    private double[] intensity2;
    private double[] intensity3;
    private double[] intensity4;
    private double[] intensity5;
    private double[] intensity6;
    private double[] intensity7;
    private double[] intensity8;

    public HawkesEntryPoint(){
        init();
    }

    private void init(){
        try {

            Properties hawkesProps = new Properties();
            String dataPath = getClass().getClassLoader().getResource("hawkesData.properties").getPath();
            File dataFile = new File(dataPath);
            CommonUtil.loadProperties(hawkesProps, dataFile);

            double[] lambda0 = convertLambda((String) hawkesProps.get("LAMBDA"));
            double[][] alpha = convertAlpha((String) hawkesProps.get("ALPHA"));
            double[] beta = convertBeta((String) hawkesProps.get("BETA"));
            double[][] beta2 = convertBeta2DArray((String) hawkesProps.get("BETA"));
            int horizon = Integer.parseInt((String)hawkesProps.get("HORIZON"));

            List<List<Double>> values = HawkesFunctions.simulateHawkes(lambda0, alpha, beta, horizon);

            this.times = HawkesFunctions.linspace(0, 10, horizon);
            this.intensity1 = HawkesFunctions.calculateIntensity(0, times, HawkesFunctions.getHistory(0, values), lambda0, alpha, beta2);
            this.intensity2 = HawkesFunctions.calculateIntensity(1, times, HawkesFunctions.getHistory(1, values), lambda0, alpha, beta2);
            this.intensity3 = HawkesFunctions.calculateIntensity(2, times, HawkesFunctions.getHistory(2, values), lambda0, alpha, beta2);
            this.intensity4 = HawkesFunctions.calculateIntensity(3, times, HawkesFunctions.getHistory(3, values), lambda0, alpha, beta2);
            this.intensity5 = HawkesFunctions.calculateIntensity(4, times, HawkesFunctions.getHistory(4, values), lambda0, alpha, beta2);
            this.intensity6 = HawkesFunctions.calculateIntensity(5, times, HawkesFunctions.getHistory(5, values), lambda0, alpha, beta2);
            this.intensity7 = HawkesFunctions.calculateIntensity(6, times, HawkesFunctions.getHistory(6, values), lambda0, alpha, beta2);
            this.intensity8 = HawkesFunctions.calculateIntensity(7, times, HawkesFunctions.getHistory(7, values), lambda0, alpha, beta2);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GatewayServer gatewayServer = new GatewayServer(new HawkesEntryPoint());
        gatewayServer.start();
        System.out.println("Gateway Server Started");
    }

    public double[] getTimes() {
        return times;
    }

    public double[] getIntensity1() {
        return intensity1;
    }

    public double[] getIntensity2() {
        return intensity2;
    }

    public double[] getIntensity3() {
        return intensity3;
    }

    public double[] getIntensity4() {
        return intensity4;
    }

    public double[] getIntensity5() {
        return intensity5;
    }

    public double[] getIntensity6() {
        return intensity6;
    }

    public double[] getIntensity7() {
        return intensity7;
    }

    public double[] getIntensity8() {
        return intensity8;
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
}
