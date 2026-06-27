package com.framework.flows;

import com.framework.pages.PlanetLoginPage;
import com.framework.factory.DriverFactory; // Added to access the active session cleanly
import io.appium.java_client.android.AndroidDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginFlow {
    private static final Logger log = LoggerFactory.getLogger(LoginFlow.class);
    public final PlanetLoginPage loginPage;
    private final AndroidDriver driver; // Saved reference locally to control device hardware elements

    public LoginFlow(AndroidDriver driver) {
        this.driver = driver;
        this.loginPage = new PlanetLoginPage(driver);
    }

    /**
     * Complete sequential flow for handling launch permissions and initial onboarding screens
     */
    public void onboardAndNavigateToLogin() {
        System.out.println("Executing Onboarding Flow...");

        // SAFEGUARD 1: Wrap native system notification alert
        try {
            loginPage.handleInitialPermissions();
        } catch (Exception e) {
            System.out.println("[Safe Notice] Notification permission popup was not found or auto-closed by device OS. Continuing flow...");
        }

        // loginPage.toggleLocationCheckbox();
        // loginPage.acceptTermsAndConditions();

        // FIX: Calls the step 3 continue button before the mobile number screen
        loginPage.clickPreMobileContinue();

        // SAFEGUARD 2: Wrap native location permission alert
        try {
            loginPage.handleLocationPermissionDialogue();
        } catch (Exception e) {
            System.out.println("[Safe Notice] Location permission dialogue was not found or auto-closed by device OS. Continuing flow...");
        }
    }

    /**
     * Complete sequential flow for authentication
     */
    public void loginWithCredentials(String mobileNumber, String Mpin) {
        System.out.println("Executing Credential Authentication Flow...");
        loginPage.enterMobileNumber(mobileNumber);

        // FIX: Calls the step 6 continue button after entering the mobile number
        loginPage.clickPostMobileContinue();

        // FIX: Explicitly check and drop the soft keyboard so it doesn't mask the MPIN elements underneath
        try {
            if (driver.isKeyboardShown()) {
                System.out.println("[Device Control] Soft keyboard detected. Hiding keyboard to clear layout view...");
                driver.hideKeyboard();
            }
        } catch (Exception e) {
            System.out.println("[Warning] Failed to check or hide keyboard safely: " + e.getMessage());
        }

        loginPage.enterMPIN(Mpin);
        loginPage.clickNoThanks();
        loginPage.clickfingerprint();
    }
}