package doryanbessiere.capturetheflag.minecraft.listener.listeners;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.compass.Compass;
import doryanbessiere.capturetheflag.minecraft.flag.Flag;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.player.GamePlayer;
import doryanbessiere.capturetheflag.minecraft.team.Team;
import org.bukkit.Location;
import org.bukkit.Material;
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
        GamePlayer gamePlayer = GameManager.getGamePlayer(player);

        ItemStack itemStack = player.getItemInHand();
        if(itemStack != null && itemStack.getType() == Material.COMPASS && gamePlayer.getCompass().compare(itemStack)){
            Compass compass = gamePlayer.getCompass();
            compass.switchMode();
            gamePlayer.sendMessage("§7Vous êtes désormais en mode §f"+compass.getCompassMode().toString().toLowerCase()+"§7 !");
        }

        if(itemStack != null &&
                itemStack.hasItemMeta() &&
                itemStack.getItemMeta().getDisplayName().
                        equalsIgnoreCase(GameManager.WAND.getItemMeta().getDisplayName())){

            Block block = event.getClickedBlock();
            if(event.getAction() == Action.LEFT_CLICK_BLOCK){
                GameManager.getPosition1().put(player, block.getLocation());
                CaptureTheFlag.sendMessage(player, "§aVous avez sélectionner le point A.");
            }
            if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
                GameManager.getPosition2().put(player, block.getLocation());
                CaptureTheFlag.sendMessage(player, "§aVous avez sélectionner le point B.");
            }
        } else {
            Block block = event.getClickedBlock();

            if(block != null && block.getType() == Material.STANDING_BANNER){
                if(block.getLocation().distance(player.getLocation()) > 4 || GameManager.getMap().getAreas().get(gamePlayer.getTeam()).isInCube(player.getLocation())){
                    return;
                }

                Flag.interact(gamePlayer, block.getLocation());
            }
        }
    }
}
