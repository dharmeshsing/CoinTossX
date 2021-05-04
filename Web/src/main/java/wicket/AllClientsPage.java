package wicket;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.*;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.CSVDataExporter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.ExportToolbar;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import vo.ClientVO;
import wicket.dataProvider.ClientDataProvider;
import wicket.services.Services;

import java.util.ArrayList;
import java.util.List;

public class AllClientsPage extends WebPage {

    private ClientDataProvider clientDataProvider;

    @SpringBean
    private Services services;

    public AllClientsPage(){

        try {
            clientDataProvider = services.getClientDataProvider();

            getClientTable();
            addLinks();
            addForm();
        }catch(Exception e){
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

    private void addForm(){
        final TextField<Integer> idTextField = new TextField<>("compId");
        final TextField<String> passwordTextField = new TextField<>("password");
        final TextField<String> tradingInputURLTextField = new TextField<>("tradingInputURL");
        final TextField<Integer> tradingInputStreamIdTextField = new TextField<>("tradingInputStreamId");
        final TextField<String> tradingOutputURLTextField = new TextField<>("tradingOutputURL");
        final TextField<Integer> tradingOutputStreamIdTextField = new TextField<>("tradingOutputStreamId");
        final TextField<String> marketDataInputURLTextField = new TextField<>("marketDataInputURL");
        final TextField<Integer> marketDataInputStreamIDTextField = new TextField<>("marketDataInputStreamId");
        final TextField<String> marketDataOutputURLTextField = new TextField<>("marketDataOutputURL");
        final TextField<Integer> marketDataOutputStreamIDTextField = new TextField<>("marketDataOutputStreamId");


        Form form = new Form("clientForm"){
            @Override
            protected void onSubmit() {
                ClientVO clientVO = new ClientVO();
                clientVO.setCompId(idTextField.getModelObject());
                clientVO.setPassword(passwordTextField.getModelObject());
                clientVO.setTradingInputURL(tradingInputURLTextField.getModelObject());
                clientVO.setTradingInputStreamId(tradingInputStreamIdTextField.getModelObject());

                clientVO.setTradingOutputURL(tradingOutputURLTextField.getModelObject());
                clientVO.setTradingOutputStreamId(tradingOutputStreamIdTextField.getModelObject());
                clientVO.setMarketDataInputURL(marketDataInputURLTextField.getModelObject());
                clientVO.setMarketDataInputStreamId(marketDataInputStreamIDTextField.getModelObject());
                clientVO.setMarketDataOutputURL(marketDataOutputURLTextField.getModelObject());
                clientVO.setMarketDataOutputStreamId(marketDataOutputStreamIDTextField.getModelObject());

                try {
                    services.addClient(clientVO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        form.setDefaultModel(new CompoundPropertyModel(new ClientVO()));
        form.add(idTextField);
        form.add(passwordTextField);
        form.add(tradingInputURLTextField);
        form.add(tradingInputStreamIdTextField);
        form.add(tradingOutputURLTextField);
        form.add(tradingOutputStreamIdTextField);
        form.add(marketDataInputURLTextField);
        form.add(marketDataInputStreamIDTextField);
        form.add(marketDataOutputURLTextField);
        form.add(marketDataOutputStreamIDTextField);

        add(form);
    }

    private void getClientTable() throws Exception {
        List<IColumn<ClientVO, String>> columns = new ArrayList<>();

        columns.add(new AbstractColumn(new Model("")) {
            public void populateItem(Item cellItem, String componentId, IModel model) {
                cellItem.add(new ActionPanel(componentId, model, cellItem));
            }
        });


        columns.add(new PropertyColumn<ClientVO, String>(new Model<>("Id"), "compId") {
            @Override
            public String getCssClass()
            {
                return "numeric";
            }
        });

        columns.add(new PropertyColumn<>(new Model<>("Password"), "password"));
        columns.add(new PropertyColumn<>(new Model<>("Trading Input URL"), "tradingInputURL"));
        columns.add(new PropertyColumn<>(new Model<>("Trading Input StreamID"), "tradingInputStreamId"));
        columns.add(new PropertyColumn<>(new Model<>("Trading Output URL"), "tradingOutputURL"));
        columns.add(new PropertyColumn<>(new Model<>("Trading Output StreamID"), "tradingOutputStreamId"));
        columns.add(new PropertyColumn<>(new Model<>("MarketData Input URL"), "marketDataInputURL"));
        columns.add(new PropertyColumn<>(new Model<>("MarketData Input StreamID"), "marketDataInputStreamId"));
        columns.add(new PropertyColumn<>(new Model<>("MarketData Output URL"), "marketDataOutputURL"));
        columns.add(new PropertyColumn<>(new Model<>("MarketData Output StreamID"), "marketDataOutputStreamId"));

        clientDataProvider.setSort("compId", SortOrder.DESCENDING);

        DataTable<ClientVO, String> clientDataTable = new DefaultDataTable<>("clientTable", columns,
                clientDataProvider, 10);

        clientDataTable.addBottomToolbar(new ExportToolbar(clientDataTable).addDataExporter(new CSVDataExporter()));
        clientDataTable.setOutputMarkupId(true);

        add(clientDataTable);
    }
}
