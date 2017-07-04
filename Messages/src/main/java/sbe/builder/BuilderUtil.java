package sbe.builder;

/**
 * Created by dharmeshsing on 14/08/15.
 */
public class BuilderUtil {

    public static String fill(String value,int capacity){
        int length = value.length();
        for(int i=length; i<capacity; i++){
            value += " ";
        }
        return value;
    }
}
