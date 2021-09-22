package doryanbessiere.capturetheflag.minecraft.listener.listeners;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    @EventHandler
    public void PlayerJoinEvent_(PlayerJoinEvent event){
        Player player = event.getPlayer();
        GameManager.join(player);
        event.setJoinMessage(null);
    }

    @EventHandler
    public void PlayerQuitEvent_(PlayerQuitEvent event){
        Player player = event.getPlayer();
        GameManager.quit(player);
        event.setQuitMessage(null);
    }
}
