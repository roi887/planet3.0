package com.framework.tests;

import com.framework.factory.DriverFactory;
import com.framework.pages.AddressUpdatePage;
import io.appium.java_client.android.AndroidDriver;
import org.testng.annotations.Listeners;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * FAST DEBUGGING SANDBOX
 * Use this class to test individual element fixes or form inputs instantly
 * without executing the full login, onboarding, or navigation sequence.
 */
@Listeners(com.framework.listeners.ExtentReportListener.class)
public class AddressUpdateDebugTest {

    private AndroidDriver driver;
    private AddressUpdatePage addressPage;

    @BeforeMethod
    public void setupDebugSession() {
        System.out.println("====== [DEBUG WORKSPACE] Connecting to active driver session ======");

        // 1. Force the DriverFactory to read your config.properties file first
        DriverFactory factoryInstance = new DriverFactory();
        factoryInstance.initProperties();

        // 2. Try to grab an active driver instance
        this.driver = DriverFactory.getDriver();

        if (this.driver == null) {
            System.out.println("[Debug Workspace] No active driver found in memory. Initializing fresh session context with properties loaded...");
            // FIX: Pass null, but now DriverFactory internal configProperties is fully loaded into memory!
            this.driver = DriverFactory.initDriver(null);
        }

        // 3. Instantiate your target page class using the attached session reference
        this.addressPage = new AddressUpdatePage(driver);
    }

    /**
     * TARGET STEP VERIFICATION PIPELINE
     * * HOW TO RUN THIS WORKSPACE LOOP:
     * 1. Pick up your physical device or emulator.
     * 2. Manually interact with the app until you are looking right at the Address Entry Form screen.
     * 3. Leave the phone screen awake on your desk.
     * 4. Look inside IntelliJ: Click the small green triangle/play icon right next to line 44 ("public void debugTargetStep").
     * 5. Click "Run 'debugTargetStep()'".
     */
    @Test
    public void debugTargetStep() {
        System.out.println("====== [DEBUG RUN EXECUTION] Injecting automation steps... ======");

        // Step 1: Tests the upgraded typing/sync buffer logic
        addressPage.enterPinCode("562106");

        // Step 2: Tests the dropdown exposure click now that the keyboard drops out of the way!
        addressPage.clickAddressProofDropdown();

        System.out.println("====== [DEBUG RUN END] Actions complete! Adjust code or properties and rerun if needed. ======");
    }
}