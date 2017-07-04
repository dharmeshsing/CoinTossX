package wicket;

import com.giffing.wicket.spring.boot.starter.app.WicketBootWebApplication;
import com.giffing.wicket.spring.boot.starter.context.WicketSpringBootApplication;
import org.apache.wicket.Page;
import org.apache.wicket.bean.validation.BeanValidationConfiguration;
import org.apache.wicket.protocol.http.CsrfPreventionRequestCycleListener;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by dharmeshsing on 19/03/16.
 */
@WicketSpringBootApplication
@EnableAsync
@EnableScheduling
public class WicketApplication extends WicketBootWebApplication {

    public static void main(String[] args) throws Exception {
        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder();
        springApplicationBuilder.sources(WicketApplication.class).run(args);
    }

    @Override
    public void init(){
        super.init();
        getRequestCycleListeners().add(new CsrfPreventionRequestCycleListener());
        new BeanValidationConfiguration().configure(this);
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }

}
