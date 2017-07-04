package reports;

import com.carrotsearch.hppc.ObjectArrayList;
import com.itextpdf.text.DocumentException;
import crossing.OrderData;
import crossing.OrderLoader;
import leafNode.OrderEntry;
import orderBook.Trade;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by dharmeshsing on 10/06/15.
 */
public class CrossingReportLatexGenerator {

    private List<OrderEntry> buyList;
    private List<OrderEntry> sellList;

    public void generate() throws IOException, DocumentException, ParseException {
        StringBuilder sb = new StringBuilder();
        OrderLoader orderLoader = new OrderLoader();

        addSection(sb,orderLoader.getMarketOrders(),"Market Order Test Case");
        addSection(sb,orderLoader.getLimitOrders(),"Limit Order Test Case");
        addSection(sb,orderLoader.getHiddenOrders(),"Hidden Order Test Case");
        addSection(sb,orderLoader.getStopOrders(),"Stop Order Test Case");
        addSection(sb,orderLoader.getStopLimitOrders(),"Stop Limit Order Test Case");
        addSection(sb,orderLoader.getFilterAndUncrossOrders(),"Filter And Uncross Test Case");
        addSection(sb,orderLoader.getAuctionOrders(),"Auction Test Case");
        addSection(sb,orderLoader.getCancelOrders(),"Cancel Order Test Case");
        addSection(sb,orderLoader.getReplaceOrders(),"Replace Order Test Case");

        FileUtils.write(new File("./MatchingLatexTestCases.tex"),sb.toString());
    }

    private void addSection(StringBuilder sb,ObjectArrayList<OrderData> orderDataList,String title) throws DocumentException, IOException, ParseException {
        generateLOBTables(sb, orderDataList, title);
        sb.append('\n');
    }

    private void generateLOBTables(StringBuilder sb,ObjectArrayList<OrderData> orderDataList, String title) throws IOException, ParseException, DocumentException {
        buyList = new ArrayList<>();
        sellList = new ArrayList<>();
        Object[] orderArr = orderDataList.buffer;
        for(int i=0; i<orderDataList.size(); i++) {
            OrderData orderData = (OrderData) orderArr[i];

            buyList.clear();
            sellList.clear();
            sb.append(LatexCommands.newPage());
            String desc = getFormattedDescription(orderData.getDescription());

            sb.append(LatexCommands.subSection(title + " - Test " + orderData.getTestNumber()));
            sb.append(LatexCommands.addParagraph(desc));
            sb.append(LatexCommands.addHorizontalSpace());

            //if(orderData.getInitState() != null) {
                addTables(sb, orderData.getInitState(), "Initial State");
           // }

            if(!orderData.getType().equals(OrderLoader.OrderDataType.AUCTION_ORDER.getValue())) {
                addTables(sb, orderData.getAggOrder(),"Aggressive Order");
            }

            addTables(sb,orderData.getExpState(),"Final State");

            addTrades(sb, orderData.getTrades());
        }
    }

    private String getFormattedDescription(String desc){
        desc = desc.replaceAll("msg","message");

        return desc;
    }

    private void addTables(StringBuilder sb,ObjectArrayList<OrderEntry> orderEntries,String caption) throws DocumentException {
        createBuySellTables(sb, orderEntries,caption);

        buyList.clear();
        sellList.clear();
    }

    private void addTrades(StringBuilder sb,ObjectArrayList<Trade> trades) throws DocumentException {
        sb.append(LatexCommands.startTradeTable());
        sb.append(LatexCommands.addTradeTable(trades));
        sb.append(LatexCommands.endTradeTable());
    }

    private void createBuySellTables(StringBuilder sb, ObjectArrayList<OrderEntry> orderEntries, String caption){

        readData(orderEntries);
        buyList.sort(Comparator.comparing(OrderEntry::getPrice).reversed());
        sellList.sort(Comparator.comparing(OrderEntry::getPrice));

        Set<Long> prices = createPriceList();
        sb.append(LatexCommands.addOrderTable(buyList,sellList,prices,caption));

    }

    private Set<Long>  createPriceList(){
        Set<Long> prices = new TreeSet<>(Comparator.comparingLong(e -> e.longValue()));
        for(OrderEntry oe: sellList) {
            prices.add(oe.getPrice());
        }
        for(OrderEntry oe: buyList) {
            prices.add(oe.getPrice());
        }

        return prices;
    }

    private void readData(ObjectArrayList<OrderEntry> initStateList){
        if(initStateList != null) {
            Object[] initArr = initStateList.buffer;
            for (int i = 0; i < initStateList.size(); i++) {
                OrderEntry oe = (OrderEntry) initArr[i];

                if (oe.getSide() == 1) {
                    buyList.add(oe);
                } else {
                    sellList.add(oe);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        CrossingReportLatexGenerator crossingReportGenerator = new CrossingReportLatexGenerator();
        crossingReportGenerator.generate();

    }
}
