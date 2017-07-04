package wicket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;

/**
 * Created by dharmeshsing on 17/04/16.
 */
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
