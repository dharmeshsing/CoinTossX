package wicket.services;

import com.carrotsearch.hppc.LongHashSet;
import com.carrotsearch.hppc.LongSet;
import com.carrotsearch.hppc.ObjectArrayList;
import com.googlecode.wickedcharts.highcharts.options.*;
import com.googlecode.wickedcharts.highcharts.options.functions.DefaultFormatter;
import com.googlecode.wickedcharts.highcharts.options.series.SimpleSeries;
import dao.OffHeapStorage;
import gateway.client.GatewayClientImpl;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sbe.builder.AdminBuilder;
import sbe.builder.LogonBuilder;
import sbe.msg.AdminTypeEnum;
import sbe.msg.marketData.TradingSessionEnum;
import uk.co.real_logic.agrona.DirectBuffer;
import vo.ClientVO;
import vo.OrderVO;
import vo.StockVO;
import vo.TradeVO;
import wicket.dataProvider.ClientDataProvider;
import wicket.dataProvider.StockDataProvider;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dharmeshsing on 19/04/16.
 */
@Service
@Singleton
public class ServicesImpl implements Services,Serializable {

    @Autowired
    private transient TradingSession tradingSession;

    @Autowired
    private transient ApplicationContext appContext;

    private transient StockDataProvider stockDataProvider;
    private transient ClientDataProvider clientDataProvider;

    private transient Properties properties;
    private transient ClientVO adminClientVO;
    private transient AdminBuilder adminBuilder;
    private transient GatewayClientImpl tradingGatewayPub;
    private OffHeapStorage offHeapStorage;
    private boolean saved;


    @PostConstruct
    public void init() {
        try {
            loadProperties("web.properties");
            clientDataProvider = new ClientDataProvider(getDataPath());
            stockDataProvider = new StockDataProvider(getDataPath());
            offHeapStorage = new OffHeapStorage();
            offHeapStorage.init(getDataPath(),true);

            adminClientVO = clientDataProvider.getClient(999);
            clientDataProvider.removeClient(adminClientVO);
            adminBuilder = new AdminBuilder();
            loginToTradingGatewayPub();
        } catch (Exception e) {
            throw new RuntimeException("Web client is unable to login to Trading Gateway",e);
        }
    }

    public void loginToTradingGatewayPub() throws IOException {
        ClientVO clientVO = getAdminClient();
        String url = clientVO.getTradingInputURL();
        int streamId = clientVO.getTradingInputStreamId();
        int compId = clientVO.getCompId();
        String password = clientVO.getPassword();

        tradingGatewayPub = new GatewayClientImpl();
        tradingGatewayPub.connectInput(url,streamId);

        LogonBuilder logonBuilder = new LogonBuilder();
        DirectBuffer buffer = logonBuilder.compID(compId)
                .password(password.getBytes())
                .newPassword(password.getBytes())
                .build();

        for(int i=0; i<3; i++){
            try {
                Thread.sleep(1000);
                System.out.println("Message login");
                tradingGatewayPub.send(buffer);
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    @PreDestroy
    public void stop(){
        sendShutDownMessage();
        tradingGatewayPub.disconnectInput();
        ((ConfigurableApplicationContext) appContext).close();
    }

    @Override
    public GatewayClientImpl getTradingGatewayPub() {
        return tradingGatewayPub;
    }

    @Override
    public StockVO getStockVO(int securityId) {
        return stockDataProvider.getStock(securityId);
    }

    public ClientDataProvider getClientDataProvider(){
        return clientDataProvider;
    }

    public StockDataProvider getStockDataProvider(){
        return stockDataProvider;
    }

    public String getScriptPath(){
        return properties.get("SCRIPT_PATH").toString();
    }

    public String getDataPath(){
        return properties.get("DATA_PATH").toString();
    }

    private void loadProperties(String propertiesFile) throws IOException {
        if(properties == null) {
            properties = new Properties();
        }

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFile)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new IOException("Unable to load properties file " + propertiesFile);
            }
        }
    }

