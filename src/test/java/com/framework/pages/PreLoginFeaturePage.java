package com.framework.pages;

import com.framework.locators.PreLoginLocators;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Collections;

public class PreLoginFeaturePage {
    private final AndroidDriver driver;
    private final WebDriverWait wait;

    public PreLoginFeaturePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Interacts with the specialized location checkbox twice
     */
    public void clickLocationCheckboxTwice() {
        System.out.println("[PreLogin] Interacting with feature location checkbox twice...");
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(PreLoginLocators.LOCATION_TOGGLE));
        checkbox.click();
        checkbox.click();
    }

    /**
     * Clicks the specialized Terms and Conditions / Privacy Policy link
     */
    public void clickTermsAndConditions() {
        System.out.println("[PreLogin] Clicking feature Terms & Conditions link...");
        wait.until(ExpectedConditions.elementToBeClickable(PreLoginLocators.TERMS_CONDITIONS_LINK)).click();
    }

    /**
     * Dynamically scrolls down page-by-page until the layout stream can no longer scroll,
     * confidently handling documents up to 22+ pages.
     */
    public void scrollTermsToBottom() {
        System.out.println("[PreLogin] Natively scrolling document window down to reading end bounds using W3C actions...");
        try {
            Dimension size = driver.manage().window().getSize();
            int startX = size.width / 2;
            int startY = (int) (size.height * 0.80); // Finger start near bottom
            int endY = (int) (size.height * 0.20);   // Finger drag to top

            String previousPageSource = "";
            int maxScrollAttempts = 35; // Upper guard rail to prevent any infinite loops

            for (int i = 0; i < maxScrollAttempts; i++) {
                String currentPageSource = driver.getPageSource();

                // If page layout matching before and after is identical, we reached the bottom boundary
                if (currentPageSource.equals(previousPageSource)) {
                    System.out.println("[PreLogin] Reached absolute bottom boundary of the document tree at step: " + i);
                    break;
                }

                previousPageSource = currentPageSource;

                // Build modern W3C Drag Gesture
                PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                Sequence scroll = new Sequence(finger, 1);

                scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
                scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                scroll.addAction(finger.createPointerMove(Duration.ofMillis(400), PointerInput.Origin.viewport(), startX, endY));
                scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

                driver.perform(Collections.singletonList(scroll));

                // Short settle rest time to let UI frame render next set of lines
                Thread.sleep(200);
            }
            System.out.println("[PreLogin] Scrolled successfully to layout bounds.");
        } catch (Exception e) {
            System.out.println("[Warning] Scroll interaction encountered an interruption: " + e.getMessage());
        }
    }

    /**
     * Dispatches a native device hardware back key sequence to escape out of the terms view
     */
    public void clickDeviceBack() {
        System.out.println("[PreLogin] Pressing native Android hardware back step button...");
        driver.navigate().back();
    }
}