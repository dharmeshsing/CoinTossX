package reports;

import com.carrotsearch.hppc.ObjectArrayList;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import crossing.OrderData;
import crossing.OrderLoader;
import leafNode.OrderEntry;
import orderBook.Trade;
import orderBook.Stock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by dharmeshsing on 10/06/15.
 */
public class CrossingReportGenerator {

    private static BaseFont base;
    private static BaseFont baseBold;
    private static Font HEADING_FONT;
    private static Font DEFAULT_FONT;
    private static Font DEFAULT_BOLD;
    private static final int COL_COUNT = 6;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    private List<OrderEntry> buyList;
    private List<OrderEntry> sellList;

    public void generate() throws IOException, DocumentException, ParseException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File("./MatchingTestCases.pdf")));

        TableHeader event = new TableHeader();
        writer.setPageEvent(event);
        document.open();

        createFonts();
        addCoverPage(document);
        addTableOfContents(document);

        OrderLoader orderLoader = new OrderLoader();

        addSection(document,orderLoader.getMarketOrders(),"1. Market Order Test Cases",event);
        addSection(document,orderLoader.getLimitOrders(),"2. Limit Order Test Cases",event);
        addSection(document,orderLoader.getHiddenOrders(),"3. Hidden Order Test Cases",event);
        addSection(document,orderLoader.getStopOrders(),"4. Stop Order Test Cases",event);
        addSection(document,orderLoader.getStopLimitOrders(),"5. Stop Limit Order Test Cases",event);
        addSection(document,orderLoader.getFilterAndUncrossOrders(),"6. Filter And Uncross Test Cases",event);
        addSection(document,orderLoader.getAuctionOrders(),"7. Auction Test Cases",event);
        addSection(document,orderLoader.getCancelOrders(),"8. Cancel Order Test Cases",event);
        addSection(document,orderLoader.getReplaceOrders(),"9. Replace Order Test Cases",event);

        document.close();
    }

    private void createFonts() throws IOException, DocumentException {
        base = BaseFont.createFont("/Library/Fonts/Arial.ttf", BaseFont.WINANSI,false);
        baseBold = BaseFont.createFont("/Library/Fonts/Arial Bold.ttf", BaseFont.WINANSI,false);
        HEADING_FONT = new Font(base, 11, Font.BOLD);
        DEFAULT_FONT = new Font(base, 11, Font.NORMAL);
        DEFAULT_BOLD = new Font(baseBold, 11, Font.NORMAL);
    }

    private void addCoverPage(Document document) throws DocumentException {
        Paragraph cover = new Paragraph("Matching Engine Limit Order Book Test Cases",new Font(baseBold, 30, Font.NORMAL));
        cover.setAlignment(Element.ALIGN_CENTER);
        document.add(cover);
        document.newPage();
    }

    private void addTableOfContents(Document document) throws DocumentException {
        document.add(new Paragraph("Table Of Contents",new Font(base, 20, Font.BOLD)));
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("1. Market Order Test Cases",DEFAULT_FONT));
        document.add(new Paragraph("2. Limit Order Test Cases",DEFAULT_FONT));
        document.add(new Paragraph("3. Hidden Order Test Cases",DEFAULT_FONT));
        document.add(new Paragraph("4. Stop Order Test Cases",DEFAULT_FONT));
        document.add(new Paragraph("5. Stop Limit Order Test Cases",DEFAULT_FONT));
        document.add(new Paragraph("6. Filter And Uncross Test Cases",DEFAULT_FONT));
        document.add(new Paragraph("7. Auction Test Cases",DEFAULT_FONT));
        document.add(new Paragraph("8. Cancel Order Test Cases",DEFAULT_FONT));
        document.add(new Paragraph("9. Replace Order Test Cases",DEFAULT_FONT));
        document.newPage();
    }

    private void addSection(Document document,ObjectArrayList<OrderData> orderDataList,String title,TableHeader event) throws DocumentException, IOException, ParseException {
        addSectionTitle(document, title);
        sectionTOC(document, orderDataList);
        event.setHeader(title);
        generateLOBTables(document, orderDataList);
        document.newPage();
    }

    private void sectionTOC(Document document,ObjectArrayList<OrderData> orderDataList) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(2f);
        table.setWidths(new int[]{3, 24});
        table.setSpacingBefore(5f);

        createSectionTableHeading(table);

        Object[] orderArr = orderDataList.buffer;
        for(int i=0; i<orderDataList.size(); i++) {
            OrderData orderData = (OrderData) orderArr[i];
            table.addCell(new Phrase(String.valueOf(orderData.getTestNumber()), DEFAULT_FONT));
            table.addCell(new Phrase(orderData.getDescription(), DEFAULT_FONT));
        }

        document.add(table);
    }

    private void addSectionTitle(Document document, String text) throws DocumentException {
        for(int i=0; i<2; i++) {
            document.add(Chunk.NEWLINE);
        }
        Paragraph sectionCT = new Paragraph(text,new Font(baseBold, 20, Font.NORMAL));
        sectionCT.setAlignment(Element.ALIGN_CENTER);
        document.add(sectionCT);
    }

    private void generateLOBTables(Document document,ObjectArrayList<OrderData> orderDataList) throws IOException, ParseException, DocumentException {
        buyList = new ArrayList<>();
        sellList = new ArrayList<>();
        Object[] orderArr = orderDataList.buffer;
        for(int i=0; i<orderDataList.size(); i++) {
            OrderData orderData = (OrderData) orderArr[i];

            buyList.clear();
            sellList.clear();
            document.newPage();
            if(i != 0) {
                document.add(Chunk.NEWLINE);
            }
            document.add(Chunk.NEWLINE);
            Paragraph testTitle = new Paragraph("Test " + orderData.getTestNumber() + " - " + orderData.getDescription(),DEFAULT_BOLD);
            document.add(testTitle);
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Stock",DEFAULT_BOLD));
            addStock(document,orderData.getStock());

            if(orderData.getInitState() != null) {
                document.add(new Paragraph("Initial State", DEFAULT_BOLD));
                addTables(document, orderData.getInitState());
            }

            if(!orderData.getType().equals(OrderLoader.OrderDataType.AUCTION_ORDER.getValue())) {
                document.add(new Paragraph("Aggressive Order", DEFAULT_BOLD));
                addTables(document, orderData.getAggOrder());
            }

            document.add(new Paragraph("Final State",DEFAULT_BOLD));
            addTables(document,orderData.getExpState());

            document.add(new Paragraph("Trades",DEFAULT_BOLD));
            addTrades(document, orderData.getTrades());
        }
    }

    private void addStock(Document document,Stock stock) throws DocumentException {
        PdfPTable table = new PdfPTable(3);

        table.setSpacingBefore(2f);
        createStockTableHeading(table);
        table.addCell(new Phrase(String.valueOf(stock.getStockCode()), DEFAULT_FONT));
        table.addCell(new Phrase(String.valueOf(stock.getMRS()), DEFAULT_FONT));
        table.addCell(new Phrase(String.valueOf(stock.getTickSize()), DEFAULT_FONT));

        table.setWidthPercentage(100);
        document.add(table);
        document.add( Chunk.NEWLINE );
    }

    private void addTables(Document document,ObjectArrayList<OrderEntry> orderEntries) throws DocumentException {
        PdfPTable table = createBuySellTables(orderEntries);
        table.setWidthPercentage(100);
        table.setSpacingBefore(2f);
        document.add(table);
        document.add( Chunk.NEWLINE );

        buyList.clear();
        sellList.clear();
    }

    private void addTrades(Document document,ObjectArrayList<Trade> trades) throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(2f);

        createTradesTableHeading(table);

        Object[] tradeArr = trades.buffer;
        for(int i=0; i<tradeArr.length; i++) {
            Trade trade = (Trade) tradeArr[i];
            if(trade != null) {
                table.addCell(new Phrase(String.valueOf(trade.getId()), DEFAULT_FONT));
                table.addCell(new Phrase(String.valueOf(trade.getPrice()/100.00), DEFAULT_FONT));
                table.addCell(new Phrase(String.valueOf(trade.getQuantity()), DEFAULT_FONT));
            }
        }

        document.add(table);
        document.add( Chunk.NEWLINE );
    }


    public ObjectArrayList<OrderData> getData() throws IOException, ParseException {
        return new OrderLoader().getHiddenOrders();
    }

    public ObjectArrayList<OrderData> getFilterAndUncrossData() throws IOException, ParseException {
       return new OrderLoader().getFilterAndUncrossOrders();
    }

    public ObjectArrayList<OrderData> getAuctionData() throws IOException, ParseException {
        return new OrderLoader().getAuctionOrders();
    }

    private PdfPTable createBuySellTables(ObjectArrayList<OrderEntry> orderEntries){
        PdfPTable table = new PdfPTable(2);

        readData(orderEntries);
        int maxRows = buyList.size() > sellList.size() ? buyList.size() : sellList.size();
        buyList.sort(Comparator.comparing(OrderEntry::getPrice).reversed());
        sellList.sort(Comparator.comparing(OrderEntry::getPrice));

        PdfPTable buyTable = createOrderBookTable("Buy", maxRows, buyList);
        table.addCell(buyTable);

        PdfPTable sellTable = createOrderBookTable("Sell", maxRows, sellList);
        table.addCell(sellTable);

        return table;
    }

    public PdfPTable createOrderBookTable(String heading, int maxRows, List<OrderEntry> orders){
        PdfPTable table = new PdfPTable(COL_COUNT);

        PdfPCell title = new PdfPCell(new Phrase(heading,DEFAULT_BOLD));
        title.setColspan(6);
        table.addCell(title);

        createTableHeading(table);

        for(OrderEntry oe: orders) {
            table.addCell(new Phrase(String.valueOf(oe.getOrderId()),DEFAULT_FONT));
            table.addCell(new Phrase(getOrderType(oe.getType()),DEFAULT_FONT));
            table.addCell(new Phrase(String.valueOf(oe.getMinExecutionSize()),DEFAULT_FONT));

            Date currentDate = Calendar.getInstance().getTime();
            currentDate.setTime(oe.getSubmittedTime());
            table.addCell(new Phrase(sdf.format(currentDate),DEFAULT_FONT));

            table.addCell(new Phrase(String.valueOf(oe.getQuantity()),DEFAULT_FONT));
            table.addCell(new Phrase(String.valueOf(oe.getPrice()/100.00),DEFAULT_FONT));
        }

        addDummyRows(table,maxRows - orders.size());

        return table;
    }

    private String getOrderType(long type){
        switch ((int)type) {
            case 1 : return "MO";
            case 2 : return "LO";
            case 3 : return "SO";
            case 4 : return "SL";
            case 5 : return "HO";
            default : return "ERROR";
        }
    }


    private void addDummyRows(PdfPTable table,int count){
        for(int i=0; i<count; i++){
            for(int k=0; k<6; k++) {
                table.addCell(new Phrase(" ",DEFAULT_FONT));
            }
        }
    }

    private void readData(ObjectArrayList<OrderEntry> initStateList){
        Object[] initArr = initStateList.buffer;
        for(int i=0; i<initStateList.size(); i++) {
            OrderEntry oe = (OrderEntry) initArr[i];

            if(oe.getSide() == 1){
                buyList.add(oe);
            }else{
                sellList.add(oe);
            }
        }
    }

    private void createStockTableHeading(PdfPTable table){
        table.addCell(new Phrase("Stock Code",DEFAULT_BOLD));
        table.addCell(new Phrase("MRS",DEFAULT_BOLD));
        table.addCell(new Phrase("Tick Size",DEFAULT_BOLD));
    }

    private void createTradesTableHeading(PdfPTable table){
        table.addCell(new Phrase("Trade ID",DEFAULT_BOLD));
        table.addCell(new Phrase("Price",DEFAULT_BOLD));
        table.addCell(new Phrase("Quantity",DEFAULT_BOLD));
    }

    private void createTableHeading(PdfPTable table){
        table.addCell(new Phrase("Order",DEFAULT_BOLD));
        table.addCell(new Phrase("Type",DEFAULT_BOLD));
        table.addCell(new Phrase("MES",DEFAULT_BOLD));
        table.addCell(new Phrase("Time",DEFAULT_BOLD));
        table.addCell(new Phrase("Size",DEFAULT_BOLD));
        table.addCell(new Phrase("Price",DEFAULT_BOLD));
    }

    private void createSectionTableHeading(PdfPTable table){
        table.addCell(new Phrase("Test No.",DEFAULT_BOLD));
        table.addCell(new Phrase("Description",DEFAULT_BOLD));
    }

    public static void main(String[] args) throws Exception {
        CrossingReportGenerator crossingReportGenerator = new CrossingReportGenerator();
        crossingReportGenerator.generate();

    }

    /**
     * Inner class to add a table as header.
     */
    class TableHeader extends PdfPageEventHelper {
        /** The header text. */
        String header;
        /** The template with the total number of pages. */
        PdfTemplate total;

        /**
         * Allows us to change the content of the header.
         * @param header The new header String
         */
        public void setHeader(String header) {
            this.header = header;
        }

        /**
         * Creates the PdfTemplate that will hold the total number of pages.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onOpenDocument(PdfWriter writer, Document document) {
            total = writer.getDirectContent().createTemplate(30, 16);
        }

        /**
         * Adds a header to every page
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onEndPage(PdfWriter writer, Document document) {
            if(writer.getPageNumber() > 2) {
                PdfPTable table = new PdfPTable(3);
                try {
                    table.setWidths(new int[]{24, 24, 2});
                    table.setTotalWidth(527);
                    table.setLockedWidth(true);
                    table.getDefaultCell().setFixedHeight(20);
                    table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                    table.addCell(header);
                    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(String.format("Page %d of", writer.getPageNumber()));
                    PdfPCell cell = new PdfPCell(Image.getInstance(total));
                    cell.setBorder(Rectangle.BOTTOM);
                    table.addCell(cell);
                    table.writeSelectedRows(0, -1, 34, 803, writer.getDirectContent());
                } catch (DocumentException de) {
                    throw new ExceptionConverter(de);
                }
            }
        }

        /**
         * Fills out the total number of pages before the document is closed.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(writer.getPageNumber() - 1)),
                    2, 2, 0);
        }
    }
}
