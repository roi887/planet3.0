package com.framework.flows;

import com.framework.pages.ResetPinPage;
import com.framework.utils.OtpFetcher;
import io.appium.java_client.android.AndroidDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResetPinFlow {

    private static final Logger logger =
            LoggerFactory.getLogger(ResetPinFlow.class);

    private final LoginFlow loginFlow;
    private final ResetPinPage resetPinPage;

    public ResetPinFlow(AndroidDriver driver) {

        this.loginFlow = new LoginFlow(driver);
        this.resetPinPage = new ResetPinPage(driver);
    }

    public void executeResetPinWorkflow(
            String mobileNumber,
            String newPin,
            String confirmPin) {

        logger.info("Starting Reset PIN workflow");

        loginFlow.onboardAndNavigateToLogin();

        loginFlow.getLoginPage()
                .enterMobileNumber(mobileNumber);

        loginFlow.getLoginPage()
                .clickPostMobileContinue();

        resetPinPage.clickResetPin();

        logger.info("Fetching OTP from server");

        String otp = OtpFetcher.getLatestOtp(mobileNumber);

        if (otp == null || otp.isBlank()) {

            throw new RuntimeException(
                    "OTP not received from server");
        }

        logger.info("OTP fetched successfully");

        resetPinPage.enterOtp(otp);

        resetPinPage.clickVerify();

        resetPinPage.enterNewPin(newPin);

        resetPinPage.enterConfirmPin(confirmPin);

        resetPinPage.clickContinue();

        resetPinPage.clickNoThanks();

        resetPinPage.clickClose();

        logger.info("Reset PIN workflow completed successfully");
    }
}