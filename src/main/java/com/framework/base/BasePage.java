package com.framework.base;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collections;

public class BasePage {

    protected AppiumDriver driver;
    protected WebDriverWait wait;

    private static final int DEFAULT_WAIT = 20;

    public BasePage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT));
    }

    /**
     * Wait until element is visible
     */
    protected WebElement waitForVisibility(By locator) {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait until element is clickable
     */
    protected WebElement waitForClickable(By locator) {
        return wait.until(
                ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Click element
     */
    protected void click(By locator) {
        System.out.println("[ACTION] Clicking: " + locator);
        waitForClickable(locator).click();
    }

    /**
     * Enter text
     */
    protected void type(By locator, String text) {
        System.out.println("[ACTION] Typing into: " + locator);

        WebElement element = waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Get element text
     */
    protected String getText(By locator) {
        return waitForVisibility(locator).getText();
    }

    /**
     * Check if element is displayed
     */
    public boolean isDisplayed(By locator) {
        try {
            return waitForVisibility(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if element exists
     */
    protected boolean isPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    /**
     * Wait for element to disappear
     */
    protected void waitForInvisibility(By locator) {
        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Hide keyboard safely
     */
    public void hideKeyboard() {

        try {

            if (driver instanceof AndroidDriver androidDriver) {
                androidDriver.hideKeyboard();
                System.out.println("[ACTION] Keyboard hidden");
            }

        } catch (Exception e) {

            try {
                driver.navigate().back();
                System.out.println("[ACTION] Back navigation used to close keyboard");
            } catch (Exception ignored) {
                System.out.println("[INFO] Keyboard not displayed");
            }

        }
    }

    /**
     * Scroll to text
     */
    protected void scrollToText(String text) {

        driver.findElement(
                AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true))"
                                + ".scrollIntoView(new UiSelector().text(\""
                                + text + "\"))"));
    }

    /**
     * Swipe Up
     */
    protected void swipeUp() {

        Dimension size = driver.manage().window().getSize();

        int startX = size.width / 2;
        int startY = (int) (size.height * 0.8);
        int endY = (int) (size.height * 0.2);

        PointerInput finger =
                new PointerInput(PointerInput.Kind.TOUCH, "finger");

        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(
                finger.createPointerMove(
                        Duration.ZERO,
                        PointerInput.Origin.viewport(),
                        startX,
                        startY));

        swipe.addAction(
                finger.createPointerDown(
                        PointerInput.MouseButton.LEFT.asArg()));

        swipe.addAction(
                finger.createPointerMove(
                        Duration.ofMillis(700),
                        PointerInput.Origin.viewport(),
                        startX,
                        endY));

        swipe.addAction(
                finger.createPointerUp(
                        PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));

        System.out.println("[ACTION] Swipe Up performed");
    }

    /**
     * Tap by coordinates
     */
    protected void tap(int x, int y) {

        PointerInput finger =
                new PointerInput(PointerInput.Kind.TOUCH, "finger");

        Sequence tap = new Sequence(finger, 1);

        tap.addAction(
                finger.createPointerMove(
                        Duration.ZERO,
                        PointerInput.Origin.viewport(),
                        x,
                        y));

        tap.addAction(
                finger.createPointerDown(
                        PointerInput.MouseButton.LEFT.asArg()));

        tap.addAction(
                finger.createPointerUp(
                        PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(tap));

        System.out.println(
                "[ACTION] Tap performed at X="
                        + x
                        + " Y="
                        + y);
    }
}