package wicket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;

public class LatencyPage extends WebPage {

    public LatencyPage(){
        add(new Link("simulationLink"){
            @Override
            public void onClick() {
                setResponsePage(HawkesSimulation.class);
            }
        });
    }
}
