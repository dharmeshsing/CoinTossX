package dao;

import com.carrotsearch.hppc.*;
import com.carrotsearch.hppc.cursors.ObjectCursor;
import org.mapdb.*;
import sbe.msg.marketData.TradingSessionEnum;
import vo.ClientVO;
import vo.OrderVO;
import vo.StockVO;
import vo.TradeVO;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

public class OffHeapStorage {

    private static final Integer HAWKES_STATUS = 1;
    private static final Integer WARMUP_STATUS = 2;
    private static final Integer LOB_STATUS = 3;

    private ObjectArrayList<StockVO> stockVOList;
    private ObjectArrayList<ClientVO> clientVOList;
    private ObjectObjectMap<String,BTreeMap<Long,OrderVO>> ordersMap;
    private IntObjectMap<BTreeMap<Integer,TradeVO>> tradesMap;
    private HTreeMap<Long,String> symbolStatusMap;
    private HTreeMap<Integer,Boolean> hawkesSimulationMap;
    private HTreeMap<Integer,Boolean> warmupSimulationMap;
    private HTreeMap<Integer,Boolean> statusMap;
    private ObjectObjectMap<String,HTreeMap.KeySet<Long> > pricesMap;
    private DB db;
    private boolean isReadOnly;

    private static final String ORDERS_BID = "orders_bid_";
    private static final String ORDERS_OFFER = "orders_offer_";
    private static final String ORDERS_SUBMITTED = "orders_sub_";
    private static final String PRICES = "prices_";

    public void init(String dataPath,boolean isReadOnly){
        ordersMap = new ObjectObjectHashMap<>();
        tradesMap = new IntObjectHashMap<>();
        pricesMap = new ObjectObjectHashMap<>();
        this.isReadOnly = isReadOnly;

        stockVOList = loadStocks(dataPath);
        clientVOList = loadClients(dataPath);
        removeClient(getClient(999)); //remove admin from list of clients

        initDB(dataPath,isReadOnly);
        initStockHashMaps(stockVOList);
        initSymbolHashMap(stockVOList);
        initSimulation(clientVOList);

        db.commit();
    }

    private void initDB(String dataPath,boolean isReadOnly){
        DBMaker.Maker maker = DBMaker.fileDB(dataPath + File.separator + "webStorage.db")
                .fileMmapEnable()
                .transactionEnable()
                .closeOnJvmShutdown()
                .allocateStartSize(10 * 1024*1024*1024) //5GB
                .allocateIncrement(1024 * 1024 * 1024); //1GB

        if(isReadOnly){
            maker = maker.readOnly();
        }

        db = maker.make();
    }

    private void initStockHashMaps(ObjectArrayList<StockVO> stockVOList){
        for (int i=0, max=stockVOList.size(); i < max; i++) {
            StockVO stockVO = stockVOList.get(i);
            if(stockVO != null){
                int securityId = stockVO.getSecurityId();

                //orders bid
                String bidKey = ORDERS_BID + securityId;
                BTreeMap<Long,OrderVO> orderBidMap = db.treeMap(bidKey, Serializer.LONG, Serializer.JAVA).createOrOpen();
                ordersMap.put(bidKey,orderBidMap);

                //orders offer
                String offerKey = ORDERS_OFFER + securityId;
                BTreeMap<Long,OrderVO> orderOfferMap = db.treeMap(offerKey, Serializer.LONG, Serializer.JAVA).createOrOpen();
                ordersMap.put(offerKey,orderOfferMap);

                //orders submitted
                String subKey = ORDERS_SUBMITTED + securityId;
                BTreeMap<Long,OrderVO> orderSubMap = db.treeMap(subKey, Serializer.LONG, Serializer.JAVA).createOrOpen();
                ordersMap.put(subKey,orderSubMap);

                //trades
                BTreeMap<Integer,TradeVO> tradeMap = db.treeMap("trades_" + securityId, Serializer.INTEGER, Serializer.JAVA).createOrOpen();
                tradesMap.put(securityId,tradeMap);

                //prices
                String priceKey = PRICES + securityId;
                HTreeMap.KeySet<Long> priceMap = db.hashSet(priceKey, Serializer.LONG).createOrOpen();
                pricesMap.put(priceKey,priceMap);
            }
        }
    }

