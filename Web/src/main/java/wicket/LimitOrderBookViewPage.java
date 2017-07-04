package wicket;

import com.googlecode.wickedcharts.wicket7.highcharts.Chart;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.CSVDataExporter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.ExportToolbar;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import vo.OrderVO;
import vo.TradeVO;
import wicket.dataProvider.OrderDataProvider;
import wicket.dataProvider.TradeDataProvider;
import wicket.services.Services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * Created by dharmeshsing on 7/05/16.
 */
public class LimitOrderBookViewPage extends WebPage {

    private AjaxSelfUpdatingTimerBehavior timer;

    private OrderDataProvider bidDataProvider;
    private OrderDataProvider offerDataProvider;
    private TradeDataProvider tradeDataProvider;

    @SpringBean
    private Services services;
    private Properties properties;

    private int securityId;
    private String stockCode;

    public LimitOrderBookViewPage(PageParameters pageParameters) {

        try {
            securityId = Integer.parseInt(pageParameters.get("securityId").toString());
            stockCode = pageParameters.get("stockCode").toString();

            bidDataProvider = new OrderDataProvider();
            offerDataProvider = new OrderDataProvider();

            Collection<OrderVO> bids = services.getOffHeapStorage().getBidOrders(securityId);
            if(bids == null || bids.size() == 0) {
                services.sendSnapShotRequest(services.getSnapShotRequest(securityId));
            }

            Chart lobChart = new Chart("lobChart", services.getChart(securityId, stockCode));
            lobChart.setOutputMarkupId(true);
            add(lobChart);

            getBidLOBTable();
            getOfferLOBTable();
            getOrderHistoryTable();
            getTradeTable();

            timer = new AjaxSelfUpdatingTimerBehavior(Duration.minutes(2)){

                @Override
                protected final void onPostProcessTarget(AjaxRequestTarget target) {
                    try {
                        lobChart.setOptions(services.getChart(securityId,stockCode));
                        services.sendSnapShotRequest(services.getSnapShotRequest(securityId));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };


            add(timer);
            addLinks();

        } catch (Exception e) {
            e.printStackTrace();
        }
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

        add(new Link("simulationLink"){
            @Override
            public void onClick() {
                setResponsePage(HawkesSimulation.class);
            }
        });

        add(new Link("hawkesDataLink"){
            @Override
            public void onClick() {
                setResponsePage(HawkesDataPage.class);
            }
        });
    }

    private void getBidLOBTable(){
        List<IColumn<OrderVO, String>> columns = new ArrayList<>();

        columns.add(new PropertyColumn<OrderVO, String>(new Model<>("Order Id"), "orderId") {
            @Override
            public String getCssClass()
            {
                return "numeric";
            }
        });

        columns.add(new PropertyColumn<OrderVO, String>(new Model<>("Price"), "price", "price"){
            @Override
            public String getCssClass()
            {
                return "numeric";
            }
        });

        columns.add(new PropertyColumn<OrderVO, String>(new Model<>("Volume"), "volume", "volume"){
            @Override
            public String getCssClass()
            {
                return "numeric";
            }
        });

        offerDataProvider = new OrderDataProvider(services.getOffHeapStorage().getBidOrders(securityId));
        bidDataProvider.setSort("price", SortOrder.DESCENDING);

        DataTable<OrderVO, String> bidDataTable = new DefaultDataTable<>("bidLobTable", columns,
                bidDataProvider, 10);

        bidDataTable.addBottomToolbar(new ExportToolbar(bidDataTable).addDataExporter(new CSVDataExporter()));
        bidDataTable.setOutputMarkupId(true);
        add(bidDataTable);
    }

    private void getOfferLOBTable(){
        List<IColumn<OrderVO, String>> columns = new ArrayList<>();

        columns.add(new PropertyColumn<OrderVO, String>(new Model<>("Order Id"), "orderId") {
            @Override
            public String getCssClass()
            {
                return "numeric";
            }
        });

        columns.add(new PropertyColumn<OrderVO, String>(new Model<>("Price"), "price", "price"){
            @Override
            public String getCssClass()
            {
                return "numeric";
            }
        });

        columns.add(new PropertyColumn<OrderVO, String>(new Model<>("Volume"), "volume", "volume"){
            @Override
            public String getCssClass()
            {
                return "numeric";
            }
        });

        offerDataProvider = new OrderDataProvider(services.getOffHeapStorage().getOfferOrders(securityId));
        offerDataProvider.setSort("price", SortOrder.ASCENDING);

        DataTable<OrderVO, String> offerDataTable = new DefaultDataTable<>("offerLobTable", columns,
                offerDataProvider, 10);

        offerDataTable.addBottomToolbar(new ExportToolbar(offerDataTable).addDataExporter(new CSVDataExporter()));
        offerDataTable.setOutputMarkupId(true);
        add(offerDataTable);
    }

    private void getOrderHistoryTable(){
        List<IColumn<OrderVO, String>> columns = new ArrayList<>();

        columns.add(new PropertyColumn<OrderVO, String>(new Model<>("Time"), "formattedTime"));

        columns.add(new PropertyColumn<OrderVO, String>(new Model<>("Id"), "orderId") {
            @Override
            public String getCssClass()
            {
                return "numeric";
            }
        });

        columns.add(new PropertyColumn<OrderVO, String>(new Model<>("Price"), "price", "price"){
            @Override
            public String getCssClass()
            {
                return "numeric";
            }
        });

        columns.add(new PropertyColumn<OrderVO, String>(new Model<>("Volume"), "volume", "volume"){
            @Override
            public String getCssClass()
            {
                return "numeric";
            }
        });

        columns.add(new PropertyColumn<OrderVO, String>(new Model<>("Type"), "side"));

        OrderDataProvider orderDataProvider = new OrderDataProvider(services.getOffHeapStorage().getSubmittedOrders(securityId));
        orderDataProvider.setSort("orderId", SortOrder.DESCENDING);

        DataTable<OrderVO, String> orderHistoryDataTable = new DefaultDataTable<>("orderHistoryTable", columns,
                orderDataProvider, 10);

        orderHistoryDataTable.addBottomToolbar(new ExportToolbar(orderHistoryDataTable).addDataExporter(new CSVDataExporter()));
        orderHistoryDataTable.setOutputMarkupId(true);
        add(orderHistoryDataTable);
    }

    private void getTradeTable(){
        List<IColumn<TradeVO, String>> columns = new ArrayList<>();
        columns.add(new PropertyColumn<TradeVO,String>(new Model<>("Trade Id"), "tradeId"){
            @Override
            public String getCssClass()
            {
                return "numeric";
            }
        });
        columns.add(new PropertyColumn<>(new Model<>("Price"), "price"));
        columns.add(new PropertyColumn<>(new Model<>("Quantity"), "quantity"));

        tradeDataProvider = new TradeDataProvider(services.getOffHeapStorage().getTrades(securityId));
        tradeDataProvider.setSort("tradeId", SortOrder.DESCENDING);

        DataTable<TradeVO, String> tradeDataTable = new DefaultDataTable<>("tradeTable", columns,
                tradeDataProvider, 10);

        tradeDataTable.addBottomToolbar(new ExportToolbar(tradeDataTable).addDataExporter(new CSVDataExporter()));
        tradeDataTable.setOutputMarkupId(true);
        add(tradeDataTable);
    }
}
