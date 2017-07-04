package wicket;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import vo.ClientVO;

/**
 * Created by dharmeshsing on 1/05/16.
 */
public class ActionPanel extends Panel
{
    public ActionPanel(String id, final IModel model, final Item item)
    {
        super(id, model);
        Link edit = new Link("edit") {
            @Override
            public void onClick() {
               ClientVO clientVO = (ClientVO)getParent().getDefaultModelObject();
            }
        };

        add(edit);
    }
}