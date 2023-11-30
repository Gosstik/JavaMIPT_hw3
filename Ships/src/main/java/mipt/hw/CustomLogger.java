package mipt.hw;

import java.util.logging.Logger;

public class CustomLogger {
    static final Logger logger;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT %1$tL] [%4$-7s] %5$s %n");
        logger = Logger.getLogger(CustomLogger.class.getName());
    }

    public static void info(String string) {
        logger.info(string);
    }
}
