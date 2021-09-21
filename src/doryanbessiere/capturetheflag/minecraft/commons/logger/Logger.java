package doryanbessiere.capturetheflag.minecraft.commons.logger;

import org.bukkit.Bukkit;

public class Logger {

    public static void error(String message){
        log(LoggerType.ERROR, message);
    }

    public static void warning(String message){
        log(LoggerType.WARNING, message);
    }

    public static void info(String message){
        log(LoggerType.INFO, message);
    }

    public static void debug(String message){
        log(LoggerType.DEBUG, message);
    }

    public static void log(LoggerType loggerType, String message){
        String text = loggerType.getPrefix()+message;
        Bukkit.getPluginManager().callEvent(new LoggerEvent(loggerType, text, message));
        Bukkit.getServer().getConsoleSender().sendMessage(text);
    }
}
