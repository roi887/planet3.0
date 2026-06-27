package com.framework.locators;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class ResetPinLocators {
    // Buttons and links - Optimized using Accessibility ID
    public static final By RESETPIN = AppiumBy.accessibilityId("Reset PIN");
    public static final By VERIFY = AppiumBy.accessibilityId("Verify");

    // OTP Input Field
    public static final By ENTEROTP = AppiumBy.className("android.widget.EditText");

    // Secure PIN and Confirm PIN inputs - Native UiAutomator instances (highly stable)
    public static final By SECPIN = AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").instance(0)");
    public static final By CONPIN = AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").instance(1)");

    // Final continue container wrapper click element - Optimized using Accessibility ID
    public static final By CON = AppiumBy.accessibilityId("Continue");

    // Dialog / Pop-up Dismissal Buttons
    public static final By NO_THANKS = AppiumBy.id("android:id/button2");
    public static final By CLOSE_X_ICON = AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.Button\").instance(1)");
}