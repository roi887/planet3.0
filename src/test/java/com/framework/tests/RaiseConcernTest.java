package com.framework.tests;

import com.framework.factory.DriverFactory;
import com.framework.flows.RaiseConcernFlow;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.appium.java_client.android.AndroidDriver;
import org.testng.annotations.Listeners;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.io.InputStream;
import java.io.InputStreamReader;

@Listeners(com.framework.listeners.ExtentReportListener.class)
public class RaiseConcernTest {

    private AndroidDriver driver;
    private RaiseConcernFlow raiseConcernFlow;

    @BeforeMethod
    public void setUpConfiguration() {
        System.out.println("[Setup] Allocating environment session driver context...");
        this.driver = DriverFactory.initDriver(null);
        if (this.driver != null) {
            this.driver.activateApp("com.ltfs.planet.uat");
        }
    }

    @Test
    public void testEndToEndRaiseConcernWithNativeLogin() {
        System.out.println("====== Starting Integrated End-to-End Test Execution ======");

        // Fetch dynamic test values using your JSON structure
        String userMobile = getJsonValue("mobileNumber");
        String userMpin = getJsonValue("Mpin");

        String inputSubject = "Payment Tracking Issue";
        String inputDetails = "The funds were successfully debited from my banking institution, but status continues showing unadjusted.";

        // Fire the business process execution sequence
        raiseConcernFlow = new RaiseConcernFlow(driver);
        raiseConcernFlow.executeLoginAndRaiseConcernWorkflow(userMobile, userMpin, inputSubject, inputDetails);

        System.out.println("====== Integrated End-to-End Test Execution Finished ======");
    }

    /**
     * Reads values from target resource folder 'loginData.json' file using clean classloader paths.
     */
    private String getJsonValue(String key) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("loginData.json")) {
            if (is == null) {
                System.out.println("[Data Error] loginData.json resource file not found on current execution path root!");
                return "";
            }
            InputStreamReader reader = new InputStreamReader(is);
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            return jsonObject.getAsJsonObject("login").get(key).getAsString();
        } catch (Exception e) {
            System.out.println("[Data Warning] JSON parsing error on target parameter: " + e.getMessage());
            return "";
        }
    }
}