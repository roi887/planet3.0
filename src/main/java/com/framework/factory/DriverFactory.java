package com.framework.factory;

import com.framework.config.ConfigurationManager;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

public final class DriverFactory {


    private static final ThreadLocal<AndroidDriver> DRIVER =
            new ThreadLocal<>();

    private DriverFactory() {
    }

    /**
     * Initialize Android Driver
     */
    public static AndroidDriver initDriver() {

        try {

            System.out.println("======================================");
            System.out.println("Initializing Appium Driver Session");
            System.out.println("======================================");

            UiAutomator2Options options =
                    new UiAutomator2Options();

            String deviceName =
                    ConfigurationManager.get("deviceName");

            String udid =
                    ConfigurationManager.get("udid");

            boolean noReset =
                    ConfigurationManager.getBoolean("noReset");

            boolean fullReset =
                    ConfigurationManager.getBoolean("fullReset");

            String appiumUrl =
                    ConfigurationManager.get("appiumUrl");

            options.setPlatformName(
                    ConfigurationManager.get("platformName"));

            options.setPlatformVersion(
                    ConfigurationManager.get("platformVersion"));

            options.setDeviceName(deviceName);

            options.setUdid(udid);

            options.setAutomationName(
                    ConfigurationManager.get("automationName"));

            options.setAppPackage(
                    ConfigurationManager.get("appPackage"));

            options.setAppActivity(
                    ConfigurationManager.get("appActivity"));

            options.setNoReset(noReset);

            options.setFullReset(fullReset);

            options.setAutoGrantPermissions(false);

            options.setDisableWindowAnimation(true);

            int adbTimeout = 90000;

            options.setCapability(
                    "appium:uiautomator2ServerLaunchTimeout",
                    adbTimeout);

            options.setCapability(
                    "appium:uiautomator2ServerReadTimeout",
                    adbTimeout);

            options.setCapability(
                    "appium:adbExecTimeout",
                    adbTimeout);

            System.out.println("========== DRIVER CONFIG ==========");
            System.out.println("Device Name : " + deviceName);
            System.out.println("UDID        : " + udid);
            System.out.println("No Reset    : " + noReset);
            System.out.println("Full Reset  : " + fullReset);
            System.out.println("Appium URL  : " + appiumUrl);
            System.out.println("===================================");

            verifyAppiumServer(appiumUrl);

            AndroidDriver driver =
                    new AndroidDriver(
                            new URL(appiumUrl),
                            options);

            driver.manage()
                    .timeouts()
                    .implicitlyWait(Duration.ofSeconds(10));

            DRIVER.set(driver);

            System.out.println(
                    "[SUCCESS] Appium Driver initialized successfully.");

            return driver;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to initialize Appium Driver",
                    e);
        }
    }

    /**
     * Get active driver
     */
    public static AndroidDriver getDriver() {

        return DRIVER.get();
    }

    /**
     * Quit and cleanup driver
     */
    public static void unloadDriver() {

        AndroidDriver driver = getDriver();

        if (driver != null) {

            try {

                driver.quit();

                System.out.println(
                        "[INFO] Driver session closed successfully.");

            } catch (Exception e) {

                System.out.println(
                        "[WARN] Failed to close driver session.");
            }

            DRIVER.remove();
        }
    }

    /**
     * Verify Appium Server
     */
    private static void verifyAppiumServer(
            String appiumBaseUrl) {

        try {

            String statusUrlString =
                    appiumBaseUrl.endsWith("/")
                            ? appiumBaseUrl + "status"
                            : appiumBaseUrl + "/status";

            URL statusUrl =
                    new URL(statusUrlString);

            HttpURLConnection connection =
                    (HttpURLConnection)
                            statusUrl.openConnection();

            connection.setRequestMethod("GET");

            connection.setConnectTimeout(5000);

            connection.setReadTimeout(5000);

            int responseCode =
                    connection.getResponseCode();

            if (responseCode < 200
                    || responseCode >= 300) {

                throw new IllegalStateException(
                        "Appium server returned HTTP "
                                + responseCode);
            }

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Appium server is not reachable. "
                            + "Please start Appium at: "
                            + appiumBaseUrl,
                    e);
        }
    }

}