    private void initSymbolHashMap(ObjectArrayList<StockVO> stockVOList){
        symbolStatusMap = db.hashMap("symbolStatus", Serializer.LONG, Serializer.STRING).createOrOpen();

        if(!isReadOnly) {
            for (int i = 0, max = stockVOList.size(); i < max; i++) {
                StockVO stockVO = stockVOList.get(i);
                if (stockVO != null) {
                    long securityId = stockVO.getSecurityId();
                    symbolStatusMap.put(securityId, TradingSessionEnum.ContinuousTrading.toString());
                }
            }
        }
    }

    public void setSymbolStatus(long securityId,TradingSessionEnum tradingSessionEnum){
        symbolStatusMap.put(securityId, tradingSessionEnum.toString());
    }

    public TradingSessionEnum getSymbolStatus(long securityId){
        return TradingSessionEnum.valueOf(symbolStatusMap.get(securityId));
    }

    public void initSimulation(ObjectArrayList<ClientVO> clientVOList){
        hawkesSimulationMap = db.hashMap("hawkesSimulation", Serializer.INTEGER, Serializer.BOOLEAN).createOrOpen();
        warmupSimulationMap = db.hashMap("warmupSimulation", Serializer.INTEGER, Serializer.BOOLEAN).createOrOpen();
        statusMap = db.hashMap("status", Serializer.INTEGER, Serializer.BOOLEAN).createOrOpen();

        if(!isReadOnly) {
            for (int i = 0, max = clientVOList.size(); i < max; i++) {
                ClientVO clientVO = clientVOList.get(i);
                if (clientVO != null) {
                    int compId = clientVO.getCompId();
                    hawkesSimulationMap.put(compId, false);
                    warmupSimulationMap.put(compId, false);
                }
            }

            statusMap.put(HAWKES_STATUS, false);
            statusMap.put(WARMUP_STATUS, false);
            statusMap.put(LOB_STATUS, false);
        }
    }

    public void updateHawkesSimulation(int compId,boolean status){
        hawkesSimulationMap.put(compId,status);
//        db.commit();
    }

    public boolean getHawkesSimulation(int compId){
        return hawkesSimulationMap.get(compId);
    }

    public void updateHawkesSimulationStatus(boolean status){
        statusMap.put(HAWKES_STATUS,status);
//        db.commit();
    }

    public boolean getHawkesSimulationSttaus(){
        return statusMap.get(HAWKES_STATUS);
    }

    public boolean hasHawkesSimulationStarted(){
        return statusMap.get(HAWKES_STATUS);
    }

    public void updateWarmupSimulation(int compId,boolean status){
        warmupSimulationMap.put(compId,status);
//        db.commit();
    }

    public boolean getWarmupSimulation(int compId){
        return warmupSimulationMap.get(compId);
    }

    public void updateWarmupSimulationStatus(boolean status){
        statusMap.put(WARMUP_STATUS,status);
//        db.commit();
    }

    public boolean getWarmupSimulationSttaus(){
        return statusMap.get(WARMUP_STATUS);
    }

    public boolean getLOBStatus(){
        return statusMap.get(LOB_STATUS);
    }

    public boolean hasLOBSnapshotComplete(){
        return statusMap.get(LOB_STATUS);
    }

    public void setLOBStatus(boolean status){
        statusMap.put(LOB_STATUS,status);
//        db.commit();
    }

    private ObjectArrayList<StockVO> loadStocks(String dataPath){
        String stockFile = dataPath + File.separator + "Stock.csv";
        ObjectArrayList<StockVO> stockVOList = new ObjectArrayList<>();
        try {
            StockDAO.loadStocks(stockFile, stockVOList);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load stocks",e);
        }
        return stockVOList;
    }

    private ObjectArrayList<ClientVO> loadClients(String dataPath){
        String clientFile = dataPath + File.separator + "clientData.csv";
        ObjectArrayList<ClientVO> clientVOList = new ObjectArrayList<>();
        try {
            ClientDAO.loadClients(clientFile, clientVOList);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load stocks",e);
        }
        return clientVOList;
    }

    public void removeClient(ClientVO clienVO){
        clientVOList.removeFirst(clienVO);
    }

    public ClientVO getClient(int id){
        for (int i=0, max=clientVOList.size(); i < max; i++) {
            ClientVO clientVO = clientVOList.get(i);
            if(clientVO.getCompId() == id){
                return clientVO;
            }
        }

        return null;
    }

    public void addPrice(int securityId,long price){
        HTreeMap.KeySet<Long>priceMap = pricesMap.get(PRICES + securityId);
        priceMap.add(price);
//        db.commit();
    }

