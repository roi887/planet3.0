package com.framework.locators;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class PlanetLoginLocators {
    // System notification permission (Allow)
    public static final By NOTIFICATION = AppiumBy.id("com.android.permissioncontroller:id/permission_allow_button");

    // Continue Button - Pre Mobile Number Screen
    public static final By PRE_MOBILE_CONTINUE_BTN = AppiumBy.accessibilityId("Continue");

    // Input fields
    public static final By MOBILE_NUMBER_FIELD = By.xpath("//android.widget.EditText");

    // Continue Button - Post Mobile Number Screen
    public static final By POST_MOBILE_CONTINUE_BTN = AppiumBy.accessibilityId("Continue");

    public static final By MPIN_FIELD = AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\")");

    // System location permission (Only this time / One time)
    public static final By LOC_PERMISSION_ALLOW = AppiumBy.id("com.android.permissioncontroller:id/permission_allow_one_time_button");

    // Dialog actions
    public static final By NO_THANKS = AppiumBy.id("android:id/button2");
    public static final By FINGERPRINT = AppiumBy.accessibilityId("Fingerprint");
}