package com.framework.utils;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class OtpFetcher {

    private static final Logger logger =
            LoggerFactory.getLogger(OtpFetcher.class);

    private static final Dotenv dotenv =
            Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

    private static final String SSH_HOST =
            dotenv.get("SSH_HOST");

    private static final String SSH_USER =
            dotenv.get("SSH_USER");

    private static final String SSH_PASSWORD =
            dotenv.get("SSH_PASSWORD");

    private static final String SSH_LOG_PATH =
            dotenv.get("SSH_LOG_PATH");

    private static final int SSH_PORT = 22;

    private static final Pattern OTP_PATTERN =
            Pattern.compile("\\b\\d{6}\\b");

    private static final int MAX_RETRIES = 3;

    private static final int RETRY_WAIT_MS = 4000;

    private OtpFetcher() {
    }

    public static String getLatestOtp(String phoneNumber) {

        validateInputs(phoneNumber);

        Session session = null;

        try {

            logger.info(
                    "Connecting to SSH host: {}",
                    SSH_HOST);

            JSch jsch = new JSch();

            session =
                    jsch.getSession(
                            SSH_USER,
                            SSH_HOST,
                            SSH_PORT);

            session.setPassword(SSH_PASSWORD);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);

            session.connect(10000);

            logger.info(
                    "SSH session established successfully");

            String otp =
                    fetchOtpFromLogs(
                            session,
                            phoneNumber);

            if (otp == null) {

                throw new RuntimeException(
                        "Unable to locate OTP for phone number: "
                                + phoneNumber);
            }

            logger.info(
                    "OTP extracted successfully");

            return otp;

        } catch (Exception e) {

            logger.error(
                    "Failed to fetch OTP from server logs",
                    e);

            throw new RuntimeException(
                    "OTP retrieval failed",
                    e);

        } finally {

            if (session != null &&
                    session.isConnected()) {

                session.disconnect();

                logger.info(
                        "SSH session closed");
            }
        }
    }

    private static String fetchOtpFromLogs(
            Session session,
            String phoneNumber)
            throws Exception {

        String latestOtp = null;

        for (int attempt = 1;
             attempt <= MAX_RETRIES;
             attempt++) {

            logger.info(
                    "Searching OTP. Attempt {}/{}",
                    attempt,
                    MAX_RETRIES);

            String command =
                    "tail -n 2000 "
                            + SSH_LOG_PATH
                            + " | grep -A 10 '"
                            + phoneNumber
                            + "'";

            ChannelExec channel =
                    (ChannelExec) session.openChannel("exec");

            try {

                channel.setCommand(command);

                try (InputStream stream =
                             channel.getInputStream();

                     BufferedReader reader =
                             new BufferedReader(
                                     new InputStreamReader(stream))) {

                    channel.connect();

                    String line;

                    while ((line = reader.readLine()) != null) {

                        Matcher matcher =
                                OTP_PATTERN.matcher(line);

                        if (matcher.find()) {

                            latestOtp =
                                    matcher.group();
                        }
                    }
                }

            } finally {

                if (channel.isConnected()) {

                    channel.disconnect();
                }
            }

            if (latestOtp != null) {

                logger.info(
                        "OTP found successfully");

                return latestOtp;
            }

            if (attempt < MAX_RETRIES) {

                logger.info(
                        "OTP not found. Waiting {} ms before retry.",
                        RETRY_WAIT_MS);

                Thread.sleep(RETRY_WAIT_MS);
            }
        }

        return null;
    }

    private static void validateInputs(
            String phoneNumber) {

        if (phoneNumber == null ||
                phoneNumber.isBlank()) {

            throw new IllegalArgumentException(
                    "Phone number cannot be empty");
        }

        if (SSH_HOST == null ||
                SSH_USER == null ||
                SSH_PASSWORD == null ||
                SSH_LOG_PATH == null) {

            throw new IllegalStateException(
                    "Missing SSH configuration in .env file");
        }
    }
}