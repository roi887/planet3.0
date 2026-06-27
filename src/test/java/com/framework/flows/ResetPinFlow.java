package com.framework.flows;

import com.framework.flows.LoginFlow;
import com.framework.pages.ResetPinPage;
import com.framework.utils.OtpFetcher;
import io.appium.java_client.android.AndroidDriver;

public class ResetPinFlow {

    private final LoginFlow loginFlow;
    private final ResetPinPage resetPinPage;

    public ResetPinFlow(AndroidDriver driver) {
        this.loginFlow = new LoginFlow(driver);
        this.resetPinPage = new ResetPinPage(driver);
    }

    public void executeResetPinWorkflow(String phoneNumber, String newPin, String confirmPin) {
        System.out.println("=== Starting Reset PIN Business Sequence ===");

        // Steps 1-4: Reusing existing onboarding alerts & splash automation
        loginFlow.onboardAndNavigateToLogin();

        // Step 5: Enter Mobile Number (Reusing your main login flow page component)
        loginFlow.loginPage.enterMobileNumber(phoneNumber);

        // Step 6: Click Continue - FIXED to use your post-mobile button action
        loginFlow.loginPage.clickPostMobileContinue();

        // Step 7: Transition to Reset Pin sub-system
        resetPinPage.clickResetPin();

        // Step 8: Tail SSH log for OTP code
        try {
            System.out.println("[SSH Log Engine] Waiting 4 seconds for remote log flush...");
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("[SSH Log Engine] Querying server via port 22 for phone: " + phoneNumber);
        String liveOtp = OtpFetcher.getLatestOtp(phoneNumber);
        resetPinPage.enterOTP(liveOtp);

        // Step 9: Verify OTP
        resetPinPage.clickVerify();

        // Step 10: Enter SECPIN
        resetPinPage.enterNewPin(newPin);

        // Step 11: Enter CONPIN
        resetPinPage.enterConfirmPin(confirmPin);

        // Step 12: Save and Submit Changes
        resetPinPage.clickContinue();

        resetPinPage.clickNO();
        resetPinPage.clickcancel();

        System.out.println("=== Reset PIN Transaction Pipeline Completed Successfully ===");
    }
}