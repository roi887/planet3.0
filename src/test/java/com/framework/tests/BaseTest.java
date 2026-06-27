package com.framework.tests;

import com.framework.constants.FrameworkConstants;
import com.framework.factory.DriverFactory;
import com.framework.flows.LoginFlow;
import io.appium.java_client.android.AndroidDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.util.Properties;

/**
 * BaseTest serves as the core orchestration layer for all test executions.
 * It manages the thread-safe driver lifecycle and initializes business flows.
 */
public class BaseTest {

    protected DriverFactory df;
    protected AndroidDriver driver;
    protected Properties prop;
    protected LoginFlow loginFlow;

    /**
     * Setup method executed before every individual test case.
     * It accepts optional TestNG parameters to support parallel execution across multiple devices.
     * @param deviceName Target emulator or physical device name passed from testng.xml
     * @param udid       Unique device identifier passed from testng.xml
     */
    @BeforeMethod
    @Parameters({"deviceName", "udid"})
    public void setUp(@Optional String deviceName, @Optional String udid) {
        System.out.println("====== Initializing Test Configuration Setup ======");

        // 1. Initialize the DriverFactory and load global properties
        df = new DriverFactory();
        df.initProperties();

        // FIX: Extract and clone the loaded properties object so 'prop' isn't null!
        // We look up the hidden configuration matrix inside your config.properties file.
        try {
            java.lang.reflect.Field field = DriverFactory.class.getDeclaredField("configProperties");
            field.setAccessible(true);
            prop = (Properties) field.get(null);
        } catch (Exception e) {
            prop = new Properties(); // absolute fallback safety
        }

        // 2. Overwrite configuration properties if TestNG passes device parameters dynamically via XML
        if (deviceName != null && !deviceName.isEmpty()) {
            prop.setProperty("deviceName", deviceName);
            System.out.println("Thread Target Device Name: " + deviceName);
        }
        if (udid != null && !udid.isEmpty()) {
            prop.setProperty("udid", udid);
            System.out.println("Thread Target Device UDID: " + udid);
        }

        // 3. Instantiate the thread-safe Appium Driver session with the valid parameters
        driver = DriverFactory.initDriver(prop);
        System.out.println("Appium Driver initialized successfully for Thread: " + Thread.currentThread().getId());

        // 4. Initialize your reusable business flows with the active driver session
        loginFlow = new LoginFlow(driver);
        System.out.println("Business Flows successfully attached to driver session.");
    }

    /**
     * Teardown method executed immediately following the completion of every test case.
     * Kept commented out for your Sandbox Debugging workspace to keep the active screen alive.
     */
//    @AfterMethod
//    public void tearDown() {
//        System.out.println("====== Skipping Test Session Teardown for Sandbox Debug Tracking ======");
//        // Keep active connections open on screen while writing/fixing automated steps.
//    }
}