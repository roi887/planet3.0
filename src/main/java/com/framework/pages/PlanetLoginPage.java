package com.framework.pages;

import com.framework.base.BasePage;
import io.appium.java_client.android.AndroidDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.framework.locators.PlanetLoginLocators.*;

public class PlanetLoginPage extends BasePage {

    private static final Logger logger =
            LoggerFactory.getLogger(PlanetLoginPage.class);

    public PlanetLoginPage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isNotificationPopupDisplayed() {
        return isDisplayed(NOTIFICATION);
    }

    public boolean isLocationPopupDisplayed() {
        return isDisplayed(LOC_PERMISSION_ALLOW);
    }

    public void handleInitialPermissions() {
        logger.info("Accepting notification permission");
        click(NOTIFICATION);
    }

    public void clickPreMobileContinue() {
        logger.info("Clicking Pre-Mobile Continue");
        click(PRE_MOBILE_CONTINUE_BTN);
    }

    public void handleLocationPermissionDialogue() {
        logger.info("Accepting location permission");
        click(LOC_PERMISSION_ALLOW);
    }

    public void enterMobileNumber(String mobileNumber) {
        logger.info("Entering mobile number");
        type(MOBILE_NUMBER_FIELD, mobileNumber);
    }

    public void clickPostMobileContinue() {
        logger.info("Clicking Post-Mobile Continue");
        click(POST_MOBILE_CONTINUE_BTN);
    }

    public void enterMPIN(String mpin) {
        logger.info("Entering MPIN");
        type(MPIN_FIELD, mpin);
    }

    public void clickNoThanks() {

        if (isDisplayed(NO_THANKS)) {

            logger.info("Clicking No Thanks");
            click(NO_THANKS);
        }
    }

    public void clickFingerprint() {

        if (isDisplayed(FINGERPRINT)) {

            logger.info("Clicking Fingerprint");
            click(FINGERPRINT);
        }
    }

    public boolean isNoThanksDisplayed() {
        return false;
    }
}