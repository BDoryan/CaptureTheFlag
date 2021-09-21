package doryanbessiere.capturetheflag.minecraft.commons.logger;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Need minecraft event system
 */
public class LoggerEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private LoggerType loggerType;
    private String message;
    private String text;

    public LoggerEvent(LoggerType loggerType, String message, String text) {
        this.loggerType = loggerType;
        this.message = message;
        this.text = text;
    }

    /**
     *
     * @return Return the log type
     */
    public LoggerType getLoggerType() {
        return loggerType;
    }

    /**
     *
     * @return Return the log message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @return Return the complete message
     */
    public String getText() {
        return message;
    }

    public LoggerEvent(boolean isAsync) {
        super(isAsync);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
