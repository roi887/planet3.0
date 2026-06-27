package com.framework.flows;

import com.framework.pages.PlanetLoginPage;
import io.appium.java_client.android.AndroidDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginFlow {

    private static final Logger logger =
            LoggerFactory.getLogger(LoginFlow.class);

    private final PlanetLoginPage loginPage;

    public LoginFlow(AndroidDriver driver) {
        this.loginPage = new PlanetLoginPage(driver);
    }

    public void onboardAndNavigateToLogin() {

        logger.info("Starting onboarding flow");

        if (loginPage.isNotificationPopupDisplayed()) {
            loginPage.handleInitialPermissions();
        }

        loginPage.clickPreMobileContinue();

        if (loginPage.isLocationPopupDisplayed()) {
            loginPage.handleLocationPermissionDialogue();
        }

        logger.info("Onboarding flow completed");
    }

    public void enterMobileNumber(String mobileNumber) {
        loginPage.enterMobileNumber(mobileNumber);
    }

    public void clickPostMobileContinue() {
        loginPage.clickPostMobileContinue();
    }

    public void loginWithCredentials(
            String mobileNumber,
            String mpin) {

        logger.info("Starting login flow");

        enterMobileNumber(mobileNumber);

        clickPostMobileContinue();

        loginPage.hideKeyboard();

        loginPage.enterMPIN(mpin);

        if (loginPage.isNoThanksDisplayed()) {
            loginPage.clickNoThanks();
        }

        logger.info("Login flow completed");
    }

    public void loginToPlanet(
            String mobileNumber,
            String mpin) {

        onboardAndNavigateToLogin();

        loginWithCredentials(
                mobileNumber,
                mpin);
    }

    public LoginFlow getLoginPage() {
        return null;
    }
}