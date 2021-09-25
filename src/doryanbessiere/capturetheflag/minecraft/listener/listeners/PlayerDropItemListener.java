package doryanbessiere.capturetheflag.minecraft.listener.listeners;

import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.game.GameState;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {

    @EventHandler
    public void PlayerDropItemEvent_(PlayerDropItemEvent event){
        if(!GameManager.isState(GameState.FINISH) && event.getPlayer().getGameMode() == GameMode.CREATIVE)return;
        event.setCancelled(true);
    }
}
