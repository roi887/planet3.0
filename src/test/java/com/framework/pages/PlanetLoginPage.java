package com.framework.pages;

import com.framework.locators.PlanetLoginLocators;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class PlanetLoginPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // Constructor
    public PlanetLoginPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Handles Step 2: Native Android notification alerts
     */
    public void handleInitialPermissions() {
        System.out.println("[Page] Waiting for notification permission popup...");
        WebElement allowBtn = wait.until(ExpectedConditions.elementToBeClickable(PlanetLoginLocators.NOTIFICATION));
        allowBtn.click();
    }

    /**
     * Handles Step 3: Click Continue BEFORE entering the mobile number
     */
    public void clickPreMobileContinue() {
        System.out.println("[Page] Clicking 'Continue' button before mobile number entry...");
        WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(PlanetLoginLocators.PRE_MOBILE_CONTINUE_BTN));
        continueBtn.click();
    }

    /**
     * Handles Step 4: Native Android location alerts
     */
    public void handleLocationPermissionDialogue() {
        System.out.println("[Page] Waiting for location privacy permission popup...");
        WebElement locationBtn = wait.until(ExpectedConditions.elementToBeClickable(PlanetLoginLocators.LOC_PERMISSION_ALLOW));
        locationBtn.click();
    }

    /**
     * Handles Step 5: Focused text injection for phone inputs
     */
    public void enterMobileNumber(String mobileNumber) {
        System.out.println("[Page] Focusing and entering mobile number: " + mobileNumber);
        WebElement inputField = wait.until(ExpectedConditions.elementToBeClickable(PlanetLoginLocators.MOBILE_NUMBER_FIELD));
        inputField.click(); // Focuses keyboard to prevent instrumentation driver crashes
        inputField.clear();
        inputField.sendKeys(mobileNumber);
    }

    /**
     * Handles Step 6: Click Continue AFTER entering the mobile number
     */
    public void clickPostMobileContinue() {
        System.out.println("[Page] Clicking 'Continue' button after mobile number entry...");
        WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(PlanetLoginLocators.POST_MOBILE_CONTINUE_BTN));
        continueBtn.click();
    }

    /**
     * Authentication methods for standard credential validations
     */
    public void enterMPIN(String mpin) {
        System.out.println("[Page] Entering MPIN security keys...");
        WebElement pinField = wait.until(ExpectedConditions.elementToBeClickable(PlanetLoginLocators.MPIN_FIELD));
        pinField.click(); // Focus field
        pinField.sendKeys(mpin);
    }

    public void clickNoThanks() {
        System.out.println("[Page] Dismissing contextual onboarding prompt overlays...");
        WebElement dismissBtn = wait.until(ExpectedConditions.elementToBeClickable(PlanetLoginLocators.NO_THANKS));
        dismissBtn.click();
    }

    public void clickfingerprint() {
        System.out.println("[Page] Finalizing device biometric setup check toggles...");
        WebElement bioBtn = wait.until(ExpectedConditions.elementToBeClickable(PlanetLoginLocators.FINGERPRINT));
        bioBtn.click();
    }
}