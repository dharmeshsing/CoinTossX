package wicket;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import vo.ClientVO;
import vo.StockVO;
import wicket.services.Services;

/**
 * Created by dharmeshsing on 1/05/16.
 */
public class StockTradingSessionPanel extends Panel {
    @SpringBean
    private Services services;

    public StockTradingSessionPanel(String id, final IModel model, final Item item) {
        super(id, model);

        StockVO stockVO;
        if(this.getDefaultModel().getObject() instanceof StockVO) {
            stockVO = (StockVO) this.getDefaultModel().getObject();
        }else{
            ClientVO clientVO = (ClientVO) this.getDefaultModel().getObject();
            stockVO = services.getStockVO(clientVO.getSecurityId());
        }
        Model<String> status = Model.of(stockVO.getStatusText());

        Label statusLabel = new Label("tradingSessionLabel", status);
        statusLabel.setOutputMarkupId(true);
        statusLabel.add(new AttributeAppender("class", "label label-info", ";"));

        add(statusLabel);
    }
}