    public Iterator<Long> getPrices(int securityId){
        HTreeMap.KeySet<Long> priceMap = pricesMap.get(PRICES + securityId);
        return priceMap.iterator();
    }

    public void clearPrices(int securityId){
        HTreeMap.KeySet<Long> priceMap = pricesMap.get(PRICES + securityId);
        if(priceMap.size() > 0) {
            priceMap.clear();
        }
        db.commit();
    }

    public void clearPrices(){
        for(ObjectCursor<HTreeMap.KeySet<Long>> pricesCursor : pricesMap.values()){
            if(pricesCursor.value.size() > 0) {
                pricesCursor.value.clear();
            }
        }
        db.commit();
    }

    public void addBidOrder(int securityId,OrderVO orderVO){
        BTreeMap<Long,OrderVO> orderMap = ordersMap.get(ORDERS_BID + securityId);
        orderMap.put(orderVO.getOrderId(),orderVO);
//        db.commit();
    }

    public void addOfferOrder(int securityId,OrderVO orderVO){
        BTreeMap<Long,OrderVO> orderMap = ordersMap.get(ORDERS_OFFER + securityId);
        orderMap.put(orderVO.getOrderId(),orderVO);
//        db.commit();
    }

    public void addSubmittedOrder(int securityId, OrderVO orderVO){
        BTreeMap<Long,OrderVO> orderMap = ordersMap.get(ORDERS_SUBMITTED + securityId);
        orderMap.put(orderVO.getOrderId(),orderVO);
//        db.commit();
    }

    public void addTrades(int securityId, TradeVO tradeVO){
        BTreeMap<Integer,TradeVO> tradeMap = tradesMap.get(securityId);
        tradeMap.put(tradeVO.getTradeId(),tradeVO);
//        db.commit();
    }

    public Collection<OrderVO> getBidOrders(int securityId){
        BTreeMap<Long,OrderVO> orderMap = ordersMap.get(ORDERS_BID + securityId);
        return orderMap.getValues();
    }

    public Collection<OrderVO> getOfferOrders(int securityId){
        BTreeMap<Long,OrderVO> orderMap = ordersMap.get(ORDERS_OFFER + securityId);
        return orderMap.getValues();
    }

    public Collection<OrderVO> getSubmittedOrders(int securityId){
        BTreeMap<Long,OrderVO> orderMap = ordersMap.get(ORDERS_SUBMITTED + securityId);
        return orderMap.getValues();
    }

    public Collection<TradeVO> getTrades(int securityId){
        BTreeMap<Integer,TradeVO> tradeMap = tradesMap.get(securityId);
        return tradeMap.getValues();
    }

    public void clearBidOrders(int securityId){
        BTreeMap<Long,OrderVO> orderMap = ordersMap.get(ORDERS_BID + securityId);
        if(orderMap.size() > 0) {
            orderMap.clear();
        }
        db.commit();
    }

    public void clearOfferOrders(int securityId){
        BTreeMap<Long,OrderVO> orderMap = ordersMap.get(ORDERS_OFFER + securityId);
        if(orderMap.size() > 0) {
            orderMap.clear();
        }
        db.commit();
    }

    public void clearSubmittedOrders(int securityId){
        BTreeMap<Long,OrderVO> orderMap = ordersMap.get(ORDERS_SUBMITTED + securityId);
        if(orderMap.size() > 0) {
            orderMap.clear();
        }
        db.commit();
    }

    public void clearTrades(int securityId){
        BTreeMap<Integer,TradeVO> tradeMap = tradesMap.get(securityId);
        if(tradeMap.size() > 0) {
            tradeMap.clear();
        }
        db.commit();
    }

    public void clearTrades(){
        for(ObjectCursor<BTreeMap<Integer,TradeVO>> tradeCursor : tradesMap.values()){
            if(tradeCursor.value.size() > 0) {
                tradeCursor.value.clear();
            }
        }
        db.commit();
    }

    public void clearOrders(){
        for(ObjectCursor<BTreeMap<Long,OrderVO>> ordersCursor : ordersMap.values()){
            if(ordersCursor.value.size() > 0) {
                ordersCursor.value.clear();
            }
        }
        db.commit();
    }

    public boolean isSimultationComplete(){
        if(hawkesSimulationMap.getValues().contains(true)){
            return false;
        }
        return true;
    }

    public boolean isWarmupComplete(){
        if(warmupSimulationMap.getValues().contains(false)){
            return false;
        }
        return true;
    }

    public void close(){
        db.commit();
        db.close();
    }

    public void commit(){
        db.commit();
    }
}

