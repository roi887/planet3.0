package com.framework.constants;

public final class FrameworkConstants {

    private FrameworkConstants() {
        throw new IllegalStateException(
                "Utility class");
    }

    public static final String CONFIG_FILE_PATH =
            "src/test/resources/config.properties";

    public static final int EXPLICIT_WAIT = 20;

    public static final int IMPLICIT_WAIT = 10;

    public static final int DEFAULT_TIMEOUT = 30;
}