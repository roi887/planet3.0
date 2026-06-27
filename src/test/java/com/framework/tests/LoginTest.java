package com.framework.tests;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import java.io.InputStream;
import java.io.InputStreamReader;

@Listeners(com.framework.listeners.ExtentReportListener.class)
public class LoginTest extends BaseTest {

    @Test
    public void openPlanet() {
        // Step 1: Execute onboarding screens
        loginFlow.onboardAndNavigateToLogin();

        // Step 2: Extract plain-text parameters from your exact JSON file location
        String rawMobileNum = "";
        String plainMpin = "";

        try {
            // Pointing directly to your subfolder path: testdata/users.json
            InputStream is = getClass().getClassLoader().getResourceAsStream("testdata/users.json");

            if (is == null) {
                throw new RuntimeException("Could not find users.json inside your resources/testdata/ folder!");
            }

            // Parse the JSON Object
            JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(is)).getAsJsonObject();
            JsonObject targetObject = jsonObject;

            // Safe Check: Navigate into nested "login" block ONLY if it exists
            if (jsonObject.has("login") && jsonObject.get("login").isJsonObject()) {
                targetObject = jsonObject.getAsJsonObject("login");
                System.out.println("[Data] Found nested 'login' block. Extracting parameters...");
            } else {
                System.out.println("[Data] No nested 'login' parent key found. Attempting root-level key extraction...");
            }

            // Extract text dynamically while checking for alternative casing
            if (targetObject.has("mobileNumber")) {
                rawMobileNum = targetObject.get("mobileNumber").getAsString();
            } else if (targetObject.has("mobilenumber")) {
                rawMobileNum = targetObject.get("mobilenumber").getAsString();
            } else {
                throw new NullPointerException("Could not locate key 'mobileNumber' or 'mobilenumber' inside JSON configuration.");
            }

            if (targetObject.has("Mpin")) {
                plainMpin = targetObject.get("Mpin").getAsString();
            } else if (targetObject.has("mpin")) {
                plainMpin = targetObject.get("mpin").getAsString();
            } else {
                throw new NullPointerException("Could not locate key 'Mpin' or 'mpin' inside JSON configuration.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to read or parse testdata/users.json data! Inspect file schema validity.", e);
        }

        // Quick data verification validation check
        if (rawMobileNum.isEmpty() || plainMpin.isEmpty()) {
            throw new RuntimeException("Data extracted from users.json is empty!");
        }

        System.out.println("[Data Extraction] Successfully processed credentials. Mobile: " + rawMobileNum);

        // Step 3: Run credential transaction flow using your plain variables
        loginFlow.loginWithCredentials(rawMobileNum, plainMpin);
    }
}