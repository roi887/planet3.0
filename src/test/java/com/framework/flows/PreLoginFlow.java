package com.framework.flows;

import com.framework.pages.PlanetLoginPage;
import com.framework.pages.PreLoginFeaturePage;
import io.appium.java_client.android.AndroidDriver;

public class PreLoginFlow {
    private final PlanetLoginPage loginPage;
    private final PreLoginFeaturePage preLoginFeaturePage;

    public PreLoginFlow(AndroidDriver driver) {
        this.loginPage = new PlanetLoginPage(driver);
        this.preLoginFeaturePage = new PreLoginFeaturePage(driver);
    }

    /**
     * Complete sequential execution flow for the Pre-Login onboarding lifecycle steps.
     */
    public void executePreLoginSequence() {
        System.out.println("=== Starting PreLogin Sequence Flow ===");

        // Step 1: Launch the app -> Click allow notification (Called from loginPage)
        loginPage.handleInitialPermissions();

        // Step 2: Click location check box twice (Called from feature page)
        preLoginFeaturePage.clickLocationCheckboxTwice();

        // Step 3: Click terms and conditions (Called from feature page)
        preLoginFeaturePage.clickTermsAndConditions();

        // Step 4: Scroll till bottom of the page (Called from feature page)
        preLoginFeaturePage.scrollTermsToBottom();

        // Step 5: Click device back (Called from feature page)
        preLoginFeaturePage.clickDeviceBack();

        // Step 6: Click continue (Pre-Mobile Number screen - Called from loginPage)
        loginPage.clickPreMobileContinue();

        // Step 7: Select allow this time for native OS location permission (Called from loginPage)
        loginPage.handleLocationPermissionDialogue();

        System.out.println("=== PreLogin Sequence Completed Successfully ===");
    }
}