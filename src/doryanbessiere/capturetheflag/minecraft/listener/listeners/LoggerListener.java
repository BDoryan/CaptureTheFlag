package doryanbessiere.capturetheflag.minecraft.listener.listeners;

import doryanbessiere.capturetheflag.minecraft.commons.logger.LoggerEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class LoggerListener implements Listener {

    public static final ArrayList<Player> listeners = new ArrayList<>();

    public static void addListener(Player player){
        if(!player.isOnline())
            return;
        listeners.add(player);
    }

    public static boolean contains(Player player){
        return listeners.contains(player);
    }

    public static void removeListener(Player player){
        listeners.remove(player);
    }

    @EventHandler
    public void LoggerEvent_(LoggerEvent event){
        for(Player player : listeners){
            if(!player.isOnline()){
                removeListener(player);
                continue;
            }
            player.sendMessage(event.getText());
        }
    }
}
