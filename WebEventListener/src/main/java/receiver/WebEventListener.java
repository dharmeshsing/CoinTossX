package receiver;


import dao.OffHeapStorage;
import util.CommonUtil;

import java.io.File;
import java.util.Properties;

/**
 * Created by dharmeshsing on 23/12/16.
 */
public class WebEventListener {

    private OffHeapStorage offHeapStorage;
    private Properties properties;
    private CommonUtil commonUtil;

    private String mediaDriverConextDir;
    private MulticastProcessor multicastProcessor;
    private Thread multicastProcessorThread;
    private MDGClientProcessor mdgClientProcessor;
    private Thread mdgClientProcessorThread;

    public void init(){
        try {
            commonUtil = new CommonUtil();
            loadProperties();
            mediaDriverConextDir = properties.getProperty("MEDIA_DRIVER_DIR") + File.separator + "web";
            initOffHeapStorage();
            initMulticastProcessor();
            initMDGClientProcessor();
        }catch(Exception e){
            throw new RuntimeException("Unable to init web event listener",e);
        }
    }

    private void initMulticastProcessor(){
        multicastProcessor = new MulticastProcessor(offHeapStorage,properties,mediaDriverConextDir);
        multicastProcessor.init();
        multicastProcessorThread = new Thread(multicastProcessor);

    }

    private void initMDGClientProcessor(){
        mdgClientProcessor = new MDGClientProcessor(offHeapStorage,properties,mediaDriverConextDir);
        mdgClientProcessor.init();
        mdgClientProcessorThread = new Thread(mdgClientProcessor);
    }

    private void loadProperties(){
        properties = new Properties();
        commonUtil.loadProperties(properties,"WebEventListener.properties");
    }

    private void initOffHeapStorage() throws Exception {
        offHeapStorage = new OffHeapStorage();
        offHeapStorage.init(getDataPath(),false);
    }

    public String getDataPath(){
        return properties.get("DATA_PATH").toString();
    }

    public void start() {
        System.out.println("Web EventListener Started");
        multicastProcessorThread.start();
        mdgClientProcessorThread.start();
    }

    public static void main(String[] args) {
        WebEventListener webEventListener = new WebEventListener();
        webEventListener.init();
        webEventListener.start();

    }
}
