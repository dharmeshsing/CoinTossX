package sbe.builder;

public class BuilderUtil {

    public static String fill(String value,int capacity){
        int length = value.length();
        for(int i=length; i<capacity; i++){
            value += " ";
        }
        return value;
    }
}
