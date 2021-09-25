package doryanbessiere.capturetheflag.minecraft.listener.listeners;

import doryanbessiere.capturetheflag.minecraft.commons.logger.Logger;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.game.GameState;
import doryanbessiere.capturetheflag.minecraft.map.Map;
import doryanbessiere.capturetheflag.minecraft.player.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class EntityDamageListener implements Listener {

    private ArrayList<Player> cancelDamage = new ArrayList<>();
    private HashMap<Player, Player> lastDamager = new HashMap<>();

    @EventHandler(priority = EventPriority.LOWEST)
    public void EntityDamageByEntityEvent_(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){
            Player killer = (Player) event.getDamager();
            Player victim = (Player) event.getEntity() ;

            GamePlayer gameVictim = GameManager.getGamePlayer(victim);
            GamePlayer gameKiller = GameManager.getGamePlayer(killer);

            if(gameVictim.getTeam() == gameKiller.getTeam()){
                event.setCancelled(true);
                return;
            }

            if(!cancelDamage.contains(victim))
                cancelDamage.add(victim);
            lastDamager.put(victim, killer);

            if(event.getFinalDamage() >= victim.getHealth()){
                kill(gameVictim, gameKiller);
                Logger.debug("§f"+victim.getName()+" §eà tué §f"+killer.getName());
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
                Player victim = (Player) event.getEntity();

                if(cancelDamage.contains(victim)){
                    cancelDamage.remove(victim);
                    return;
                }

                GamePlayer gameVictim = GameManager.getGamePlayer(victim);
                Map map = GameManager.getMap();

                lastDamager.put(victim, null);

                if(map.getAreas().get(gameVictim.getTeam()).isInCube(victim.getLocation())) {
                    event.setCancelled(true);
                    return;
                }

                if(gameVictim.hasRespawn() && event.getFinalDamage() >= victim.getHealth()){
                    kill(gameVictim, null);
                    event.setCancelled(true);
                }
            }
        }
    }

    public void kill(GamePlayer gameVictim, GamePlayer gameKiller){
        gameVictim.death();
        Player victim = gameVictim.getPlayer();
        victim.getPlayer().playSound(victim.getLocation(), Sound.HURT_FLESH, 3.0F, 0.5F);

        if(gameKiller != null){
            gameKiller.getPlayer().playSound(victim.getLocation(), Sound.HURT_FLESH, 3.0F, 0.5F);
            gameKiller.kill();
        }
    }
}
