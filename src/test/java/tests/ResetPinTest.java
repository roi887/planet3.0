package tests;

import com.framework.factory.DriverFactory;
import com.framework.flows.ResetPinFlow;
import com.framework.utils.JsonDataReader;
import com.framework.listeners.ExtentReportListener;
import io.appium.java_client.android.AndroidDriver;
import org.testng.annotations.Listeners;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Listeners(ExtentReportListener.class)
public class ResetPinTest {
    private AndroidDriver driver;
    private ResetPinFlow resetPinFlow;

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.initDriver();

        // 2. Initialize the flow runner with our fresh driver session instance
        resetPinFlow = new ResetPinFlow(driver);
    }

    @Test
    public void testEndToEndResetPinWithJsonData() {
        // Gathering structural variables dynamically from testdata/users.json
        String targetPhone = JsonDataReader.getTestData("testdata/users.json", "login.mobileNumber");
        String securePinData = JsonDataReader.getTestData("testdata/users.json", "login.SECPIN");
        String confirmPinData = JsonDataReader.getTestData("testdata/users.json", "login.CONPIN");

        // Triggering the automated workflow execution pipeline
        resetPinFlow.executeResetPinWorkflow(targetPhone, securePinData, confirmPinData);
    }

//    @AfterMethod
//    public void tearDown() {
//        if (driver != null) {
//            driver.quit();
//            System.out.println("[Cleanup] Appium automation hardware session closed cleanly.");
//        }
//    }
}