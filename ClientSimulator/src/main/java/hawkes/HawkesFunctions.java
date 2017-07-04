package hawkes;

import cern.colt.matrix.tdouble.DoubleFactory2D;
import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.algo.decomposition.DenseDoubleEigenvalueDecomposition;
import com.carrotsearch.hppc.DoubleArrayList;
import org.apache.commons.math3.distribution.UniformRealDistribution;

import java.time.LocalTime;
import java.util.*;


/**
 * Created by dharmeshsing on 18/02/16.
 */
public class HawkesFunctions {

    private static UniformRealDistribution urd = new UniformRealDistribution();

    public static DoubleArrayList simulateHawkes(double lambda0, double alpha, double beta, double horizon) {

        if (beta < alpha) {
            throw new RuntimeException("Unstable. You must have alpha < beta");
        }

        DoubleArrayList history = new DoubleArrayList();
        double lambda_star = lambda0;
        double dlambda;
        double t;

        //first event
        double U = urd.sample();
        double s = -(1.0 / lambda_star) * Math.log(U);
        if (s <= horizon) {
            history.add(s);
            dlambda = alpha;
            t = s;
        } else {
            return history;
        }

        //general routine
        while (true) {
            lambda_star = lambda0 + dlambda * Math.exp(-beta * (s - t));
            U = urd.sample();
            s = s - (1.0 / lambda_star) * Math.log(U);
            if (s > horizon) {
                return (history);
            }
            double D = urd.sample();
            if (D <= (lambda0 + dlambda * Math.exp(-beta * (s - t))) / lambda_star) {
                history.add(s);
                dlambda = dlambda * Math.exp(-beta * (s - t)) + alpha;
                t = s;
            }
        }
    }

    public static List<List<Double>> simulateHawkes(double[] lambda0, double[][] alpha, double[]beta, double horizon) {
        int dimension = lambda0.length;
        List<List<Double>> history = new ArrayList<>();

        for (int i = 0; i < dimension; i++) {
            List a = new ArrayList<Double>();
            history.add(a);
        }

        double[][] dlambda = new double[dimension][dimension];
        double[] m_lambda0 = lambda0.clone();
        double[] m_lambda = new double[dimension];

        checkStability(beta, alpha);

        double lambda_star = 0.0;

        double t;
        for (int i = 0; i < dimension; i++) {
            lambda_star += m_lambda0[i];
            m_lambda[i] = m_lambda0[i];
        }

        //first event
        double U = urd.sample();
        double s = -(1.0 / lambda_star) * Math.log(U);


        if (s <= horizon) {
            double D = urd.sample();
            int n0 = attribute(D, lambda_star, m_lambda);
            history.get(n0).add(s);

            for (int i = 0; i < dimension; i++) {
                dlambda[i][n0] = alpha[i][n0];
                m_lambda[i] = m_lambda0[i] + alpha[i][n0];
            }
        } else {
            return (history);
        }

        t = s;

        //general routine
        lambda_star = 0;
        for (int i = 0; i < dimension; i++) {
            lambda_star = lambda_star + m_lambda[i];
        }
        while (true) {
            U = urd.sample();
            s = s - (1.0 / lambda_star) * Math.log(U);
            if (s <= horizon) {
                double D = urd.sample();
                double I_M = 0.0;
                for (int i = 0; i < dimension; i++) {
                    double dl = 0.0;
                    for (int j = 0; j < dimension; j++) {
                        dl += dlambda[i][j] * Math.exp(-beta[i] * (s - t));
                    }
                    m_lambda[i] = m_lambda0[i] + dl;
                    I_M = I_M + m_lambda[i];
                }
                if (D <= (I_M / lambda_star)) {
                    int n0 = attribute(D, lambda_star, m_lambda);
                    history.get(n0).add(s);
                    lambda_star = 0.0;
                    for (int i = 0; i < dimension; i++) {
                        double dl = 0.0;
                        for (int j = 0; j < dimension; j++) {
                            dlambda[i][j] = dlambda[i][j] * Math.exp(-beta[i] * (s - t));
                            if (n0 == j) {
                                dlambda[i][n0] += alpha[i][n0];
                            }
                            dl += dlambda[i][j];
                        }
                        lambda_star += m_lambda0[i] + dl;
                    }
                    t = s;
                } else {
                    lambda_star = I_M;
                }

            } else {
                return (history);
            }
        }

    }

