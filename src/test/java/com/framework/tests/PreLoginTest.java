package com.framework.tests;

import com.framework.flows.PreLoginFlow;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(com.framework.listeners.ExtentReportListener.class)
public class PreLoginTest extends BaseTest {

    @Test
    public void testOnboardingAndLocationPermissionExecution() {
        // Initialize the flow sequence with our running automated driver instance
        PreLoginFlow preLoginFlow = new PreLoginFlow(driver);

        // Triggers your precise step-by-step pre-login flow up to the location acceptance
        preLoginFlow.executePreLoginSequence();
    }
}