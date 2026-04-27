package utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class AppLogger {

    private static final Logger LOGGER = Logger.getLogger("HotelNova");

    static {
        try {
            FileHandler fileHandler = new FileHandler("app.log", true);
            fileHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    return "[" + record.getLevel() + "] " + record.getMessage() + System.lineSeparator();
                }
            });
            LOGGER.addHandler(fileHandler);
            LOGGER.setUseParentHandlers(true);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "No fue posible iniciar archivo de logs", e);
        }
    }

    private AppLogger() {
    }

    public static void http(String method, String path) {
        LOGGER.info(method + " " + path);
    }

    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void error(String message, Throwable ex) {
        LOGGER.log(Level.SEVERE, message, ex);
    }
}