    public static void checkStability(double[] beta, double[][] alpha){

        //Diagnol
        int size = alpha.length;
        double[][] beta_diag = new double[size][size];

        for(int i=0; i< size; i++){
            beta_diag[i][i] = beta[i];
        }

        double[][] beta_minus_alpha = new double[size][size];


        for(int r=0; r<size; r++){
            for(int c=0; c<size; c++){
                beta_minus_alpha[r][c] = beta_diag[r][c] - alpha[r][c];
            }
        }

        DoubleMatrix2D beta_mat = DoubleFactory2D.dense.make(beta_minus_alpha);
        DenseDoubleEigenvalueDecomposition eigval = new DenseDoubleEigenvalueDecomposition(beta_mat);
        DoubleMatrix1D real = eigval.getRealEigenvalues();

        if(real.getMinLocation()[0] < 0){
            throw new RuntimeException("Unstable. beta - alpha must have eigenvalues with strictly positive real part.");
        }
    }

    public static int attribute(double alea, double I_star, double[] m_lambda ){

        int index = 0;
        double cumul = m_lambda[0];
        while (alea > (cumul / I_star)) {
            index = index + 1;
            cumul = cumul + m_lambda[index];
        }
        return index;
    }

    public static double[] calculateIntensity(double[] time,double[] history, double lambda0,double alpha, double beta){
        double[] l  = new double[time.length];
        Arrays.fill(l,0,time.length,lambda0);

        for(int i=0; i<time.length; i++){
            for(int j=0; j<history.length; j++){
                if(time[i] > history[j]){
                    l[i] += alpha * Math.exp(-beta * (time[i] - history[j]));
                }
            }
        }

        return l;
    }

    public static double[] calculateIntensity(int index, double[] time,double[] history, double[] lambda0,double[][] alpha, double[][] beta){
        double[] l  = new double[time.length];
        Arrays.fill(l,0,time.length,lambda0[index]);

        int dimension = lambda0.length;

        for(int t=0; t<time.length; t++){
            for(int h=0; h<history.length; h++){
                if(time[t] > history[h]){
                    for(int i=0; i<=index; i++) {
                        for (int d = 0; d < dimension; d++) {
                            double c_alpha = alpha[d][index];
                            double c_beta = beta[d][index];

                            l[t] += c_alpha * Math.exp(-c_beta * (time[t] - history[h]));
                        }
                    }
                }
            }
        }

        return l;
    }

    public static double[] linspace(double min, double max, int points) {
        double[] d = new double[points];
        for (int i = 0; i < points; i++){
            d[i] = min + i * (max - min) / (points - 1);
        }
        return d;
    }

    public static List<LocalTime> convertToDateTime(double[] times, LocalTime startTime){
        List<LocalTime> dateTimes = new ArrayList<>();
        for (int i = 0; i < times.length; i++) {
            dateTimes.add(startTime.plusSeconds(new Double(times[i]).longValue()));
        }

        return dateTimes;
    }

    public static List<HawkesData> getSortedHawkesData(List<List<Double>> values){
        List<HawkesData> hawkesDataList = new ArrayList<>();
        int type=0;
        Iterator iterator = values.iterator();
        while(iterator.hasNext()){
            Iterator it2 = ((List<Double>)iterator.next()).iterator();
            type++;
            while(it2.hasNext()){
                Double d = (Double)it2.next();
                HawkesData hd = new HawkesData();

                //hd.setDelay(new Double(d.doubleValue() * 10000).longValue());
                hd.setDelay(new Double(d.doubleValue()).longValue());
                hd.setType(type);

                hawkesDataList.add(hd);
            }
        }

        Comparator<HawkesData> hawkesComp = Comparator.comparing(e -> e.getDelay());
        Collections.sort(hawkesDataList, hawkesComp.reversed());
//        Collections.reverse(hawkesDataList);

        return hawkesDataList;
    }

    public static double[] getHistory(int index,List<List<Double>> values){
        List<Double> simulation = values.get(index);
        double history[] = new double[simulation.size()];
        for(int i=0; i<history.length; i++){
            if (simulation.get(i) != 0) {
                history[i] = simulation.get(i);
            }
        }

        return history;
    }

    public static void writeIntensityDataToFile(){

    }




}
