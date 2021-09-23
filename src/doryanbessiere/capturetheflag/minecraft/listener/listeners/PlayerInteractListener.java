package doryanbessiere.capturetheflag.minecraft.listener.listeners;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void PlayerInteractEvent_(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack itemStack = player.getItemInHand();
        if(itemStack != null &&
                itemStack.hasItemMeta() &&
                itemStack.getItemMeta().getDisplayName().
                        equalsIgnoreCase(GameManager.WAND.getItemMeta().getDisplayName())){

            Block block = event.getClickedBlock();
            if(event.getAction() == Action.LEFT_CLICK_BLOCK){
                GameManager.position1.put(player, block.getLocation());
                CaptureTheFlag.sendMessage(player, "§aVous avez sélectionner le point A.");
            }
            if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
                GameManager.position2.put(player, block.getLocation());
                CaptureTheFlag.sendMessage(player, "§aVous avez sélectionner le point B.");
            }
        }
    }
}
