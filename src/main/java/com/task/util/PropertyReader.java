package com.task.util;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class PropertyReader {

    static final String SERVER_PROPERTIES_PATH = "server.properties";

    public static Set<Map.Entry<Object, Object>> getServicesNameByPropertyFile() throws IOException {
        InputStream inputStream = null;
        Properties prop = new Properties();

        try {
            inputStream = PropertyReader.class.getClassLoader().getResourceAsStream(SERVER_PROPERTIES_PATH);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + SERVER_PROPERTIES_PATH + "' not found in the classpath");
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return prop.entrySet();
    }
}


