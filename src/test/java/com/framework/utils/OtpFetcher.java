package com.framework.utils;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtpFetcher {

    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    private static final String SSH_HOST = dotenv.get("SSH_HOST");
    private static final String SSH_USER = dotenv.get("SSH_USER");
    private static final String SSH_PASSWORD = dotenv.get("SSH_PASSWORD");
    private static final String SSH_LOG_PATH = dotenv.get("SSH_LOG_PATH");
    private static final int SSH_PORT = 22;

    public static String getLatestOtp(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty or null.");
        }

        Session session = null;
        ChannelExec channel = null;
        String isolatedOtp = null;

        try {
            JSch jsch = new JSch();
            System.out.println("[SSH Engine] Initiating secure socket connection handshake to " + SSH_HOST + "...");

            session = jsch.getSession(SSH_USER, SSH_HOST, SSH_PORT);
            session.setPassword(SSH_PASSWORD);

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect(10000);
            System.out.println("[SSH Engine] SSH Session Authentication Successful!");

            for (int attempt = 1; attempt <= 3; attempt++) {
                System.out.println("[SSH Engine] Scanning server log entries (Attempt " + attempt + "/3)...");

                // Tail 2000 lines and check 10 lines down to match your repo's multi-line log output format
                String command = "tail -n 2000 " + SSH_LOG_PATH + " | grep -A 10 '" + phoneNumber + "'";

                channel = (ChannelExec) session.openChannel("exec");
                channel.setCommand(command);
                channel.setInputStream(null);

                try (InputStream outputStream = channel.getInputStream();
                     InputStreamReader isr = new InputStreamReader(outputStream);
                     BufferedReader reader = new BufferedReader(isr)) {

                    channel.connect();
                    String logLine;

                    while ((logLine = reader.readLine()) != null) {
                        if (logLine.contains("OTP is :")) {
                            Pattern digitPattern = Pattern.compile("\\b\\d{6}\\b");
                            Matcher matcher = digitPattern.matcher(logLine);

                            if (matcher.find()) {
                                // REMOVED the 'break;'.
                                // We keep updating isolatedOtp so that the LAST line parsed (the newest one) wins.
                                isolatedOtp = matcher.group();
                            }
                        }
                    }
                } finally {
                    if (channel != null && channel.isConnected()) {
                        channel.disconnect();
                    }
                }

                if (isolatedOtp != null) {
                    System.out.println("[SSH Engine] Successfully extracted LATEST OTP token: " + isolatedOtp);
                    break;
                }

                if (attempt < 3) {
                    System.out.println("[SSH Engine] Target OTP layout block missing. Sleeping 4s for server cache flush...");
                    Thread.sleep(4000);
                }
            }

        } catch (Exception e) {
            System.err.println("CRITICAL RUNTIME ERROR: Failed to pipeline log target text arrays over SSH streams.");
            e.printStackTrace();
        } finally {
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
            System.out.println("[SSH Engine] Socket connection closed down cleanly.");
        }

        if (isolatedOtp == null) {
            throw new RuntimeException("Automation Error: Unable to locate 'OTP is : [6-digits]' format layout pattern within server log payloads for phone number: " + phoneNumber);
        }

        return isolatedOtp;
    }
}