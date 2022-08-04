package Config;

import protocol.Serializer;

import java.util.Properties;

public class Config {
    static Properties properties;
    public static Serializer.Algorithm getSerializerAlgorithm(){
        String value= properties.getProperty("serializer.algorithm");
        if(value==null){
            return Serializer.Algorithm.Java;
        }else{
            return Serializer.Algorithm.valueOf(value);
        }
    }
}
