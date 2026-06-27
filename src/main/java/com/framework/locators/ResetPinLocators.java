package com.framework.locators;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public final class ResetPinLocators {

    private ResetPinLocators() {
    }

    public static final By RESET_PIN =
            AppiumBy.accessibilityId("Reset PIN");

    public static final By OTP_FIELD =
            AppiumBy.className("android.widget.EditText");

    public static final By VERIFY_BUTTON =
            AppiumBy.accessibilityId("Verify");

    public static final By NEW_PIN_FIELD =
            AppiumBy.androidUIAutomator(
                    "new UiSelector().className(\"android.widget.EditText\").instance(0)");

    public static final By CONFIRM_PIN_FIELD =
            AppiumBy.androidUIAutomator(
                    "new UiSelector().className(\"android.widget.EditText\").instance(1)");

    public static final By CONTINUE_BUTTON =
            AppiumBy.accessibilityId("Continue");

    public static final By NO_THANKS =
            AppiumBy.id("android:id/button2");

    public static final By CLOSE_ICON =
            AppiumBy.androidUIAutomator(
                    "new UiSelector().className(\"android.widget.Button\").instance(1)");
}