package com.framework.factory;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;
import org.testng.SkipException;

public class DriverFactory {

    // ThreadLocal prevents driver leakage if you scale tests to execute in parallel later
    private static final ThreadLocal<AndroidDriver> driverThreadLocal = new ThreadLocal<>();
    private static final Properties configProperties = new Properties();

    /**
     * Initializes global framework environment variables from your configuration properties file.
     * Maps the config.properties file into memory.
     */
    public void initProperties() {
        System.out.println("[Config] Initializing global framework configuration environment properties...");
        try {
            String configFilePath = System.getProperty("user.dir") + "/src/test/resources/config.properties";
            try (FileInputStream fileInputStream = new FileInputStream(configFilePath)) {
                configProperties.load(fileInputStream);
                System.out.println("[Config] Property configuration matrices loaded successfully.");
            }
        } catch (IOException e) {
            System.out.println("[Warning] config.properties file lookup skipped or not found at root path. Using baseline fail-safes.");
        }
    }

    /**
     * Spins up a dynamic, clean Appium automation capability infrastructure session.
     * Resolves daily session initialization failures by avoiding system app uninstalls.
     * @return Fresh or existing active AndroidDriver instance
     */
    public static AndroidDriver initDriver(Properties prop) {
        try {
            System.out.println("====== Initializing Test Configuration Setup ======");

            Properties activeProps = (prop != null && !prop.isEmpty()) ? prop : configProperties;
            UiAutomator2Options options = new UiAutomator2Options();

            // 1. Core Device Capabilities (Cleaned up fallbacks to prevent accidental Wi-Fi routing lockouts)
            String targetDevice = activeProps.getProperty("deviceName", "BMHIXOINWSOF9T9T");
            String targetUdid = activeProps.getProperty("udid", "BMHIXOINWSOF9T9T");

            options.setPlatformName(activeProps.getProperty("platformName", "Android"));
            options.setPlatformVersion(activeProps.getProperty("platformVersion", "15")); // Set to 15 to reflect your target OS level
            options.setDeviceName(targetDevice);
            options.setUdid(targetUdid);
            options.setAutomationName(activeProps.getProperty("automationName", "UiAutomator2"));

            // 2. Target Application Packaging Capabilities
            options.setAppPackage(activeProps.getProperty("appPackage", "com.ltfs.planet.uat"));
            options.setAppActivity(activeProps.getProperty("appActivity", "com.ltfs.d2c.MainActivity"));

            // XIAOMI & ANDROID 15 STABILIZATION GATES (Prevents proxy "Socket Hang Up" errors during inputs)
            options.setCapability("appium:ensureWebviewsHavePages", true);
            options.setCapability("appium:resetKeyboard", true);
            options.setCapability("appium:unicodeKeyboard", true);

            // 3. Dynamic Reset Configuration Reading
            boolean noResetVal = Boolean.parseBoolean(activeProps.getProperty("noReset", "false"));
            boolean fullResetVal = Boolean.parseBoolean(activeProps.getProperty("fullReset", "false"));
            boolean amStartHomeVal = Boolean.parseBoolean(activeProps.getProperty("amStartAsHome", "false"));

            options.setAutoGrantPermissions(false);
            options.setDisableWindowAnimation(true);

            options.setNoReset(noResetVal);
            options.setFullReset(fullResetVal);
            options.setCapability("appium:amStartAsHome", amStartHomeVal);

            // 4. Reset & Speed Safe-Routing Engine
            if (noResetVal) {
                System.out.println("[Sandbox Mode] 'noReset=true' detected. Bypassing validation pipelines for sub-5s startup...");

                options.setCapability("appium:skipServerInstallation", true);
                options.setCapability("appium:skipVerify", true);
                options.setCapability("appium:dontStopAppOnReset", true);
                options.setCapability("appium:shouldTerminateApp", false);
                options.setCapability("appium:forceAppLaunch", false);
                options.setCapability("appium:enforceAppInstall", false);
                options.setCapability("appium:fastReset", false);
                options.setCapability("appium:appWaitActivity", "*");
                options.setCapability("appium:clearDeviceLogsOnStart", false);
            } else {
                System.out.println("[Production Flow] 'noReset=false' detected. Deploying complete automation flow...");

                options.setCapability("appium:dontStopAppOnReset", false);
                options.setCapability("appium:shouldTerminateApp", true);
                options.setCapability("appium:forceAppLaunch", true);
                options.setCapability("appium:enforceAppInstall", false);
                options.setCapability("appium:skipServerInstallation", false);
                options.setCapability("appium:skipVerify", false);

                System.out.println("[Fix] Bypassing package signature uninstalls to prevent DELETE_FAILED_INTERNAL_ERROR.");
            }

            // 5. CRITICAL TIMEOUT BUFFERS
            int timeoutThreshold = noResetVal ? 30000 : 90000;
            options.setCapability("appium:uiautomator2ServerLaunchTimeout", timeoutThreshold);
            options.setCapability("appium:uiautomator2ServerReadTimeout", timeoutThreshold);
            options.setCapability("appium:adbExecTimeout", timeoutThreshold);

            // 6. Dynamic Appium Server URL Connection Routing
            String targetAppiumUrl = activeProps.getProperty("appiumUrl", "http://127.0.0.1:4723");
            if (!targetAppiumUrl.endsWith("/")) {
                targetAppiumUrl = targetAppiumUrl + "/";
            }

            boolean skipIfAppiumUnavailable = Boolean.parseBoolean(activeProps.getProperty("skipIfAppiumUnavailable", "true"));
            verifyAppiumServer(targetAppiumUrl, skipIfAppiumUnavailable);

            URL url = new URL(targetAppiumUrl);

            // 7. Driver Session Handshake Lifecycle Initialization via ThreadLocal
            AndroidDriver localDriver = new AndroidDriver(url, options);
            localDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            driverThreadLocal.set(localDriver);
            System.out.println("[Factory] Android Driver dynamic capability handshake completed successfully via ThreadLocal.");

        } catch (SkipException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Appium Driver dynamically: " + e.getMessage(), e);
        }
        return getDriver();
    }

    /**
     * Safe retrieval tool for tearing down or accessing active automation sessions cleanly
     */
    public static AndroidDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Cleans up the ThreadLocal driver pointer after a test finishes to prevent background process memory leaks.
     */
    public static void unloadDriver() {
        if (getDriver() != null) {
            driverThreadLocal.remove();
        }
    }

    private static void verifyAppiumServer(String appiumBaseUrl, boolean skipIfUnavailable) {
        try {
            URL statusUrl = new URL(appiumBaseUrl + "status");
            HttpURLConnection connection = (HttpURLConnection) statusUrl.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                return;
            }

            if (skipIfUnavailable) {
                throw new SkipException("Appium server returned HTTP " + responseCode + " at " + statusUrl);
            }
            throw new IllegalStateException("Appium server returned HTTP " + responseCode + " at " + statusUrl);
        } catch (Exception e) {
            String runtimeErrorMessage = "Appium server is not reachable at " + appiumBaseUrl + "status. Ensure Appium is running.";
            if (!skipIfUnavailable) {
                throw new IllegalStateException(runtimeErrorMessage, e);
            }
            throw new SkipException(runtimeErrorMessage, e);
        }
    }
}