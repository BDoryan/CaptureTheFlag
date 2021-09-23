package doryanbessiere.capturetheflag.minecraft;

import doryanbessiere.capturetheflag.minecraft.command.CTFCommandManager;
import doryanbessiere.capturetheflag.minecraft.commons.logger.Logger;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.listener.ListenersManager;
import doryanbessiere.capturetheflag.minecraft.map.MapManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class CaptureTheFlag extends JavaPlugin {

    public static CaptureTheFlag instance;

    public static CaptureTheFlag getInstance() {
        return instance;
    }

    private CTFCommandManager commandManager;

    public static void saveConfiguration() {
        instance.saveConfig();
    }

    private File maps_directory;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        super.onEnable();

        ListenersManager.listen(this);
        Logger.debug("Listening to the events : OK");

        commandManager = new CTFCommandManager(this);
        commandManager.loadCommands();
        Logger.debug("Loading of the commands : OK");

        MapManager.loads(getConfiguration());
        Logger.debug("Loading maps : OK");

        GameManager.init();
        Logger.debug("Initialization of the game : OK");

        Logger.info("CaptureTheFlag : OK");

        for(Player player : Bukkit.getOnlinePlayers()){
            GameManager.join(player);
        }
    }

    public CTFCommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public void onLoad() {
        super.onLoad();

        instance = this;

        Logger.info("CaptureTheFlag : loaded");
    }

    @Override
    public void onDisable() {
        this.saveDefaultConfig();
        super.onDisable();

        Logger.info("CaptureTheFlag : disabled");
    }

    public static final String PREFIX = "§8[§cCTF§8] ";

    public static FileConfiguration getConfiguration(){
        return instance.getConfig();
    }

    public static String getPrefix(){
        return PREFIX;
    }

    public static void sendMessage(CommandSender sender, String text){
        sender.sendMessage(text);
    }
    public static void sendMessage(Player player, String text){
        player.sendMessage(text);
    }
    public static void broadcast(String text){
        Bukkit.broadcastMessage(text);
    }
}
