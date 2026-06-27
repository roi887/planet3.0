package com.framework.utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import java.time.Duration;
import java.util.Collections;

public class GestureUtils {

    /**
     * Performs a vertical swipe from bottom to top (Scrolls down the page view).
     */
    public static void scrollDown(AppiumDriver driver) {
        Dimension size = driver.manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.70);
        int endY = (int) (size.height * 0.30);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence scroll = new Sequence(finger, 1);

        scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        scroll.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), startX, endY));
        scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(scroll));
    }

    /**
     * Reusable logic to scroll down continuously until a target element appears.
     */
    public static void scrollUntilElementVisible(AppiumDriver driver, By locator, int maxScrolls) {
        int scrollCount = 0;
        while (scrollCount < maxScrolls) {
            try {
                if (driver.findElement(locator).isDisplayed()) {
                    System.out.println("[GestureUtils] Target element found on screen view!");
                    return;
                }
            } catch (Exception e) {
                // Element is not visible or not attached yet, continue down
            }
            System.out.println("[GestureUtils] Element not visible yet, performing scroll " + (scrollCount + 1) + "...");
            scrollDown(driver);
            scrollCount++;
        }
    }
}