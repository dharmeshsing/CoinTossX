package wicket;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.*;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import vo.StockVO;
import wicket.dataProvider.StockDataProvider;
import wicket.services.Services;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dharmeshsing on 6/05/16.
 */
public class AllStocksPage extends WebPage {

    private StockDataProvider stockDataProvider;
    private AjaxSelfUpdatingTimerBehavior timer;

    @SpringBean
    private Services services;

    public AllStocksPage(){
        try {
            stockDataProvider = services.getStockDataProvider();

            buildTable();
            addLinks();
            services.updateTradingSession();
            addTimer();


        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void addTimer(){
        timer = new AjaxSelfUpdatingTimerBehavior(Duration.seconds(10)){

            @Override
            protected final void onPostProcessTarget(AjaxRequestTarget target) {
                services.updateTradingSession();
            }
        };

        add(timer);
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

    private void buildTable() throws Exception {
        List<IColumn<StockVO, String>> columns = new ArrayList<>();

        columns.add(new PropertyColumn<StockVO, String>(new Model<>("Id"), "securityId") {
            @Override
            public String getCssClass()
            {
                return "numeric";
            }
        });
        columns.add(new PropertyColumn<>(new Model<>("Code"), "stockCode"));
        columns.add(new PropertyColumn<>(new Model<>("Name"), "name"));

        columns.add(new AbstractColumn(new Model("Trading Session")) {
            public void populateItem(Item cellItem, String componentId, IModel model) {
                cellItem.add(new StockTradingSessionPanel(componentId, model, cellItem));
            }
        });

        columns.add(new AbstractColumn(new Model("View")) {
            public void populateItem(Item cellItem, String componentId, IModel model) {
                cellItem.add(new AllStocksActionPanel(componentId, model, cellItem));
            }
        });


        stockDataProvider.setSort("securityId", SortOrder.DESCENDING);

        DataTable<StockVO, String> stockDataTable = new DefaultDataTable<>("stockTable", columns,
                stockDataProvider, 10);

        stockDataTable.setOutputMarkupId(true);

        add(stockDataTable);
    }
}
