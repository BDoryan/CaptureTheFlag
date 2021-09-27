package doryanbessiere.capturetheflag.minecraft.listener.listeners;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.command.commands.CaptureTheFlagCommand;
import doryanbessiere.capturetheflag.minecraft.compass.Compass;
import doryanbessiere.capturetheflag.minecraft.flag.Flag;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.map.Map;
import doryanbessiere.capturetheflag.minecraft.map.MapManager;
import doryanbessiere.capturetheflag.minecraft.player.GamePlayer;
import doryanbessiere.capturetheflag.minecraft.projector.ProjectorBlock;
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
        Block block = event.getClickedBlock();

        if(itemStack != null &&
                itemStack.hasItemMeta() && block != null){
            if(itemStack.getItemMeta().getDisplayName().
                    equalsIgnoreCase(GameManager.WAND.getItemMeta().getDisplayName())){
                if(event.getAction() == Action.LEFT_CLICK_BLOCK){
                    GameManager.getPosition1().put(player, block.getLocation());
                    CaptureTheFlag.sendMessage(player, "§aVous avez sélectionner le point A.");
                    event.setCancelled(true);
                } else
                if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
                    GameManager.getPosition2().put(player, block.getLocation());
                    CaptureTheFlag.sendMessage(player, "§aVous avez sélectionner le point B.");
                    event.setCancelled(true);
                }
            } else if(itemStack.getItemMeta().getDisplayName().
                    equalsIgnoreCase(GameManager.PROJECTOR_TOOL.getItemMeta().getDisplayName())){
                if(block.getType() != Material.SLIME_BLOCK){
                    CaptureTheFlag.sendMessage(player, "§cCette outil fonctionne uniquement avec les SLIM_BLOCK !");
                    return;
                }
                String mapName = player.getLocation().getWorld().getName();
                Map map = MapManager.getMap(mapName);

                if(map == null){
                    CaptureTheFlag.sendMessage(player, "§cCette map n'existe pas !");
                    return;
                }
                if(event.getAction() == Action.LEFT_CLICK_BLOCK){
                    ProjectorBlock projectorBlock = map.getProjector(block.getLocation());
                    if(projectorBlock != null){
                        map.removeProjector(projectorBlock);
                        CaptureTheFlag.sendMessage(player, "§aCe bloc a été retiré avec succès.");
                        event.setCancelled(true);
                    } else {
                        CaptureTheFlag.sendMessage(player, "§cCe bloc ne possède pas de configuration !");
                    }
                } else
                if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
                    if(!CaptureTheFlagCommand.projectorConfig.containsKey(gamePlayer)){
                        gamePlayer.sendMessage("§cVous n'avez pas défini de configuration, essayez: §e/ctf help");
                        return;
                    }
                    if(!map.existsProjector(block.getLocation())){
                        map.addProjector(new ProjectorBlock(block.getLocation(), CaptureTheFlagCommand.projectorConfig.get(gamePlayer)));
                        CaptureTheFlag.sendMessage(player, "§aCe bloc a été retiré avec succès.");
                        event.setCancelled(true);
                    } else {
                        CaptureTheFlag.sendMessage(player, "§cCe bloc a déjà une configuration ! §7(Retirer le si vous voulez le redéfinir)");
                    }
                }
            }
        } else {
            if(block != null && block.getType() == Material.STANDING_BANNER){
                if(block.getLocation().distance(player.getLocation()) > 4 || GameManager.getMap().getAreas().get(gamePlayer.getTeam()).isInCube(player.getLocation())){
                    return;
                }

                Flag.interact(gamePlayer, block.getLocation());
            }
        }
    }
}
