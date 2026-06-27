package tests;

import com.framework.base.BaseTest;
import com.framework.factory.DriverFactory;
import com.framework.flows.AddressUpdateFlow;
import com.framework.locators.AddresUpdateLoc;
import com.framework.utils.JsonDataReader;
import com.framework.listeners.ExtentReportListener;
import io.appium.java_client.android.AndroidDriver;
import org.testng.annotations.Listeners;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

/**
 * PRODUCTION REGRESSION SUITE
 * This runs the clean, end-to-end automation pipeline from scratch.
 * It inherits the global thread-safe driver management context from BaseTest.
 */
@Listeners(ExtentReportListener.class)
public class AddressUpdateTest extends BaseTest {

    private AndroidDriver driver;
    private AddressUpdateFlow addressUpdateFlow;

    @BeforeMethod
    public void setUpTestConfiguration() {
        System.out.println("[Setup] Initializing active driver session configuration context...");
        // Passing null forces the factory to safely fall back to your config file properties!
        this.driver = DriverFactory.initDriver();

        if (this.driver != null) {
            this.driver.activateApp("com.ltfs.planet.uat");
        }
    }
    @Test
    public void testEndToEndAddressModificationPipeline() {
        System.out.println("====== [PRODUCTION RUN] Starting Address Modification Test ======");

        // 1. Extract credentials and dynamic field data from your centralized JSON tracking framework
        String testDataFilePath = "testdata/users.json";

        String mobileNumber = JsonDataReader.getTestData(testDataFilePath, "login.mobileNumber");
        String mpin         = JsonDataReader.getTestData(testDataFilePath, "login.Mpin");

        String addressLine1 = JsonDataReader.getTestData(testDataFilePath, "address.ADDL_1");
        String addressLine2 = JsonDataReader.getTestData(testDataFilePath, "address.ADDL_2");
        String zipCodeData  = JsonDataReader.getTestData(testDataFilePath, "address.PIN");

        // Your Windows local target document image filepath
        String uploadImageFilePath = "C:\\Users\\ven06482\\Downloads\\1000000037.png";

        // 2. Instantiate your Business Layer Flow using the active driver
        addressUpdateFlow = new AddressUpdateFlow(driver);

        // 3. Execute the full end-to-end automated business flow sequence pipeline.
        addressUpdateFlow.loginAndExecuteAddressUpdate(
                mobileNumber,
                mpin,
                addressLine1,
                addressLine2,
                zipCodeData,
                AddresUpdateLoc.SEL_PROOF,
                uploadImageFilePath
        );

        System.out.println("====== [PRODUCTION RUN] Test Sequence Finalized Cleanly ======");
    }

    /**
     * Specific test lifecycle teardown cleanup orchestration hook.
     */
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            System.out.println("[Clean] Terminating Appium production automation target lifecycle...");
            driver.quit();
            System.out.println("[Cleanup] Session terminated safely.");
        }
    }
}