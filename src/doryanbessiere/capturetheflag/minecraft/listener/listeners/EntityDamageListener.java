package doryanbessiere.capturetheflag.minecraft.listener.listeners;

import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.game.GameState;
import doryanbessiere.capturetheflag.minecraft.map.Map;
import doryanbessiere.capturetheflag.minecraft.player.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {


    @EventHandler
    public void EntityDamageByEntityEvent_(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player){
            Player damager = (Player) event.getEntity();
            GamePlayer gameDamager = GameManager.getGamePlayer(damager);

            if(!gameDamager.hasRespawn()){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void EntityDamageEvent_(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            if(GameManager.isState(GameState.WAITING)){
                event.setCancelled(true);
            }

            if(GameManager.isState(GameState.INGAME)){
                Player player = (Player) event.getEntity();
                GamePlayer gamePlayer = GameManager.getGamePlayer(player);
                Map map = GameManager.getMap();

                if(map.getAreas().get(gamePlayer.getTeam()).isInCube(player.getLocation())) {
                    event.setCancelled(true);
                    return;
                }

                if(player.getHealth() - event.getFinalDamage() < 0){
                    event.setCancelled(true);
                    gamePlayer.death();
                }
            }
        }
    }
}
