package com.framework.locators;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class AddresUpdateLoc {

    // View Loan Details button from home screen
    public static final By SERVICES = AppiumBy.accessibilityId("dashboard_services_tab");

    // Applicant Details sub-tab
    public static final By ADD_UPDATE = AppiumBy.accessibilityId("services_address_details_card");

    // Address Update Pencil icon launcher (Button Instance 9)
    public static final By ADD_PENCIL = AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.Button\").instance(9)");

    // Address Line 1 input field (EditText Instance 0)
    public static final By ADDL_1 = AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").instance(0)");

    // Address Line 2 input field (EditText Instance 1)
    public static final By ADDL_2 = AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").instance(1)");

    // Area PIN Code input field (EditText Instance 2)
    public static final By PIN = AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").instance(2)");

    // Address Proof Dropdown Selection Component
    public static final By ADD_PROOF = AppiumBy.androidUIAutomator("new UiSelector().description(\"Select Address Proof\")");

    // Dynamic Address Proof Choice Item (Used in dropdown verification selection)
    public static final By SEL_PROOF = AppiumBy.accessibilityId("Aadhaar Card");

    // Upload Document Action Button (Button Instance 2)
    public static final By UPL_DOC = AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.Button\").instance(2)");

    // First Continue Button
    public static final By CON = AppiumBy.accessibilityId("Continue");

    // Second Continue Button (Subsequent page validation submitter)
    public static final By CON2 = AppiumBy.accessibilityId("Continue");
}