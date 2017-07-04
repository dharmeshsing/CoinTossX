package vo;

import java.io.Serializable;

/**
 * Created by dharmeshsing on 29/04/16.
 */
public class ClientVO implements Serializable {
    private int compId;
    private String password;
    private String tradingInputURL;
    private int tradingInputStreamId;
    private String tradingOutputURL;
    private int tradingOutputStreamId;
    private String marketDataInputURL;
    private int marketDataInputStreamId;
    private String marketDataOutputURL;
    private int marketDataOutputStreamId;
    private int securityId;

    private String statusText;
    private String statusClass;
    private boolean simulationComplete;
    private boolean warmUpComplete;

    public int getCompId() {
        return compId;
    }

    public void setCompId(int compId) {
        this.compId = compId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTradingInputURL() {
        return tradingInputURL;
    }

    public void setTradingInputURL(String tradingInputURL) {
        this.tradingInputURL = tradingInputURL;
    }

    public int getTradingInputStreamId() {
        return tradingInputStreamId;
    }

    public void setTradingInputStreamId(int tradingInputStreamId) {
        this.tradingInputStreamId = tradingInputStreamId;
    }

    public String getMarketDataOutputURL() {
        return marketDataOutputURL;
    }

    public void setMarketDataOutputURL(String marketDataOutputURL) {
        this.marketDataOutputURL = marketDataOutputURL;
    }

    public int getMarketDataOutputStreamId() {
        return marketDataOutputStreamId;
    }

    public void setMarketDataOutputStreamId(int marketDataOutputStreamId) {
        this.marketDataOutputStreamId = marketDataOutputStreamId;
    }

    public String getTradingOutputURL() {
        return tradingOutputURL;
    }

    public void setTradingOutputURL(String tradingOutputURL) {
        this.tradingOutputURL = tradingOutputURL;
    }

    public int getTradingOutputStreamId() {
        return tradingOutputStreamId;
    }

    public void setTradingOutputStreamId(int tradingOutputStreamId) {
        this.tradingOutputStreamId = tradingOutputStreamId;
    }

    @Override
    public String toString() {
        return compId + "," +
                password + "," +
                tradingInputURL + "," +
                tradingInputStreamId + "," +
                tradingOutputURL + "," +
                tradingOutputStreamId + "," +
                marketDataInputURL + "," +
                marketDataInputStreamId + "," +
                marketDataOutputURL + "," +
                marketDataOutputStreamId + "," +
                securityId;
    }

    public int getSecurityId() {
        return securityId;
    }

    public void setSecurityId(int securityId) {
        this.securityId = securityId;
    }

    public String getStatusText(){
        return statusText;
    }

    public String getStatusClass(){
        return statusClass;
    }

    public void setStatusText(String statusText){
        this.statusText = statusText;
    }

    public void setStatusClass(String statusClass){
        this.statusClass = statusClass;
    }


    public boolean isSimulationComplete() {
        return simulationComplete;
    }

    public void setSimulationComplete(boolean simulationComplete) {
        this.simulationComplete = simulationComplete;
    }

    public boolean isWarmUpComplete() {
        return warmUpComplete;
    }

    public void setWarmUpComplete(boolean warmUpComplete) {
        this.warmUpComplete = warmUpComplete;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientVO clientVO = (ClientVO) o;

        return compId == clientVO.compId;

    }

    @Override
    public int hashCode() {
        return compId;
    }

    public String getMarketDataInputURL() {
        return marketDataInputURL;
    }

    public void setMarketDataInputURL(String marketDataInputURL) {
        this.marketDataInputURL = marketDataInputURL;
    }

    public int getMarketDataInputStreamId() {
        return marketDataInputStreamId;
    }

    public void setMarketDataInputStreamId(int marketDataInputStreamId) {
        this.marketDataInputStreamId = marketDataInputStreamId;
    }
}
