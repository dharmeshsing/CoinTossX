package wicket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;

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
