package doryanbessiere.capturetheflag.minecraft.listener.listeners;

import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;

public class AsyncPlayerPreLoginListener implements Listener {

    @EventHandler
    public void AsyncPlayerPreLoginEvent_(AsyncPlayerPreLoginEvent event){
        String reason = GameManager.canJoin();
        if(reason != null){
            event.setResult(PlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(reason);
        }
    }
}
