package doryanbessiere.capturetheflag.minecraft.listener.listeners;

import doryanbessiere.capturetheflag.minecraft.commons.cuboid.Cuboid;
import doryanbessiere.capturetheflag.minecraft.commons.logger.Logger;
import doryanbessiere.capturetheflag.minecraft.flag.Flag;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.game.GameState;
import doryanbessiere.capturetheflag.minecraft.player.GamePlayer;
import doryanbessiere.capturetheflag.minecraft.team.Team;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class PlayerMoveListener implements Listener {

    public static HashMap<Player, Location> lastPlayerLocation = new HashMap<>();

    @EventHandler
    public void PlayerMoveEvent_(PlayerMoveEvent event){
        Player player = event.getPlayer();
        GamePlayer gamePlayer = GameManager.getGamePlayer(player);
        Location to = event.getTo();
        Location from = event.getFrom();

        if(!GameManager.isState(GameState.INGAME))return;

        if(player.getLocation().add(0, -1, 0).getBlock().getType() == Material.SLIME_BLOCK){
            player.setVelocity(player.getLocation().getDirection().multiply(2).setY(0.75));
        }

        if (lastPlayerLocation.containsKey(player)) {
            Location lastLocation = lastPlayerLocation.get(player);
            if (lastLocation.getBlockX() == to.getBlockX() && lastLocation.getBlockY() == to.getBlockY()
                    && lastLocation.getBlockZ() == to.getBlockZ()) return;


            GameManager.map.getAreas().forEach((team, area) -> {
                if(team != gamePlayer.getTeam() && area.isInCube(event.getTo())){
                    event.getTo().setPitch(0);
                    Vector inverseDirectionVec = player.getLocation().getDirection().normalize().multiply(-1);
                    Location behind = player.getLocation().add(inverseDirectionVec);

                    Vector vel = event.getTo().getDirection();
                    if(area.isInCube(behind)){
                        vel = vel.multiply(2).setY(0.5);
                    } else {
                        vel = vel.multiply(-2);
                    }
                    vel = vel.setY(0.5);
                    player.setVelocity(vel);
                }
            });

            GameManager.map.getAreas().forEach((team, area) -> {
                if(area.isInCube(player.getLocation())) {
                    gamePlayer.enter(area);
                }
            });
            gamePlayer.getCopyAreas().forEach(area -> {
                if(!area.isInCube(player.getLocation()))
                    gamePlayer.exit(area);
            });

            if(gamePlayer.getFlag() == null){
                GameManager.map.getFlags().forEach((team, location) -> {
                    if(team != gamePlayer.getTeam()){
                        Flag flag = team.getFlag();
                        if(flag.getLocation().getBlockX() == to.getBlockX() &&
                                team.getFlag().getLocation().getBlockY() == to.getBlockY() &&
                                team.getFlag().getLocation().getBlockZ() == to.getBlockZ()){
                            flag.stolen(gamePlayer);
                            return;
                        }
                    }
                });
            }
            lastPlayerLocation.put(player, to);
        } else {
            lastPlayerLocation.put(player, to);
        }
   }
}
