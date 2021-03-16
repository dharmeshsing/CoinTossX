package wicket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import sbe.msg.marketData.TradingSessionEnum;
import wicket.services.Services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@EnableScheduling
public class AppConfig implements SchedulingConfigurer {

    @Autowired
    private Services services;
    private Properties tradingSessionProps;
    private static final String TRADING_SESSION_PROPS = "tradingSessionsCron.properties";

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        String dataPath = services.getProperties().get("DATA_PATH").toString();
        try {
            loadProperties(dataPath + File.separator + TRADING_SESSION_PROPS);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load " + TRADING_SESSION_PROPS);
        }

        if(tradingSessionProps.get("TRADING_SESSIONS") != null) {
            String[] tradingSessions = tradingSessionProps.get("TRADING_SESSIONS").toString().split(",");
            for (String session : tradingSessions) {
                TradingSessionEnum tradingSessionEnum = getTradingSessionEnum(tradingSessionProps.get(session + "." + "name").toString());
                String cron = tradingSessionProps.get(session + "." + "cron").toString();

                System.out.println(cron + " configured");
                taskRegistrar.addCronTask(new CronTask(() -> {
                    services.changeTradingSession(tradingSessionEnum);
                }, cron));
            }
        }

    }

    private TradingSessionEnum getTradingSessionEnum(String sessionName){
        switch(sessionName){
            case "StartOfTrading": return TradingSessionEnum.StartOfTrading;
            case "OpeningAuctionCall": return TradingSessionEnum.OpeningAuctionCall;
            case "ContinuousTrading": return TradingSessionEnum.ContinuousTrading;
            case "FCOAuctionCall": return TradingSessionEnum.FCOAuctionCall;
            case "VolatilityAuctionCall": return TradingSessionEnum.VolatilityAuctionCall;
            case "IntraDayAuctionCall": return TradingSessionEnum.IntraDayAuctionCall;
            case "ClosingAuctionCall": return TradingSessionEnum.ClosingAuctionCall;
            case "ClosingPricePublication": return TradingSessionEnum.ClosingPricePublication;
            case "ClosingPriceCross": return TradingSessionEnum.ClosingPriceCross;
            case "PostClose": return TradingSessionEnum.PostClose;
            case "TradeReporting": return TradingSessionEnum.TradeReporting;
            default: throw new RuntimeException(sessionName + " trading session does not exist");
        }
    }


    private void loadProperties(String propertiesFile) throws IOException {
        if(tradingSessionProps == null) {
            tradingSessionProps = new Properties();
        }

        try (InputStream inputStream = new FileInputStream(propertiesFile)) {
            if (inputStream != null) {
                tradingSessionProps.load(inputStream);
            } else {
                throw new RuntimeException("Unable to load properties file " + propertiesFile);
            }
        }
    }
}
