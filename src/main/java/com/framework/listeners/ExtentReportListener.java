package com.framework.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExtentReportListener implements ITestListener {

    private static final List<TestOutcome> OUTCOMES = Collections.synchronizedList(new ArrayList<>());

    private enum Status {
        PASSED,
        FAILED,
        SKIPPED
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("====== TEST STARTED: " + result.getName() + " ======");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("====== TEST PASSED: " + result.getName() + " ======");
        OUTCOMES.add(new TestOutcome(result, Status.PASSED));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.err.println("====== TEST FAILED: " + result.getName() + " ======");
        Throwable throwable = result.getThrowable();
        System.err.println("Reason: " + (throwable == null ? "Unknown" : throwable.getMessage()));
        OUTCOMES.add(new TestOutcome(result, Status.FAILED));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("====== TEST SKIPPED: " + result.getName() + " ======");
        OUTCOMES.add(new TestOutcome(result, Status.SKIPPED));
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("====== ALL TEST TESTS FINISHED ======");
        writeHtmlReport(context);
        printSummary(context);
    }

    private void printSummary(ITestContext context) {
        System.out.println("====== TEST SUMMARY ======");
        System.out.println("Passed: " + context.getPassedTests().size());
        System.out.println("Failed: " + context.getFailedTests().size());
        System.out.println("Skipped: " + context.getSkippedTests().size());
        System.out.println("Report: target/test-reports/test-summary.html");
    }

    private void writeHtmlReport(ITestContext context) {
        try {
            Path reportDir = Paths.get(System.getProperty("user.dir"), "target", "test-reports");
            Files.createDirectories(reportDir);

            Path reportFile = reportDir.resolve("test-summary.html");
            Files.writeString(reportFile, buildHtmlReport(context), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("====== REPORT GENERATION FAILED ======");
            System.err.println(e.getMessage());
        }
    }

    private String buildHtmlReport(ITestContext context) {
        List<TestOutcome> snapshot;
        synchronized (OUTCOMES) {
            snapshot = new ArrayList<>(OUTCOMES);
        }

        long passed = snapshot.stream().filter(outcome -> outcome.status == Status.PASSED).count();
        long failed = snapshot.stream().filter(outcome -> outcome.status == Status.FAILED).count();
        long skipped = snapshot.stream().filter(outcome -> outcome.status == Status.SKIPPED).count();
        String generatedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        StringBuilder rows = new StringBuilder();
        for (TestOutcome outcome : snapshot) {
            rows.append("<tr class='")
                    .append(outcome.status.name().toLowerCase())
                    .append("'>")
                    .append("<td>").append(escape(outcome.testName)).append("</td>")
                    .append("<td>").append(escape(outcome.className)).append("</td>")
                    .append("<td>").append(outcome.status).append("</td>")
                    .append("<td>").append(escape(outcome.message)).append("</td>")
                    .append("</tr>");
        }

        return "<!DOCTYPE html>" +
                "<html><head><meta charset='UTF-8'>" +
                "<title>Test Summary Report</title>" +
                "<style>" +
                "body{font-family:Arial,sans-serif;margin:24px;background:#f6f7fb;color:#1f2937;}" +
                ".card{background:#fff;border-radius:12px;padding:20px;box-shadow:0 8px 24px rgba(0,0,0,.08);margin-bottom:16px;}" +
                ".stats{display:flex;gap:12px;flex-wrap:wrap;}" +
                ".stat{padding:12px 16px;border-radius:10px;background:#eef2ff;min-width:140px;}" +
                ".passed{background:#dcfce7;} .failed{background:#fee2e2;} .skipped{background:#fef3c7;}" +
                "table{width:100%;border-collapse:collapse;background:#fff;border-radius:12px;overflow:hidden;}" +
                "th,td{padding:12px 10px;border-bottom:1px solid #e5e7eb;text-align:left;vertical-align:top;}" +
                "th{background:#111827;color:#fff;}" +
                "tr.passed{background:#f0fdf4;} tr.failed{background:#fef2f2;} tr.skipped{background:#fffbeb;}" +
                "</style></head><body>" +
                "<div class='card'><h1>Test Summary Report</h1>" +
                "<p><strong>Suite:</strong> " + escape(context.getSuite().getName()) + "</p>" +
                "<p><strong>Generated:</strong> " + generatedAt + "</p>" +
                "<div class='stats'>" +
                "<div class='stat passed'><strong>Passed</strong><br>" + passed + "</div>" +
                "<div class='stat failed'><strong>Failed</strong><br>" + failed + "</div>" +
                "<div class='stat skipped'><strong>Skipped</strong><br>" + skipped + "</div>" +
                "<div class='stat'><strong>Total</strong><br>" + snapshot.size() + "</div>" +
                "</div></div>" +
                "<table><thead><tr><th>Test</th><th>Class</th><th>Status</th><th>Details</th></tr></thead><tbody>" +
                rows +
                "</tbody></table>" +
                "</body></html>";
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private static final class TestOutcome {
        private final String testName;
        private final String className;
        private final Status status;
        private final String message;

        private TestOutcome(ITestResult result, Status status) {
            this.testName = result.getName();
            this.className = result.getTestClass().getName();
            this.status = status;
            Throwable throwable = result.getThrowable();
            this.message = throwable == null ? "" : throwable.getMessage();
        }
    }
}