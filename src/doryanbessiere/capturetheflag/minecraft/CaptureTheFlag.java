package doryanbessiere.capturetheflag.minecraft;

import doryanbessiere.capturetheflag.minecraft.command.CTFCommandManager;
import doryanbessiere.capturetheflag.minecraft.commons.logger.Logger;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.listener.ListenersManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CaptureTheFlag extends JavaPlugin {

    @Override
    public void onEnable() {
        super.onEnable();

        ListenersManager.listen(this);
        Logger.debug("Listening to the events : OK");

        CTFCommandManager commandManager = new CTFCommandManager(this);
        commandManager.loadCommands();
        Logger.debug("Loading of the commands : OK");

        GameManager.init();
        Logger.debug("Initialization of the game : OK");

        Logger.info("CaptureTheFlag : OK");
    }

    @Override
    public void onLoad() {
        super.onLoad();
        Logger.info("CaptureTheFlag : loaded");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Logger.info("CaptureTheFlag : disabled");
    }

    public static final String PREFIX = "§7[§dCaptureTheFlag§7]";

    public static String getPrefix(){
        return PREFIX;
    }

    public static void sendMessage(CommandSender sender, String text){
        sender.sendMessage(PREFIX+" "+text);
    }
    public static void sendMessage(Player player, String text){
        player.sendMessage(PREFIX+" "+text);
    }
}
