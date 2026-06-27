package com.framework.flows;

import com.framework.pages.RaiseConcernPage;
import io.appium.java_client.android.AndroidDriver;

public class RaiseConcernFlow {

    private final AndroidDriver driver;
    private final LoginFlow loginFlow; // Reusing your standard LoginFlow
    private final RaiseConcernPage raiseConcernPage;

    public RaiseConcernFlow(AndroidDriver driver) {
        this.driver = driver;
        this.loginFlow = new LoginFlow(driver);
        this.raiseConcernPage = new RaiseConcernPage(driver);
    }

    /**
     * Complete sequential flow to login and successfully raise a concern
     */
    public void executeLoginAndRaiseConcernWorkflow(
            String mobileNumber,
            String mpin,
            String subject,
            String details
    ) {
        // Step 1: Handle login prerequisites using your exact login flow pattern
        System.out.println("[Business Flow] Starting application login sequence...");
        loginFlow.onboardAndNavigateToLogin();
        loginFlow.loginWithCredentials(mobileNumber, mpin);

        // Step 2: Navigate through dashboard to Raise Concern details
        System.out.println("[Business Flow] Navigating to Raise Concern section...");
        raiseConcernPage.clickRaiseServiceRequestCard();
        raiseConcernPage.openCategoryDropdown();
        raiseConcernPage.selectPaymentUpdatedOption();

        // Step 3: Populate Form Fields
        System.out.println("[Business Flow] Filling out concern profile data...");
        raiseConcernPage.enterSubject(subject);
        raiseConcernPage.enterDescription(details);

        // --- KEYBOARD DISMISSAL HOOK (Matching AddressUpdateFlow strategy) ---
        System.out.println("[Business Flow] Suppressing keyboard after details entry...");
        try {
            if (driver.isKeyboardShown()) {
                driver.hideKeyboard();
                System.out.println("[Business Flow] Soft keyboard dismissed successfully via driver API.");
            }
        } catch (Exception e) {
            // Safe fall-back execution loop for hardware devices (Xiaomi, etc.)
            driver.navigate().back();
            System.out.println("[Business Flow] Soft keyboard collapsed via native Back click event fallback.");
        }

        // Step 4: Click intermediate action button before scroll
        raiseConcernPage.clickFormActionButtonThree();

        // --- GESTURE UTILS SCROLL HOOK (Matching AddressUpdateFlow strategy) ---
        System.out.println("[Business Flow] Delegating layout view scroll-down processing to screen bounds...");
        scrollDownToBottom();

        // Step 5: Final Submission Sequence
        System.out.println("[Business Flow] Triggering final confirmation submission sequence...");
        raiseConcernPage.clickFormActionButtonTwo();
        raiseConcernPage.clickSendButton();

        System.out.println("[Business Flow] Raise Concern Flow Completed Successfully!");
    }

    /**
     * Programmatic drag touch swipe sequence to pull up hidden fields
     */
    private void scrollDownToBottom() {
        int width = driver.manage().window().getSize().getWidth();
        int height = driver.manage().window().getSize().getHeight();

        int startX = width / 2;
        int startY = (int) (height * 0.75);
        int endY = (int) (height * 0.20);

        org.openqa.selenium.interactions.PointerInput finger =
                new org.openqa.selenium.interactions.PointerInput(org.openqa.selenium.interactions.PointerInput.Kind.TOUCH, "finger");
        org.openqa.selenium.interactions.Sequence scrollSequence = new org.openqa.selenium.interactions.Sequence(finger, 1);

        scrollSequence.addAction(finger.createPointerMove(java.time.Duration.ZERO, org.openqa.selenium.interactions.PointerInput.Origin.viewport(), startX, startY));
        scrollSequence.addAction(finger.createPointerDown(org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
        scrollSequence.addAction(finger.createPointerMove(java.time.Duration.ofMillis(600), org.openqa.selenium.interactions.PointerInput.Origin.viewport(), startX, endY));
        scrollSequence.addAction(finger.createPointerUp(org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(java.util.Collections.singletonList(scrollSequence));
    }
}