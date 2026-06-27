package com.framework.pages;

import com.framework.base.BasePage;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.framework.locators.ResetPinLocators.*;

public class ResetPinPage extends BasePage {

    private static final Logger logger =
            LoggerFactory.getLogger(ResetPinPage.class);

    public ResetPinPage(AndroidDriver driver) {
        super(driver);
    }

    public void clickResetPin() {

        logger.info("Clicking Reset PIN");

        click(RESET_PIN);
    }

    public void enterOtp(String otp) {

        logger.info("Entering OTP");

        type(OTP_FIELD, otp);
    }

    public void clickVerify() {

        logger.info("Clicking Verify");

        click(VERIFY_BUTTON);
    }

    public void enterNewPin(String pin) {

        enterPin(NEW_PIN_FIELD, pin);
    }

    public void enterConfirmPin(String pin) {

        enterPin(CONFIRM_PIN_FIELD, pin);
    }

    private void enterPin(By locator, String pin) {

        logger.info("Entering PIN");

        type(locator, pin);
    }

    public void clickContinue() {

        logger.info("Clicking Continue");

        click(CONTINUE_BUTTON);
    }

    public void clickNoThanks() {

        if (isDisplayed(NO_THANKS)) {

            logger.info("Clicking No Thanks");

            click(NO_THANKS);
        }
    }

    public void clickClose() {

        if (isDisplayed(CLOSE_ICON)) {

            logger.info("Clicking Close Icon");

            click(CLOSE_ICON);
        }
    }
}