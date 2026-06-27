package com.framework.pages;

import com.framework.locators.ResetPinLocators;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ResetPinPage {
    private final WebDriverWait wait;

    public ResetPinPage(AndroidDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void clickResetPin() {
        System.out.println("[Page] Clicking 'Reset PIN' button launcher...");
        wait.until(ExpectedConditions.elementToBeClickable(ResetPinLocators.RESETPIN)).click();
    }

    public void enterOTP(String otp) {
        System.out.println("[Page] Cleaning field and entering SSH fetched OTP: " + otp);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(ResetPinLocators.ENTEROTP));
        element.click();
        element.clear();
        element.sendKeys(otp);
    }

    public void clickVerify() {
        System.out.println("[Page] Submitting verification OTP...");
        wait.until(ExpectedConditions.elementToBeClickable(ResetPinLocators.VERIFY)).click();
    }

    public void enterNewPin(String pin) {
        System.out.println("[Page] Securing inputting SECPIN values...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(ResetPinLocators.SECPIN));
        element.click();
        element.clear();
        element.sendKeys(pin);
    }

    public void enterConfirmPin(String pin) {
        System.out.println("[Page] Securing inputting CONPIN values...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(ResetPinLocators.CONPIN));
        element.click();
        element.clear();
        element.sendKeys(pin);
    }

    public void clickContinue() {
        System.out.println("[Page] Executing final verification submission...");
        wait.until(ExpectedConditions.elementToBeClickable(ResetPinLocators.CON)).click();


    }

    public void clickNO(){
        wait.until(ExpectedConditions.elementToBeClickable(ResetPinLocators.NO_THANKS)).click();
        System.out.println("clicking on no for location");

    }

    public void clickcancel(){
        wait.until(ExpectedConditions.elementToBeClickable(ResetPinLocators.CLOSE_X_ICON)).click();
        System.out.println("clicking on cancel");
    }
}