package com.framework.pages;

import com.framework.locators.RaiseConsern;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class RaiseConcernPage {

    private final AndroidDriver driver;
    private final RaiseConsern locators;
    private final WebDriverWait wait;

    public RaiseConcernPage(AndroidDriver driver) {
        this.driver = driver;
        this.locators = new RaiseConsern();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Initialize dynamic field elements
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(7)), this.locators);
    }

    public void clickRaiseServiceRequestCard() {
        System.out.println("[Page] Clicking Raise Service Request Card...");
        wait.until(ExpectedConditions.elementToBeClickable(locators.raiseServiceRequestCard)).click();
    }

    public void openCategoryDropdown() {
        System.out.println("[Page] Opening Category Dropdown...");
        wait.until(ExpectedConditions.elementToBeClickable(locators.categoryDropdown)).click();
    }

    public void selectPaymentUpdatedOption() {
        System.out.println("[Page] Selecting Payment Updated Option...");
        wait.until(ExpectedConditions.elementToBeClickable(locators.paymentUpdated)).click();
    }

    public void enterSubject(String subject) {
        System.out.println("[Page] Entering Subject text: " + subject);
        wait.until(ExpectedConditions.elementToBeClickable(locators.queryInputFieldFirst)).sendKeys(subject);
    }

    public void enterDescription(String details) {
        System.out.println("[Page] Entering Description details...");
        wait.until(ExpectedConditions.elementToBeClickable(locators.queryInputFieldSecond)).sendKeys(details);
    }

    public void clickFormActionButtonThree() {
        System.out.println("[Page] Clicking Form Action Button Three (Instance 3)...");
        wait.until(ExpectedConditions.elementToBeClickable(locators.formActionButtonThree)).click();
    }

    public void clickFormActionButtonTwo() {
        System.out.println("[Page] Clicking Form Action Button Two (Instance 2)...");
        wait.until(ExpectedConditions.elementToBeClickable(locators.formActionButtonTwo)).click();
    }

    public void clickSendButton() {
        System.out.println("[Page] Clicking Final Send Submission Button...");
        wait.until(ExpectedConditions.elementToBeClickable(locators.sendButton)).click();
    }
}