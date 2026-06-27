package com.framework.locators;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

/**
 * Repository for all Raise Concern module UI components.
 */
public class RaiseConsern {

    // 1. The entrypoint card for raising a service request
    @AndroidFindBy(accessibility = "services_raise_a_service_request_card")
    public WebElement raiseServiceRequestCard;

    // 2. The Category/Concern Dropdown Selector
    @AndroidFindBy(uiAutomator = "new UiSelector().description(\"raise_a_query_dropdown_category\nChoose a Category\nSelect a Concern\")")
    public WebElement categoryDropdown;

    @AndroidFindBy(accessibility = "Payment updated and not adjusted")
    public WebElement paymentUpdated;
    // 3. The First Text Input Field (e.g., Query Title or Subject)
    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.EditText\").instance(0)")
    public WebElement queryInputFieldFirst;

    // 4. The Second Text Input Field (e.g., Detailed Description)
    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.EditText\").instance(1)")
    public WebElement queryInputFieldSecond;

    // 5. The Secondary Action/Navigation Button (Instance 3)
    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.Button\").instance(3)")
    public WebElement formActionButtonThree;

    // 6. The Primary Form Action Button (Instance 2)
    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.Button\").instance(2)")
    public WebElement formActionButtonTwo;

    // 7. The Final Send/Submit Button Label
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Send\")")
    public WebElement sendButton;
}