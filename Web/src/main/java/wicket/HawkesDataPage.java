package wicket;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import wicket.services.Services;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

public class HawkesDataPage extends WebPage {

    private String lambda;
    private String alpha;
    private String beta;
    private int horizon;

    @SpringBean
    private Services services;

    public HawkesDataPage(){

        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final TextField<Integer> horizonTextField = new TextField<>("horizon", Model.of(horizon));
        horizonTextField.setRequired(true);

        final TextField<String> lambdaTextField = new TextField<>("lambda", Model.of(lambda));
        lambdaTextField.setRequired(true);


        final TextArea<String> alphaTextArea = new TextArea<>("alpha",Model.of(alpha));
        alphaTextArea.setRequired(true);


        final TextArea<String> betaTextArea = new TextArea<>("beta",Model.of(beta));
        betaTextArea.setRequired(true);

        AjaxFormComponentUpdatingBehavior changeHorizonText = new AjaxFormComponentUpdatingBehavior ("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                horizon = horizonTextField.getModelObject();
            }
        };

        AjaxFormComponentUpdatingBehavior changeLambdaText = new AjaxFormComponentUpdatingBehavior ("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                lambda = lambdaTextField.getModelObject();
            }
        };

        AjaxFormComponentUpdatingBehavior changeAlphaText = new AjaxFormComponentUpdatingBehavior ("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                alpha = alphaTextArea.getModelObject();
            }
        };

        AjaxFormComponentUpdatingBehavior changeBetaText = new AjaxFormComponentUpdatingBehavior ("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                beta = betaTextArea.getModelObject();
            }
        };
        horizonTextField.add(changeHorizonText);
        lambdaTextField.add(changeLambdaText);
        alphaTextArea.add(changeAlphaText);
        betaTextArea.add(changeBetaText);


        Form<?> form = new Form<Void>("form"){
            @Override
            protected void onSubmit() {
                horizon = horizonTextField.getModelObject();
                lambda = lambdaTextField.getModelObject();
                alpha = alphaTextArea.getModelObject();
                beta = betaTextArea.getModelObject();
            }
        };

        form.add(horizonTextField);
        form.add(lambdaTextField);
        form.add(alphaTextArea);
        form.add(betaTextArea);
        add(form);

        form.add(new Button("generateLink"){
            @Override
            public void onSubmit() {
                horizon = horizonTextField.getModelObject();
                lambda = lambdaTextField.getModelObject();
                alpha = alphaTextArea.getModelObject();
                beta = betaTextArea.getModelObject();

                PageParameters pageParameters = new PageParameters();
                pageParameters.set("horizon",horizon);
                pageParameters.set("lambda",lambda);
                pageParameters.set("alpha",alpha);
                pageParameters.set("beta",beta);

                setResponsePage(Charts.class,pageParameters);
            }
        });

        form.add(new Button("saveLink"){
            @Override
            public void onSubmit() {
                try{
                    horizon = horizonTextField.getModelObject();
                    lambda = lambdaTextField.getModelObject();
                    alpha = alphaTextArea.getModelObject();
                    beta = betaTextArea.getModelObject();

                    Properties prop = new Properties();
                    prop.setProperty("HORIZON", String.valueOf(horizon));
                    prop.setProperty("LAMBDA", lambda);
                    prop.setProperty("ALPHA", alpha);
                    prop.setProperty("BETA",beta);

                    String dataPath = services.getDataPath();
                    FileOutputStream fr=new FileOutputStream(new File(dataPath + File.separator + "hawkesData.properties"));
                    prop.store(fr,"Properties");
                    fr.close();

                    WebSession.get().setAttribute("horizon",horizon);
                    WebSession.get().setAttribute("lambda",lambda);
                    WebSession.get().setAttribute("alpha",alpha);
                    WebSession.get().setAttribute("beta",beta);

                }catch(Exception e){
                    e.printStackTrace();
                }

                setResponsePage(HawkesDataPage.class);
            }
        });

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

    private void init() throws Exception {
        String dataFile = services.getDataPath()+ File.separator + "hawkesData.properties";
        services.loadPropertiesFromFile(dataFile);
        Properties hawkesProps = services.getProperties();

        lambda = (String)hawkesProps.get("LAMBDA");
        alpha = (String)hawkesProps.get("ALPHA");
        beta = (String)hawkesProps.get("BETA");
        horizon = Integer.parseInt((String)hawkesProps.get("HORIZON"));
    }
}
