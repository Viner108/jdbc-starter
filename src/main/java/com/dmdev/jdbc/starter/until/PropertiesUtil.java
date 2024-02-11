package com.dmdev.jdbc.starter.until;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();

    static {
        loadPropertieds();
    }
    public static String get(String key){
        return PROPERTIES.getProperty(key);
    }

    private static void loadPropertieds() {
        try (InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")){
            PROPERTIES.load(inputStream);
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }

    public PropertiesUtil() {
    }
}
