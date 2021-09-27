package doryanbessiere.capturetheflag.minecraft.listener.listeners;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.commons.cuboid.Cuboid;
import doryanbessiere.capturetheflag.minecraft.commons.logger.Logger;
import doryanbessiere.capturetheflag.minecraft.flag.Flag;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.game.GameState;
import doryanbessiere.capturetheflag.minecraft.player.GamePlayer;
import doryanbessiere.capturetheflag.minecraft.projector.ProjectorBlock;
import doryanbessiere.capturetheflag.minecraft.projector.ProjectorConfig;
import doryanbessiere.capturetheflag.minecraft.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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

        Block groundBlock = player.getLocation().add(0, -1, 0).getBlock();
        if(groundBlock.getType() == Material.SLIME_BLOCK){
            if(gamePlayer.canProjection()){
                ProjectorBlock projectorBlock = GameManager.getMap().getProjector(groundBlock.getLocation());
                if(projectorBlock != null){
                    ProjectorConfig config = projectorBlock.getConfig();
                    player.teleport(new Location(player.getLocation().getWorld(), player.getLocation().getX(),player.getLocation().getY(),player.getLocation().getZ(), config.getYaw(), config.getPitch()));
                    player.setVelocity(player.getLocation().getDirection().multiply(config.getPower()).setY(0.75));
                    gamePlayer.setCanProjection(false);

                    Bukkit.getScheduler().runTaskLater(CaptureTheFlag.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            gamePlayer.setCanProjection(true);
                        }
                    }, 20 * 3);
                } else {
                    gamePlayer.sendMessage("§dCe bloc n'est pas un propulseur !");
                }
            }
        }

        if (lastPlayerLocation.containsKey(player)) {
            Location lastLocation = lastPlayerLocation.get(player);
            if (lastLocation.getBlockX() == to.getBlockX() && lastLocation.getBlockY() == to.getBlockY()
                    && lastLocation.getBlockZ() == to.getBlockZ()) return;

            if(!gamePlayer.hasRespawn()){
                event.getFrom().setPitch(event.getTo().getPitch());
                event.getFrom().setYaw(event.getTo().getYaw());
                event.setTo(event.getFrom());
                return;
            }

            /**
             * S'occupe d'éjecter les joueurs lorqu'ils n'ont pas le droit d'être dans une zone
             */
            GameManager.getMap().getAreas().forEach((team, area) -> {
                if((team != gamePlayer.getTeam() || (team == gamePlayer.getTeam()) && gamePlayer.getFlag()!= null) && area.isInCube(event.getTo())){
                    float pitch = event.getFrom().getPitch();
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
                    event.getTo().setPitch(pitch);
                }
            });

            /**
             * Permet de définir lorsqu'un joueur rentre dans une zone
             */
            GameManager.getMap().getAreas().forEach((team, area) -> {
                if(area.isInCube(player.getLocation())) {
                    gamePlayer.enter(area);
                }
            });

            /**
             * Permet de définir lorsqu'un joueur quitte une zone
             */
            gamePlayer.getCopyAreas().forEach(area -> {
                if(!area.isInCube(player.getLocation()))
                    gamePlayer.exit(area);
            });

            /**
             * Gestion des drapeau
             */
            Flag.interact(gamePlayer, event.getTo());

            /**
             * Permet de définir l'endroit où placer le drapeau (déplacé par le joueur)
             */
            if(gamePlayer.getFlag() != null)
                gamePlayer.getFlag().setLocation(player.getLocation());

            lastPlayerLocation.put(player, to);
        } else {
            lastPlayerLocation.put(player, to);
        }
   }
}
