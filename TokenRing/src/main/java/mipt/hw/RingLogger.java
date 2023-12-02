package mipt.hw;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class RingLogger {

    private static final String LOGS_DIRECTORY_NAME = "logs";
    private static final Logger LOGGER;
    private static final SimpleFormatter FORMATTER;
    private static boolean disableLog = false;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT %1$tL] [%4$-7s] %5$s %n");
        FORMATTER = new SimpleFormatter();

        LOGGER = Logger.getLogger(RingLogger.class.getName());
        LOGGER.setUseParentHandlers(false);

        createLogDir();
    }

    public static String getLogsDirName() {
        return LOGS_DIRECTORY_NAME;
    }

    public static void removeHandlers() {
        LOGGER.setUseParentHandlers(false);
    }

    public static void addFileHandler(File file) {
        Handler fh = null;
        try {
            fh = new FileHandler(file.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fh.setFormatter(FORMATTER);
        LOGGER.addHandler(fh);
    }

    public static void addConsoleHandler() {
        SimpleFormatter fmt = new SimpleFormatter();
        StreamHandler sh = new StreamHandler(System.out, fmt);
        LOGGER.addHandler(sh);
    }

    public static void info(String string) {
        if (!disableLog) {
            LOGGER.info(string);
        }
    }

    public static void warning(String string) {
        if (!disableLog) {
            LOGGER.log(Level.WARNING, string);
        }
    }

    public static void disableLogging() {
        disableLog = true;
    }

    private static void createLogDir() {
        Path dirPath = Paths.get(LOGS_DIRECTORY_NAME);
       if (Files.isDirectory(dirPath)) {
            return;
        }

        boolean dirCreated = new File(LOGS_DIRECTORY_NAME).mkdirs();
        if (!dirCreated) {
            throw new RuntimeException(
                    "Unable to create directory \"" + LOGS_DIRECTORY_NAME + "\"");
        }
    }
}
