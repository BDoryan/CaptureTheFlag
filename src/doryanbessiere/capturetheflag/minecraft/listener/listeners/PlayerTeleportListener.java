package doryanbessiere.capturetheflag.minecraft.listener.listeners;

import doryanbessiere.capturetheflag.minecraft.commons.logger.Logger;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.game.GameState;
import doryanbessiere.capturetheflag.minecraft.player.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleportListener implements Listener {

    @EventHandler
    public void PlayerTeleportEvent_(PlayerTeleportEvent event){
        Player player = event.getPlayer();
        GamePlayer gamePlayer = GameManager.getGamePlayer(player);

        if(!GameManager.isState(GameState.INGAME))return;
        GameManager.getMap().getAreas().forEach((team, area) -> {
            if(team != gamePlayer.getTeam() && area.isInCube(event.getTo())){
                event.setCancelled(true);
                gamePlayer.sendMessage("§cVous ne pouvez pas aller dans la base de l'ennemie!");
            }
        });
    }
}
