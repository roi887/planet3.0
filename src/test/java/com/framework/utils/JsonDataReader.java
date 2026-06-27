package com.framework.utils;

import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.InputStream;

public class JsonDataReader {

    public static String getTestData(String fileName, String key) {
        try (InputStream is = JsonDataReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                // This line prints the hidden path Java is searching during runtime execution
                System.out.println("DEBUG: Classpath lookup failed! Looking into target directory root for: " + fileName);
                throw new RuntimeException("Could not find json data file: " + fileName);
            }
            JSONTokener tokener = new JSONTokener(is);
            JSONObject jsonObject = new JSONObject(tokener);

            if (key.contains(".")) {
                String[] parts = key.split("\\.");
                return jsonObject.getJSONObject(parts[0]).getString(parts[1]);
            }

            return jsonObject.getString(key);
        } catch (Exception e) {
            throw new RuntimeException("Error reading key '" + key + "' from JSON file: " + e.getMessage());
        }
    }
}