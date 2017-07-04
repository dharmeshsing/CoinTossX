package wicket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;

/**
 * Created by dharmeshsing on 5/04/16.
 */
public class HomePage extends WebPage{
    public HomePage(){

        add(new Form<Void>("getStartedForm"){
            @Override
            protected void onSubmit() {
                setResponsePage(AllStocksPage.class);
            }
        });
    }
}
