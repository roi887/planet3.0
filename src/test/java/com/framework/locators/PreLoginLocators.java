package com.framework.locators;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class PreLoginLocators {
    // Onboarding location toggle - Optimized using Accessibility ID
    public static final By LOCATION_TOGGLE = AppiumBy.accessibilityId("permission_selection_location_toggle");

    // Terms & Conditions Link - Optimized using Accessibility ID
    public static final By TERMS_CONDITIONS_LINK  = AppiumBy.xpath("//*[contains(@content-desc, 'permission_selection_privacy_policy_link')]");
}