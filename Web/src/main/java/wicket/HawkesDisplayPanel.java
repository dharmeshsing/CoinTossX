package wicket;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import vo.ClientVO;

/**
 * Created by dharmeshsing on 6/05/16.
 */
public class HawkesDisplayPanel extends Panel {


    public HawkesDisplayPanel(String id, final IModel model, final Item item) {
        super(id, model);

        ClientVO clientVO = (ClientVO)this.getDefaultModel().getObject();
        Model<String> status = Model.of(clientVO.getStatusText());
        Model<String> statusClass = Model.of(clientVO.getStatusClass());

        Label statusLabel = new Label("statusLabel", status);
        statusLabel.setOutputMarkupId(true);
        statusLabel.add(new AttributeAppender("class", statusClass, ";"));

        add(statusLabel);
    }
}
