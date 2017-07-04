package reports;

import com.carrotsearch.hppc.ObjectArrayList;
import leafNode.OrderEntry;
import orderBook.Trade;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dharmeshsing on 19/01/17.
 */
public class LatexCommands {

    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    public static String newPage(){
        return "\\pagebreak{}\n";
    }

    public static String subSection(String title){
        return "\\subsubsection{" + title +"}\n";
    }

    public static String addParagraph(String text){
        return "\\paragraph*{\\textmd{" + text + "}}";
    }

    public static String addHorizontalSpace(){
        return "\n\\\n";
    }

    public static String startTradeTable(){
        return "\\begin{table}[!htb]\n" +
                "\\footnotesize\n" +
                "\\centering\n";
    }

    public static String endTradeTable(){
        return "\\caption*{Trades} \n" +
                "\\label{labelhere}\n" +
                "\\end{table}\n";
    }

    public static StringBuilder addOrderTable(List<OrderEntry> buyOrders,List<OrderEntry> sellOrders,Set<Long> prices,String caption){
        StringBuilder orderTable = new StringBuilder();

        orderTable.append("\\begin{table}[!htb]\n" +
                "\\resizebox{\\textwidth}{!}{%\n" +
                "\\begin{tabular}{l l l l l >{\\columncolor[gray]{0.8}}l l l l l l}\n" +
                "\\toprule \n" +
                "\\multicolumn{5}{c}{\\textbf{Buy}} & & \\multicolumn{5}{c}{\\textbf{Sell}}\\\\\n" +
                "\\textbf{Order} & \\textbf{Type} & \\textbf{MES} & \\textbf{Time} & \\textbf{Size} & \\textbf{Price} & \\textbf{Size} & \\textbf{Time} & \\textbf{MES} & \\textbf{Type} & \\textbf{Order}\\\\\n" +
                "\\midrule\n");

        if(prices.size() == 0){
            orderTable.append("Empty LOB\\\\\n");
        }else {
            prices.stream().forEach(p -> {
                List<OrderEntry> foundBuyOrders = buyOrders.stream().filter(bo -> bo != null && bo.getPrice() == p).collect(Collectors.toList());
                List<OrderEntry> foundSellOrder = sellOrders.stream().filter(so -> so != null && so.getPrice() == p).collect(Collectors.toList());

                Iterator<OrderEntry> buyIt = foundBuyOrders.iterator();
                Iterator<OrderEntry> sellIt = foundSellOrder.iterator();

                while (buyIt.hasNext() || sellIt.hasNext()) {
                    OrderEntry boe = null;
                    OrderEntry soe = null;

                    try {
                        boe = buyIt.next();
                    } catch (NoSuchElementException e) {
                    }

                    try {
                        soe = sellIt.next();
                    } catch (NoSuchElementException e) {
                    }

                    if (boe != null && soe != null) {
                        Date bcurrentDate = Calendar.getInstance().getTime();
                        bcurrentDate.setTime(boe.getSubmittedTime());

                        Date scurrentDate = Calendar.getInstance().getTime();
                        scurrentDate.setTime(soe.getSubmittedTime());

                        orderTable.append(boe.getOrderId() + " & " +
                                getOrderType(boe.getType()) + " & " +
                                boe.getMinExecutionSize() + " & " +
                                sdf.format(bcurrentDate) + " & " +
                                boe.getQuantity() + " & " +
                                p / 100.00 + " & " +
                                soe.getQuantity() + " & " +
                                sdf.format(scurrentDate) + " & " +
                                soe.getMinExecutionSize() + " & " +
                                getOrderType(soe.getType()) + " & " +
                                soe.getOrderId() +
                                "\\\\\n");
                    } else if (boe == null && soe != null) {
                        Date scurrentDate = Calendar.getInstance().getTime();
                        scurrentDate.setTime(soe.getSubmittedTime());

                        orderTable.append(" & & & & & " +
                                p / 100.00 + " & " +
                                soe.getQuantity() + " & " +
                                sdf.format(scurrentDate) + " & " +
                                soe.getMinExecutionSize() + " & " +
                                getOrderType(soe.getType()) + " & " +
                                soe.getOrderId() +
                                "\\\\\n");
                    } else if (boe != null && soe == null) {
                        Date bcurrentDate = Calendar.getInstance().getTime();
                        bcurrentDate.setTime(boe.getSubmittedTime());

                        orderTable.append(boe.getOrderId() + " & " +
                                getOrderType(boe.getType()) + " & " +
                                boe.getMinExecutionSize() + " & " +
                                sdf.format(bcurrentDate) + " & " +
                                boe.getQuantity() + " & " +
                                p / 100.00 +
                                " & & & & & " +
                                "\\\\\n");
                    }
                }

            });
        }


        orderTable.append("\\bottomrule\n" +
                "\\end{tabular}\n" +
                "}\n" +
                "\\caption*{" + caption + "} \n" +
                "\\label{labelhere}\n" +
                "\\end{table}\n");

        return orderTable;
    }

    public static StringBuilder addTradeTable(ObjectArrayList<Trade> trades){
        StringBuilder tradeTable = new StringBuilder();

        tradeTable.append("\\begin{tabular}{l l l}\n" +
                "\\toprule \n" +
                "\\textbf{Trade Id} & \\textbf{Price} & \\textbf{Quanity} \\\\\n" +
                "\\midrule\n");

        Object[] tradeArr = trades.buffer;
        if(tradeArr.length != 0) {
            for (int i = 0; i < tradeArr.length; i++) {
                Trade trade = (Trade) tradeArr[i];
                if (trade != null) {
                    tradeTable.append(trade.getId() + " & " +
                                    trade.getPrice() / 100.00 + " & " +
                                    trade.getQuantity() +
                                    "\\\\\n"
                    );
                }
            }
        }else{
            tradeTable.append("Zero trades executed\\\\\n");
        }

        tradeTable.append("\\bottomrule\n" +
                          "\\end{tabular}\n");

        return tradeTable;
    }

    private static String getOrderType(long type){
        switch ((int)type) {
            case 1 : return "MO";
            case 2 : return "LO";
            case 3 : return "SO";
            case 4 : return "SL";
            case 5 : return "HO";
            default : return "ERROR";
        }
    }
}
