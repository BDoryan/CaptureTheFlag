package doryanbessiere.capturetheflag.minecraft.listener;

import doryanbessiere.capturetheflag.minecraft.listener.listeners.EntitySpawnListener;
import doryanbessiere.capturetheflag.minecraft.listener.listeners.LoggerListener;
import doryanbessiere.capturetheflag.minecraft.listener.listeners.PlayerConnectionListener;
import doryanbessiere.capturetheflag.minecraft.listener.listeners.WeatherChangeListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class ListenersManager {

    public static void listen(Plugin plugin){
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerConnectionListener(), plugin);
        pluginManager.registerEvents(new EntitySpawnListener(), plugin);
        pluginManager.registerEvents(new LoggerListener(), plugin);
        pluginManager.registerEvents(new WeatherChangeListener(), plugin);
    }
}
