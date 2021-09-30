package doryanbessiere.capturetheflag.minecraft.listener.listeners;

import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.game.GameState;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {

    @EventHandler
    public void BlockPlaceEvent_(BlockPlaceEvent event){
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE && !GameManager.isState(GameState.FINISH))return;
        event.setCancelled(true);
    }

    @EventHandler
    public void BlockBreakEvent_(BlockBreakEvent event){
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE && !GameManager.isState(GameState.FINISH))return;
        event.setCancelled(true);
    }
}
