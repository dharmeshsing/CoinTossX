package wicket;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import vo.StockVO;

/**
 * Created by dharmeshsing on 1/05/16.
 */
public class AllStocksActionPanel extends Panel
{
    public AllStocksActionPanel(String id, final IModel model, final Item item)
    {
        super(id, model);

        add(new Form<Void>("viewForm"){
            @Override
            protected void onSubmit() {
                StockVO stockVO = (StockVO)this.getParent().getDefaultModel().getObject();
                PageParameters pageParameters = new PageParameters();
                pageParameters.set("securityId",stockVO.getSecurityId());
                pageParameters.set("stockCode",stockVO.getStockCode());

                setResponsePage(LimitOrderBookViewPage.class, pageParameters);
            }
        });
    }
}