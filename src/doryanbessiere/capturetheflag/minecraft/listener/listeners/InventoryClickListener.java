package doryanbessiere.capturetheflag.minecraft.listener.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void InventoryClickEvent_(InventoryClickEvent event){
        if(event.getWhoClicked() instanceof Player && ((Player) event.getWhoClicked()).getGameMode() != GameMode.CREATIVE){
            event.setCancelled(true);
        }
    }
}
