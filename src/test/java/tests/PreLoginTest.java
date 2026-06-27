package tests;

import com.framework.base.BaseTest;
import com.framework.flows.PreLoginFlow;
import com.framework.listeners.ExtentReportListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(ExtentReportListener.class)
public class PreLoginTest extends BaseTest {

    @Test
    public void testOnboardingAndLocationPermissionExecution() {
        // Initialize the flow sequence with our running automated driver instance
        PreLoginFlow preLoginFlow = new PreLoginFlow(driver);

        // Triggers your precise step-by-step pre-login flow up to the location acceptance
        preLoginFlow.executePreLoginSequence();
    }
}