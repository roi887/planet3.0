package com.framework.base;

import com.framework.factory.DriverFactory;
import com.framework.flows.LoginFlow;
import io.appium.java_client.android.AndroidDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    private static final Logger logger =
            LoggerFactory.getLogger(BaseTest.class);

    protected AndroidDriver driver;

    protected LoginFlow loginFlow;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {

        logger.info("======================================");
        logger.info("TEST SETUP STARTED");
        logger.info("======================================");

        driver = DriverFactory.initDriver();

        loginFlow = new LoginFlow(driver);

        logger.info("Driver initialized successfully");
        logger.info("Business flows initialized successfully");

        logger.info("======================================");
        logger.info("TEST SETUP COMPLETED");
        logger.info("======================================");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        logger.info("======================================");
        logger.info("TEST TEARDOWN STARTED");
        logger.info("======================================");

        DriverFactory.unloadDriver();

        logger.info("Driver session terminated successfully");

        logger.info("======================================");
        logger.info("TEST TEARDOWN COMPLETED");
        logger.info("======================================");
    }
}