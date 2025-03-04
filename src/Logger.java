import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

public class Logger {
    public static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Logger.class.getName());

    static {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new LogFormat()); // Use custom formatter
        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);
        logger.setLevel(Level.INFO);
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void warning(String message) {
        logger.warning(message);
    }

    public static void severe(String message) {
        logger.severe(message);
    }

    public static void setLevel(Level level) {
        logger.setLevel(level);
    }
}