    public void loadPropertiesFromFile(String propertiesFile) throws IOException {
        if(properties == null) {
            properties = new Properties();
        }

        try (InputStream inputStream = new FileInputStream(propertiesFile)) {

            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new IOException("Unable to load properties file " + propertiesFile);
            }
        }
    }

    public Properties getProperties(){
        return properties;
    }

    @Override
    public TradingSessionEnum getTradingSession(int securityId) {
        return tradingSession.getTradingSession(securityId);
    }

    @Override
    @Scheduled(fixedRate = 10000)
    public void setTradingSessionEnum() {
        ObjectArrayList<StockVO> stocks = stockDataProvider.getStocks();
        for (int i=0, max=stocks.size(); i < max; i++) {
            StockVO stockVO = stocks.get(i);
            if (stockVO != null) {
                int securityId = stockVO.getSecurityId();
                TradingSessionEnum tradingSessionEnum = offHeapStorage.getSymbolStatus(securityId);
                tradingSession.setTradingSessionEnum(securityId,tradingSessionEnum);

            }
        }
    }

    @Override
    @Scheduled(fixedRate = 10000)
    public void updateHawkesSimulation() {
        if(offHeapStorage.hasHawkesSimulationStarted() && offHeapStorage.isSimultationComplete() && !saved){
            saved = true;
            stopSimulation();
        }
    }

    @Override
    public ClientVO getAdminClient() {
        return adminClientVO;
    }

    @Override
    public void sendSnapShotRequest(DirectBuffer buffer) {
        tradingGatewayPub.send(buffer);
    }

    @Override
    public DirectBuffer getSnapShotRequest(int securityId){
        return adminBuilder.compID(getAdminClient().getCompId())
                .securityId(securityId)
                .adminMessage(AdminTypeEnum.LOB)
                .build();
    }

    private void sendShutDownMessage(){
        tradingGatewayPub.send(adminBuilder.compID(getAdminClient().getCompId())
                .adminMessage(AdminTypeEnum.ShutDown)
                .build());
    }

    @Override
    public void changeTradingSession(TradingSessionEnum tradingSessionEnum) {
        tradingSession.changeTradingSession(tradingSessionEnum);
    }

    public void saveLOBDataToFile(){
        try {
            File testResultsDir = new File(getDataPath() + File.separator + "TestResults_" + DateTimeFormatter.ofPattern("dd-MM-yyyy_HH_mm_ss").format(LocalDateTime.now()));
            testResultsDir.mkdir();

            ObjectArrayList<StockVO> stocks = stockDataProvider.getStocks();
            for (int i=0, max=stocks.size(); i < max; i++) {
                StockVO stockVO = stocks.get(i);

                if(stockVO == null){
                    continue;
                }

                int securityId = stockVO.getSecurityId();
                System.out.println("Saving lob details for security " + securityId);

                //save orders
                Collection<OrderVO> submittedOrders = offHeapStorage.getSubmittedOrders(securityId);
                saveOrders(submittedOrders, securityId, testResultsDir);

                //save trades
                Collection<TradeVO> trades = offHeapStorage.getTrades(securityId);
                saveTrades(trades, securityId, testResultsDir);

                //save bid and ask
                sendSnapShotRequest(getSnapShotRequest(securityId));
                Thread.sleep(2000);
                while (!offHeapStorage.getLOBStatus()) {
                    Thread.sleep(2000);
                }
                Collection<OrderVO> lobOrders = offHeapStorage.getOfferOrders(securityId);
                Collection<OrderVO> bidOrders = offHeapStorage.getBidOrders(securityId);
                lobOrders.addAll(bidOrders);
                saveLOBOrders(lobOrders, securityId, testResultsDir);

            }

        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void startSimulation() throws IOException {
        ObjectArrayList<ClientVO> clients = getClientDataProvider().getClients();
        for (int i=0, max=clients.size(); i < max; i++) {
            ClientVO clientVO = clients.get(i);
            if(clientVO != null) {
                ProcessBuilder processBuilder = new ProcessBuilder();
                List<String> cmd = new ArrayList<>();
                cmd.add(getScriptPath() + File.separator + "startHawkesSimulation" + getFileExtension());
                cmd.add(String.valueOf(clientVO.getCompId()));
                cmd.add(String.valueOf(clientVO.getSecurityId()));

                processBuilder.command(cmd);
                processBuilder.start();
            }
        }
    }

    private String getFileExtension() {
        if (System.getProperty("os.name").contains("Windows")) {
            return ".bat";
        }else{
            return ".sh";
        }
    }

    @Override
    public void stopSimulation() {
        System.out.println("Going to save lob");
        saveLOBDataToFile();

        ClientVO clientVO = getAdminClient();
        AdminBuilder adminBuilder = new AdminBuilder();
        DirectBuffer directBuffer = adminBuilder.compID(clientVO.getCompId()).adminMessage(AdminTypeEnum.SimulationComplete).build();
        getTradingGatewayPub().send(directBuffer);

        System.out.println("Sent simulation complete");
    }

    @Override
    public void warmpSimulation() throws IOException {
        ObjectArrayList<ClientVO> clients = getClientDataProvider().getClients();
        for (int i=0, max=clients.size(); i < max; i++) {
            ClientVO clientVO = clients.get(i);
            if(clientVO != null) {
                Runtime.getRuntime().exec(getScriptPath() + File.separator +
                        "startHawkesSimulation" + getFileExtension() + " " +
                        clients.get(i).getCompId() + " " +
                        clients.get(i).getSecurityId() + " " +
                        "warmUp");
            }
        }
    }

    @Override
    public void stopWarmup()  {
        try {
            Runtime.getRuntime().exec(getScriptPath() + File.separator + "stopHawkesSimulation" + getFileExtension());

            ClientVO clientVO = getAdminClient();
            AdminBuilder adminBuilder = new AdminBuilder();
            DirectBuffer directBuffer = adminBuilder.compID(clientVO.getCompId()).adminMessage(AdminTypeEnum.WarmUpComplete).build();
            getTradingGatewayPub().send(directBuffer);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean isSimulationStarted() {
        return offHeapStorage.getHawkesSimulationSttaus();
    }

    @Override
    public boolean isWarmupStarted() {
        return offHeapStorage.getWarmupSimulationSttaus();
    }

    @Override
    public boolean isWarmUpComplete() {
        return offHeapStorage.isWarmupComplete();
    }

    @Override
    public boolean isSimulationIsComplete() {
        return offHeapStorage.isSimultationComplete();
    }

    private void saveOrders(Collection<OrderVO> orderVOs,int securityId,File testResultsDir) throws Exception {
        String fileName = "OrdersSubmitted_" + securityId + ".csv";
        File file = new File(testResultsDir,fileName);

        writeCsvFile(file, OrderVO.getFileHeader(), orderVOs);
    }

    private void saveTrades(Collection<TradeVO> tradeVOs,int securityId,File testResultsDir) throws Exception {
        String fileName = "Trades_" + securityId + ".csv";
        File file = new File(testResultsDir,fileName);

        writeCsvFile(file, TradeVO.getFileHeader(), tradeVOs);
    }

    private void saveLOBOrders(Collection<OrderVO> lobOrders,int securityId,File testResultsDir) throws Exception {
        String fileName = "LOBOrders_" + securityId + ".csv";
        File file = new File(testResultsDir,fileName);

        writeCsvFile(file, OrderVO.getFileHeader(), lobOrders);
    }


    public static void writeCsvFile(File file, String[] header,Collection<?> listToSave) throws Exception {
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(System.lineSeparator())
                                  .withQuote('"')
                                  .withQuoteMode(QuoteMode.ALL);

        try (
                FileWriter fileWriter = new FileWriter(file);
                CSVPrinter csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat)
        ) {

            csvFilePrinter.printRecord(header);

            listToSave.stream().forEach(obj -> {
                if(obj != null){
                    try {
                        if (obj instanceof OrderVO) {
                            csvFilePrinter.printRecord(((OrderVO) obj).getValues());
                        } else {
                            csvFilePrinter.printRecord(((TradeVO) obj).getValues());

                        }
                    }catch (Exception e){
                        throw new RuntimeException("Error writing to file",e);
                    }
                }
            });
        }
    }

    @Override
    public Options getChart(int securityId,String stockCode) throws Exception {
        Collection<OrderVO> bids = offHeapStorage.getBidOrders(securityId);
        Collection<OrderVO> offers = offHeapStorage.getOfferOrders(securityId);

        if(bids == null && offers == null){
            Options options = new Options();
            options.setTitle(new Title("Limit Order Book"));
            return options;
        }

        List<Number> bidQuantities = new ArrayList<>();
        List<Number> offerQuantities = new ArrayList<>();

        LongSet allPrices = new LongHashSet();
        Iterator<Long> it = offHeapStorage.getPrices(securityId);
        while(it.hasNext()){
            allPrices.add(it.next());
        }

        long[] prices = allPrices.toArray();
        Arrays.sort(prices);

        for(long price : prices){

            long totalBid = 0;
            long totalOffer = 0;

            for(OrderVO bid : bids){
                if(bid.getPrice() == price){
                    totalBid += bid.getVolume();
                }
            }

            for(OrderVO offer : offers){
                if(offer.getPrice() == price){
                    totalOffer += offer.getVolume();
                }
            }

            bidQuantities.add(totalBid);
            offerQuantities.add(totalOffer);

        }

        List<String> strPrices = Arrays.stream(prices).mapToObj(String::valueOf).collect(Collectors.toList());


        Options options = new Options();
        options.setTitle(new Title(stockCode));
        options.setChartOptions(new ChartOptions()
                .setType(SeriesType.COLUMN));

        options.setxAxis(new Axis()
                .setCategories(strPrices)
                .setTitle(new Title("Prices")));

        options.setyAxis(new Axis().setTitle(new Title("Volume")));

        options.setPlotOptions(new PlotOptionsChoice()
                .setColumn(new PlotOptions()
                        .setStacking(Stacking.NORMAL)));


        options.setTooltip(new Tooltip()
                .setFormatter(new DefaultFormatter()));

        options.setCredits(new CreditOptions()
                .setEnabled(Boolean.FALSE));

        options.addSeries(new SimpleSeries()
                .setName("Bid")
                .setData(bidQuantities));

        options.addSeries(new SimpleSeries()
                .setName("Ask")
                .setData(offerQuantities));

        return options;

    }

    @Override
    public void updateClientSimulationStatus(){
        ObjectArrayList<ClientVO> clients = clientDataProvider.getClients();
        for (int i=0, max=clients.size(); i < max; i++) {
            ClientVO clientVO = clients.get(i);

            if(clientVO == null){
                continue;
            }

            if (isSimulationStarted()){
                clientVO.setStatusText("Running");
                clientVO.setStatusClass("label label-success");
            }else if(isWarmupStarted()){
                clientVO.setStatusText("Warming Up");
                clientVO.setStatusClass("label label-info");
            }else if(isSimulationIsComplete()){
                clientVO.setStatusText("Finished");
                clientVO.setStatusClass("label label-warning");
            }else if(isWarmUpComplete()){
                clientVO.setStatusText("Warm Up Complete");
                clientVO.setStatusClass("label label-warning");
            }
        }
    }

    @Override
    public void updateLabelClass(){
        ObjectArrayList<ClientVO> clients = clientDataProvider.getClients();
        for (int i=0, max=clients.size(); i < max; i++) {
            if(clients.get(i) == null){continue;}
            clients.get(i).setStatusClass("label label-default");
        }
    }

    @Override
    public void updateTradingSession(){
        ObjectArrayList<StockVO> stocks = stockDataProvider.getStocks();
        for (int i=0, max=stocks.size(); i < max; i++) {
            StockVO stockVO = stocks.get(i);
            if (stockVO != null) {
                stockVO.setStatusText(getTradingSession(stockVO.getSecurityId()).toString());
            }
        }
    }

    public void addClient(ClientVO clientVO) throws Exception{
        clientDataProvider.addClient(clientVO);
    }

    @Override
    public OffHeapStorage getOffHeapStorage() {
        return offHeapStorage;
    }
}
