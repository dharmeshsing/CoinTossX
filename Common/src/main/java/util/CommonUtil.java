package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * Created by dharmeshsing on 13/12/16.
 */
public class CommonUtil implements Serializable {

    public static DateTimeFormatter creationTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public void loadProperties(Properties properties,String propertiesFile) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFile)) {
            properties.load(inputStream);
        }catch(Exception e){
            throw new RuntimeException("Unable to load properties file " + propertiesFile,e);
        }
    }

    public static void loadProperties(Properties properties,File propertiesFile) {
        try (InputStream inputStream = new FileInputStream(propertiesFile)) {
            properties.load(inputStream);
        }catch(Exception e){
            throw new RuntimeException("Unable to load properties file " + propertiesFile,e);
        }
    }
}
