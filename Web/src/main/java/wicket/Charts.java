package wicket;

import com.googlecode.wickedcharts.highcharts.options.*;
import com.googlecode.wickedcharts.highcharts.options.color.HexColor;
import com.googlecode.wickedcharts.highcharts.options.series.Coordinate;
import com.googlecode.wickedcharts.highcharts.options.series.CoordinatesSeries;
import com.googlecode.wickedcharts.highcharts.options.series.Series;
import com.googlecode.wickedcharts.highcharts.options.series.SimpleSeries;
import com.googlecode.wickedcharts.wicket7.highcharts.Chart;
import hawkes.HawkesFunctions;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by dharmeshsing on 8/04/16.
 */
public class Charts extends WebPage {

    public Charts(PageParameters pageParameters){
        int horizon = Integer.parseInt(pageParameters.get("horizon").toString());
        String lambda = pageParameters.get("lambda").toString();
        String alpha = pageParameters.get("alpha").toString();
        String beta = pageParameters.get("beta").toString();

        add(new Chart("intensityChart", getIntensityChart(horizon, lambda, alpha, beta)));
        //add(new Chart("qqplotChart", getQQPlot(horizon, lambda, alpha, beta)));

        addLinks();

    }

    private void addLinks(){
        add(new Link("homeLink"){
            @Override
            public void onClick() {
                setResponsePage(HomePage.class);
            }
        });

        add(new Link("stockLink"){
            @Override
            public void onClick() {
                setResponsePage(AllStocksPage.class);
            }
        });

        add(new Link("clientLink"){
            @Override
            public void onClick() {
                setResponsePage(AllClientsPage.class);
            }
        });

        add(new Link("hawkesDataLink"){
            @Override
            public void onClick() {
                setResponsePage(HawkesDataPage.class);
            }
        });

        add(new Link("simulationLink"){
            @Override
            public void onClick() {
                setResponsePage(HawkesSimulation.class);
            }
        });

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

    private double[][] convertBeta2DArray(String betaText){
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

    private Options getIntensityChart(int horizon, String lambdaText,String alphaText,String betaText) {
        Options options = new Options();

        options.clearSeries();
        ChartOptions chartOptions = new ChartOptions();
        chartOptions.setZoomType(ZoomType.X);

        options.setChartOptions(chartOptions);
        chartOptions.setType(SeriesType.LINE);
        Title title = new Title("Intensity Chart");
        title.setX(-20);
        options.setTitle(title);

        double[] lambda0 = convertLambda(lambdaText);
        double[][] alpha = convertAlpha(alphaText);
        double[] beta = convertBeta(betaText);
        double[][] beta2 = convertBeta2DArray(betaText);

        List<List<Double>> values = HawkesFunctions.simulateHawkes(lambda0, alpha, beta, horizon);

        double[] times = HawkesFunctions.linspace(0, 100, horizon);
        double[] intensity1 = HawkesFunctions.calculateIntensity(0, times, HawkesFunctions.getHistory(0,values), lambda0, alpha, beta2);
        double[] intensity2 = HawkesFunctions.calculateIntensity(1, times, HawkesFunctions.getHistory(1,values), lambda0, alpha, beta2);
        double[] intensity3 = HawkesFunctions.calculateIntensity(2, times, HawkesFunctions.getHistory(2,values), lambda0, alpha, beta2);
        double[] intensity4 = HawkesFunctions.calculateIntensity(3, times, HawkesFunctions.getHistory(3,values), lambda0, alpha, beta2);
        double[] intensity5 = HawkesFunctions.calculateIntensity(4, times, HawkesFunctions.getHistory(4,values), lambda0, alpha, beta2);
        double[] intensity6 = HawkesFunctions.calculateIntensity(5, times, HawkesFunctions.getHistory(5,values), lambda0, alpha, beta2);
        double[] intensity7 = HawkesFunctions.calculateIntensity(6, times, HawkesFunctions.getHistory(6,values), lambda0, alpha, beta2);
        double[] intensity8 = HawkesFunctions.calculateIntensity(7, times, HawkesFunctions.getHistory(7, values), lambda0, alpha, beta2);


        DecimalFormat df = new DecimalFormat( "#,###,###,##0.00" );
        String[] s_times = new String[times.length];
        for (int i = 0; i < s_times.length; i++){
            s_times[i] = df.format(times[i]).toString();
        }

        List<Number> int1 = new ArrayList<>();
        for(int i=0; i<intensity1.length; i++){
            int1.add(intensity1[i]);
        }

        List<Number> int2 = new ArrayList<>();
        for(int i=0; i<intensity2.length; i++){
            int2.add(intensity2[i]);
        }

        List<Number> int3 = new ArrayList<>();
        for(int i=0; i<intensity3.length; i++){
            int3.add(intensity3[i]);
        }

        List<Number> int4 = new ArrayList<>();
        for(int i=0; i<intensity4.length; i++){
            int4.add(intensity4[i]);
        }

        List<Number> int5 = new ArrayList<>();
        for(int i=0; i<intensity5.length; i++){
            int5.add(intensity5[i]);
        }

        List<Number> int6 = new ArrayList<>();
        for(int i=0; i<intensity6.length; i++){
            int6.add(intensity6[i]);
        }

        List<Number> int7 = new ArrayList<>();
        for(int i=0; i<intensity7.length; i++){
            int7.add(intensity7[i]);
        }

        List<Number> int8 = new ArrayList<>();
        for(int i=0; i<intensity8.length; i++){
            int8.add(intensity8[i]);
        }


        Axis xAxis = new Axis();
        xAxis.setCategories(Arrays.asList(s_times));
        xAxis.setMaxZoom(100);
        xAxis.setTickLength(1);
        xAxis.setTitle(new Title("Times(s)"));
        options.setxAxis(xAxis);

        PlotLine plotLines = new PlotLine();
        plotLines.setValue(0f);
        plotLines.setWidth(1);
        plotLines.setColor(new HexColor("#999999"));

        Axis yAxis = new Axis();
        yAxis.setTitle(new Title("Lambda(t)"));
        yAxis.setPlotLines(Collections.singletonList(plotLines));
        options.setyAxis(yAxis);

        Legend legend = new Legend();
        legend.setLayout(LegendLayout.HORIZONTAL);
        legend.setAlign(HorizontalAlignment.CENTER);
        legend.setVerticalAlign(VerticalAlignment.BOTTOM);
        //legend.setX(-10);
        // legend.setY(100);
        legend.setBorderWidth(0);
        options.setLegend(legend);

        Series<Number> series1 = new SimpleSeries();
        series1.setName("Type 1");
        series1.setData(int1);
        options.addSeries(series1);

        Series<Number> series2 = new SimpleSeries();
        series2.setName("Type 2");
        series2.setData(int2);
        options.addSeries(series2);

        Series<Number> series3 = new SimpleSeries();
        series3.setName("Type 3");
        series3.setData(int3);
        options.addSeries(series3);

        Series<Number> series4 = new SimpleSeries();
        series4.setName("Type 4");
        series4.setData(int4);
        options.addSeries(series4);

        Series<Number> series5 = new SimpleSeries();
        series5.setName("Type 5");
        series5.setData(int5);
        options.addSeries(series5);

        Series<Number> series6 = new SimpleSeries();
        series6.setName("Type 6");
        series6.setData(int6);
        options.addSeries(series6);

        Series<Number> series7 = new SimpleSeries();
        series7.setName("Type 7");
        series7.setData(int7);
        options.addSeries(series7);

        Series<Number> series8 = new SimpleSeries();
        series8.setName("Type 8");
        series8.setData(int8);
        options.addSeries(series8);

        return options;

    }

    private Options getQQPlot(int horizon, String lambdaText,String alphaText,String betaText){
        Options options = new Options();

        ChartOptions chartOptions = new ChartOptions();

        options.setChartOptions(chartOptions);
        chartOptions.setType(SeriesType.SCATTER);
        Title title = new Title("QQPlot Chart");
        title.setX(-20);
        options.setTitle(title);

        double[] lambda0 = convertLambda(lambdaText);
        double[][] alpha = convertAlpha(alphaText);
        double[] beta = convertBeta(betaText);
        double[][] beta2 = convertBeta2DArray(betaText);

        List<List<Double>> values = HawkesFunctions.simulateHawkes(lambda0, alpha, beta, horizon);

        double[] times = HawkesFunctions.linspace(0, 100, horizon);
        double[] intensity1 = HawkesFunctions.calculateIntensity(0, times, HawkesFunctions.getHistory(0,values), lambda0, alpha, beta2);
        double[] intensity2 = HawkesFunctions.calculateIntensity(1, times, HawkesFunctions.getHistory(1,values), lambda0, alpha, beta2);
        double[] intensity3 = HawkesFunctions.calculateIntensity(2, times, HawkesFunctions.getHistory(2,values), lambda0, alpha, beta2);
        double[] intensity4 = HawkesFunctions.calculateIntensity(3, times, HawkesFunctions.getHistory(3,values), lambda0, alpha, beta2);
        double[] intensity5 = HawkesFunctions.calculateIntensity(4, times, HawkesFunctions.getHistory(4,values), lambda0, alpha, beta2);
        double[] intensity6 = HawkesFunctions.calculateIntensity(5, times, HawkesFunctions.getHistory(5,values), lambda0, alpha, beta2);
        double[] intensity7 = HawkesFunctions.calculateIntensity(6, times, HawkesFunctions.getHistory(6,values), lambda0, alpha, beta2);
        double[] intensity8 = HawkesFunctions.calculateIntensity(7, times, HawkesFunctions.getHistory(7, values), lambda0, alpha, beta2);

        Arrays.sort(intensity1);
        Arrays.sort(intensity2);
        Arrays.sort(intensity3);
        Arrays.sort(intensity4);
        Arrays.sort(intensity5);
        Arrays.sort(intensity6);
        Arrays.sort(intensity7);
        Arrays.sort(intensity8);

        Percentile percentile = new Percentile();
        List<Coordinate<Number, Number>> seriesData = new ArrayList<>();

        double[] target_percentiles = HawkesFunctions.linspace(1, 99, 100);

        List<Coordinate<Number, Number>> normal = new ArrayList<>();
        List<Coordinate<Number, Number>> seriesData1 = new ArrayList<>();
        List<Coordinate<Number, Number>> seriesData2 = new ArrayList<>();
        List<Coordinate<Number, Number>> seriesData3 = new ArrayList<>();
        List<Coordinate<Number, Number>> seriesData4 = new ArrayList<>();
        List<Coordinate<Number, Number>> seriesData5 = new ArrayList<>();
        List<Coordinate<Number, Number>> seriesData6 = new ArrayList<>();
        List<Coordinate<Number, Number>> seriesData7 = new ArrayList<>();
        List<Coordinate<Number, Number>> seriesData8 = new ArrayList<>();

        for(int x=0; x<intensity1.length; x++){
            double xCord = (x - 0.5)/intensity1.length * 100;
            //xCord = -Math.log(xCord);
           // - np.log(1.0 - target_percentiles * 0.01)
           // -1/l*log(1-a)
            if(xCord <=0){
                xCord = 1;
            }

            System.out.println(Math.log(1.0 - x) + ", " + xCord);

            double yCord = percentile.evaluate(intensity1,xCord);
            seriesData1.add(new Coordinate(xCord,yCord));
        }

     /**   for(int x=0; x<intensity2.length; x++){
            double xCord = (x - 0.5)/intensity2.length * 100;
            double yCord = percentile.evaluate(intensity2,xCord);
            seriesData2.add(new Coordinate(xCord,yCord));
        }

        for(int x=0; x<intensity3.length; x++){
            double xCord = (x - 0.5)/intensity3.length * 100;
            double yCord = percentile.evaluate(intensity3,xCord);
            seriesData3.add(new Coordinate(xCord,yCord));
        }

        for(int x=0; x<intensity4.length; x++){
            double xCord = (x - 0.5)/intensity4.length * 100;
            double yCord = percentile.evaluate(intensity4,xCord);
            seriesData4.add(new Coordinate(xCord,yCord));
        }

        for(int x=0; x<intensity5.length; x++){
            double xCord = (x - 0.5)/intensity5.length * 100;
            double yCord = percentile.evaluate(intensity5,xCord);
            seriesData5.add(new Coordinate(xCord,yCord));
        }

        for(int x=0; x<intensity6.length; x++){
            double xCord = (x - 0.5)/intensity6.length * 100;
            double yCord = percentile.evaluate(intensity6,xCord);
            seriesData6.add(new Coordinate(xCord,yCord));
        }

        for(int x=0; x<intensity7.length; x++){
            double xCord = (x - 0.5)/intensity7.length * 100;
            double yCord = percentile.evaluate(intensity7,xCord);
            seriesData7.add(new Coordinate(xCord,yCord));
        }

        for(int x=0; x<intensity8.length; x++){
            double xCord = (x - 0.5)/intensity8.length * 100;
            double yCord = percentile.evaluate(intensity8,xCord);
            seriesData8.add(new Coordinate(xCord,yCord));
        }*/

        Axis xAxis = new Axis();
        xAxis.setMaxZoom(100);
        xAxis.setMin(0);
        //xAxis.setMax(theoretical_percentiles.length);
        options.setxAxis(xAxis);

        Axis yAxis = new Axis();
        yAxis.setTitle(new Title("Temperature"));
        yAxis.setMin(0);
      //  yAxis.setMax(theoretical_percentiles.length);
        options.setyAxis(yAxis);

        Legend legend = new Legend();
        legend.setLayout(LegendLayout.HORIZONTAL);
        legend.setAlign(HorizontalAlignment.CENTER);
        legend.setVerticalAlign(VerticalAlignment.BOTTOM);
        //legend.setX(-10);
        // legend.setY(100);
        legend.setBorderWidth(0);
        options.setLegend(legend);

       /** CoordinatesSeries normalSeries = new CoordinatesSeries();
        normalSeries.setName("Target");
        normalSeries.setData(normal);
        options.addSeries(normalSeries); **/

        CoordinatesSeries series1 = new CoordinatesSeries();
        series1.setSize(new PixelOrPercent(1, PixelOrPercent.Unit.PIXELS));
        series1.setName("Type 1");
        series1.setData(seriesData1);
        options.addSeries(series1);

      /**  CoordinatesSeries series2 = new CoordinatesSeries();
        series2.setName("Type 2");
        series2.setData(seriesData1);
        options.addSeries(series2);


        CoordinatesSeries series3 = new CoordinatesSeries();
        series3.setName("Type 3");
        series3.setData(seriesData3);
        options.addSeries(series3);


        CoordinatesSeries series4 = new CoordinatesSeries();
        series4.setName("Type 4");
        series4.setData(seriesData4);
        options.addSeries(series4);


        CoordinatesSeries series5 = new CoordinatesSeries();
        series5.setName("Type 5");
        series5.setData(seriesData5);
        options.addSeries(series5);


        CoordinatesSeries series6 = new CoordinatesSeries();
        series6.setName("Type 6");
        series6.setData(seriesData6);
        options.addSeries(series6);


        CoordinatesSeries series7 = new CoordinatesSeries();
        series7.setName("Type 7");
        series7.setData(seriesData7);
        options.addSeries(series7);


        CoordinatesSeries series8 = new CoordinatesSeries();
        series8.setName("Type 8");
        series8.setData(seriesData8);
        options.addSeries(series8);*/



        /*Series<Number> targetSeries = new Corod();
        targetSeries.setName("Taregt");
        targetSeries.setData(targetPer);
        options.addSeries(targetSeries);

        Series<Number> series1 = new SimpleSeries();
        series1.setName("Type1");
        series1.setData(empPer1);
        options.addSeries(series1);*/

        return options;
    }
}
