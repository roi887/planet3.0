package com.framework.flows;

import com.framework.pages.AddressUpdatePage;
import com.framework.locators.AddresUpdateLoc; // Imported your locators class
import com.framework.utils.GestureUtils;     // Imported your new GestureUtils class
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class AddressUpdateFlow {

    private final AndroidDriver driver;
    private final LoginFlow loginFlow;
    private final AddressUpdatePage addressUpdatePage;

    public AddressUpdateFlow(AndroidDriver driver) {
        this.driver = driver;
        this.loginFlow = new LoginFlow(driver);
        this.addressUpdatePage = new AddressUpdatePage(driver);
    }

    /**
     * Complete sequential flow to login and successfully submit an address update
     */
    public void loginAndExecuteAddressUpdate(
            String mobileNumber,
            String mpin,
            String address1,
            String address2,
            String pinCode,
            By targetProofLocator,
            String localWindowsFilePath
    ) {
        // Step 1: Handle login prerequisites
        System.out.println("[Business Flow] Starting application login sequence...");
        loginFlow.onboardAndNavigateToLogin();
        loginFlow.loginWithCredentials(mobileNumber, mpin);

        // Step 2: Navigate through dashboard to Address details
        System.out.println("[Business Flow] Navigating to Address Update section...");
        addressUpdatePage.clickViewDetails();
        addressUpdatePage.clickApplicantDetails();
        addressUpdatePage.clickAddressPencil();
        addressUpdatePage.clickFirstContinue();

        // Step 3: Populate Address Form
        System.out.println("[Business Flow] Filling out new address profile data...");
        addressUpdatePage.enterAddressLine1(address1);
        addressUpdatePage.enterAddressLine2(address2);
        addressUpdatePage.enterPinCode(pinCode);

        // --- KEYBOARD DISMISSAL HOOK ---
        System.out.println("[Business Flow] Suppressing keyboard after pin code entry...");
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

        // --- GESTURE UTILS SCROLL HOOK ---
        System.out.println("[Business Flow] Delegating layout view scroll-down processing to GestureUtils...");
        GestureUtils.scrollUntilElementVisible(driver, AddresUpdateLoc.ADD_PROOF, 4);

        // Step 4: Handle document proof attachments
        System.out.println("[Business Flow] Attaching structural system document proof files...");
        addressUpdatePage.clickAddressProofDropdown();
        addressUpdatePage.selectProofFromDropdown(targetProofLocator);
        addressUpdatePage.uploadLocalDocument(localWindowsFilePath);

        // Step 5: Final Submission
        System.out.println("[Business Flow] Triggering final confirmation submission sequence...");
        addressUpdatePage.clickSecondContinue();
        System.out.println("[Business Flow] Address Update Flow Completed Successfully!");
    }
}