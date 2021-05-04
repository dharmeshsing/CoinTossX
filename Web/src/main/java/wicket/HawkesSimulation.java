package wicket;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.*;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import vo.ClientVO;
import wicket.dataProvider.ClientDataProvider;
import wicket.services.Services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HawkesSimulation extends WebPage {

    private ClientDataProvider clientDataProvider;
    private AjaxSelfUpdatingTimerBehavior timer;

    @SpringBean
    private Services services;

    public HawkesSimulation(){
        try {
            clientDataProvider = services.getClientDataProvider();

            buildTable();
            addButtons();
            addTimer();
            addLinks();

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

    private void addTimer(){
        timer = new AjaxSelfUpdatingTimerBehavior(Duration.seconds(3)){

            @Override
            protected final void onPostProcessTarget(AjaxRequestTarget target) {
                services.updateClientSimulationStatus();

                if((services.isWarmupStarted() && services.isWarmUpComplete()) ||
                    services.isSimulationStarted() && services.isSimulationIsComplete()) {
                    timer.stop(target);
                }
            }
        };

        add(timer);
    }

    private void addButtons() {
        Form form = new Form("startSimulationForm");

        form.add(new AjaxButton("warmUpButton",Model.of("Warmup")){
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                try {
//                    services.clear();
                    warmpSimulation();
                    timer.restart(target);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        form.add(new AjaxButton("startSimulationButton",Model.of("Start")){
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                try {
//                    services.clear();
                    startSimulation();
                    timer.restart(target);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        form.add(new AjaxButton("stopSimulationButton",Model.of("Stop")){
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if(services.isWarmupStarted()){
                    services.stopWarmup();
                }else if(services.isSimulationStarted()){
                    services.stopSimulation();
                }

//                services.clearClientCounter();
                timer.stop(target);
            }
        });

        form.add(new AjaxButton("shutDownButton",Model.of("ShutDown")){
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                try {
                    stopSimulation();
//                    services.clearClientCounter();
                    timer.stop(target);
                    services.stop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        add(form);
    }

    private void startSimulation() throws IOException {
        services.startSimulation();
        WebSession.get().setAttribute("isWarmup", null);
    }

    private void warmpSimulation() throws IOException {
        services.warmpSimulation();
        WebSession.get().setAttribute("isWarmup", "true");
    }

    private void stopSimulation() throws IOException {
        services. stopSimulation();
        WebSession.get().setAttribute("isWarmup", null);
        services.updateLabelClass();
    }

    private void buildTable() throws Exception {
        List<IColumn<ClientVO, String>> columns = new ArrayList<>();

        columns.add(new PropertyColumn<>(new Model<>("Client Id"), "compId"));
        columns.add(new PropertyColumn<>(new Model<>("Stock"), "securityId"));

        columns.add(new AbstractColumn(new Model("Status")) {
            public void populateItem(Item cellItem, String componentId, IModel model) {
                cellItem.add(new HawkesDisplayPanel(componentId, model, cellItem));
            }
        });

        columns.add(new AbstractColumn(new Model("Trading Session")) {
            public void populateItem(Item cellItem, String componentId, IModel model) {
                cellItem.add(new StockTradingSessionPanel(componentId, model, cellItem));
            }
        });


        clientDataProvider.setSort("compId", SortOrder.DESCENDING);

        DataTable<ClientVO, String> clientDataTable = new DefaultDataTable<>("clientTable", columns,
                clientDataProvider, 10);

        clientDataTable.setOutputMarkupId(true);
        add(clientDataTable);
    }
}
