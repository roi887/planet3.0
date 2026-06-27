package com.framework.pages;

import com.framework.locators.AddresUpdateLoc;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.time.Duration;

public class AddressUpdatePage {
    private final AndroidDriver driver;
    private final WebDriverWait wait;

    public AddressUpdatePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void clickViewDetails() {
        System.out.println("[Page] Clicking services button...");
        wait.until(ExpectedConditions.elementToBeClickable(AddresUpdateLoc.SERVICES)).click();
    }

    public void clickApplicantDetails() {
        System.out.println("[Page] Tapping add update tab directly...");
        wait.until(ExpectedConditions.elementToBeClickable(AddresUpdateLoc.ADD_UPDATE)).click();
    }

    public void clickAddressPencil() {
        System.out.println("[Page] Launching address edit sequence via Pencil icon...");
        wait.until(ExpectedConditions.elementToBeClickable(AddresUpdateLoc.ADD_PENCIL)).click();
    }

    public void clickFirstContinue() {
        System.out.println("[Page] Clicking first Continue button...");
        wait.until(ExpectedConditions.elementToBeClickable(AddresUpdateLoc.CON)).click();
    }

    public void enterAddressLine1(String address1) {
        System.out.println("[Page] Entering Address Line 1: " + address1);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(AddresUpdateLoc.ADDL_1));
        element.click();
        element.clear();
        element.sendKeys(address1);
    }

    public void enterAddressLine2(String address2) {
        System.out.println("[Page] Entering Address Line 2: " + address2);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(AddresUpdateLoc.ADDL_2));
        element.click();
        element.clear();
        element.sendKeys(address2);
    }

    public void enterPinCode(String pinCode) {
        System.out.println("[Page] Entering Area Pin Code: " + pinCode);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(AddresUpdateLoc.PIN));
        element.click();
        element.clear();
        element.sendKeys(pinCode);

        // Pause briefly to let the application framework sync the entered string values before flow intercept
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void clickAddressProofDropdown() {
        System.out.println("[Page] Expanding Address Proof selector dropdown...");
        wait.until(ExpectedConditions.elementToBeClickable(AddresUpdateLoc.ADD_PROOF)).click();
    }

    public void selectProofFromDropdown(By targetProofLocator) {
        System.out.println("[Page] Selecting specified document type option...");
        wait.until(ExpectedConditions.elementToBeClickable(targetProofLocator)).click();
    }

    public void uploadLocalDocument(String localWindowsFilePath) {
        System.out.println("[Upload Engine] Resolving local workstation file...");
        File file = new File(localWindowsFilePath);
        if (!file.exists()) {
            throw new RuntimeException("CRITICAL ERROR: Local file path missing at target location: " + file.getAbsolutePath());
        }

        String remoteDeviceDestinationPath = "/sdcard/Download/" + file.getName();

        System.out.println("[Upload Engine] Phase 1: Silently pushing binary stream payload data to Android local download system storage...");
        try {
            byte[] fileContent = java.nio.file.Files.readAllBytes(file.toPath());
            byte[] base64EncodedData = java.util.Base64.getEncoder().encode(fileContent);
            driver.pushFile(remoteDeviceDestinationPath, base64EncodedData);
            System.out.println("[Upload Engine] Binary payload cleanly cached inside device path: " + remoteDeviceDestinationPath);
        } catch (java.io.IOException e) {
            throw new RuntimeException("CRITICAL IO ERROR: Stream aborted down the Appium server connection tunnel: " + e.getMessage(), e);
        }

        System.out.println("[Upload Engine] Phase 2: Tapping app upload button to activate native Android file picking overlay panel...");
        wait.until(ExpectedConditions.elementToBeClickable(AddresUpdateLoc.UPL_DOC)).click();

        System.out.println("[Upload Engine] Phase 3: Interacting with native OS file tray to tap element matching name: " + file.getName());
        try {
            By nativeSystemFileItem = AppiumBy.androidUIAutomator("new UiSelector().text(\"" + file.getName() + "\")");
            wait.until(ExpectedConditions.elementToBeClickable(nativeSystemFileItem)).click();
            System.out.println("[Upload Engine] File chosen! Attached cleanly back to the application execution form layout.");
        } catch (Exception e) {
            throw new RuntimeException("SYSTEM PICKER CRASH: Android failed to expose or locate file visual component text: " + file.getName() + ". Error: " + e.getMessage(), e);
        }
    }

    public void clickSecondContinue() {
        System.out.println("[Page] Finalizing submission sequence via second Continue interaction...");
        wait.until(ExpectedConditions.elementToBeClickable(AddresUpdateLoc.CON2)).click();
    }
}