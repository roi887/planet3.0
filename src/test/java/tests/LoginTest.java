package tests;

import com.framework.base.BaseTest;
import com.framework.listeners.ExtentReportListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

@Listeners(ExtentReportListener.class)
public class LoginTest extends BaseTest {

    private static final Logger logger =
            LoggerFactory.getLogger(LoginTest.class);

    @Test(description = "Verify Planet Login with valid credentials")
    public void loginToPlanet() {

        logger.info("========== Login Test Started ==========");

        String mobileNumber;
        String mpin;

        try (InputStream is =
                     getClass()
                             .getClassLoader()
                             .getResourceAsStream(
                                     "testdata/users.json")) {

            if (is == null) {

                throw new RuntimeException(
                        "users.json not found");
            }

            JsonObject loginData =
                    JsonParser
                            .parseReader(
                                    new InputStreamReader(is))
                            .getAsJsonObject()
                            .getAsJsonObject("login");

            mobileNumber =
                    loginData.get("mobileNumber")
                            .getAsString();

            mpin =
                    loginData.get("mpin")
                            .getAsString();

        } catch (Exception e) {

            logger.error(
                    "Failed to read login test data",
                    e);

            throw new RuntimeException(e);
        }

        loginFlow.onboardAndNavigateToLogin();

        loginFlow.loginWithCredentials(
                mobileNumber,
                mpin);

        logger.info(
                "========== Login Test Completed ==========");
    }
}