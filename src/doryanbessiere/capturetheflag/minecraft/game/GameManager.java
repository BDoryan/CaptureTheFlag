package doryanbessiere.capturetheflag.minecraft.game;

import com.google.common.collect.Lists;
import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.commons.config.ConfigurationUtils;
import doryanbessiere.capturetheflag.minecraft.commons.logger.Logger;
import doryanbessiere.capturetheflag.minecraft.player.GamePlayer;
import doryanbessiere.capturetheflag.minecraft.schedulers.ScoreboardRunnable;
import doryanbessiere.capturetheflag.minecraft.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class GameManager {

    public static int MIN_PLAYERS = 6;
    public static int MAX_PLAYERS = 40;

    public static HashMap<String, GamePlayer> players = new HashMap<>();
    public static GameState state = GameState.WAITING;

    /**
     * This method is executed when a player joins the server (the game)
     *
     * @param player
     */
    public static void join(Player player){
        GamePlayer gamePlayer = new GamePlayer(player);
        players.put(gamePlayer.getUUIDString(), gamePlayer);

        if(state == GameState.WAITING){
            /*
            Bukkit.broadcastMessage(CaptureTheFlag.getPrefix()+"§f"+player.getName()+" §aà rejoint la partie. §7(§f"+GameManager.getPlayers().size()+"§8/§f"+GameManager.MAX_PLAYERS+"§7).");
            if(GameManager.getPlayers().size() < GameManager.MIN_PLAYERS){
                Bukkit.broadcastMessage(CaptureTheFlag.getPrefix()+"§7Il faut minimum "+MIN_PLAYERS+" joueurs afin que la partie se lance..");
            }*/

            player.setGameMode(GameMode.ADVENTURE);
            gamePlayer.clean();
            teleportToLobby(player);
        }
    }

    /**
     * This method is executed when a player leaves the server (the game)
     *
     * @param player
     */
    public static void quit(Player player){
        GamePlayer gamePlayer = players.get(player);
        gamePlayer.getTeam().removePlayer(gamePlayer);
        players.remove(player);

        if(state == GameState.WAITING){
            /*
            Bukkit.broadcastMessage(CaptureTheFlag.getPrefix()+"§f"+player.getName()+" §cà quitté la partie. §7(§f"+GameManager.getPlayers().size()+"§8/§f"+GameManager.MAX_PLAYERS+"§7).");
             */
        }
    }

    /**
     * Allows you to teleport a player to the point of appearance of the lobby
     *
     * @param player
     */
    public static void teleportToLobby(Player player){
        Location location = ConfigurationUtils.getLocation(CaptureTheFlag.getConfiguration(), "locations.lobby");
        player.teleport(location);
        Logger.debug(player.getName()+" teleported to lobby ! ");
    }

    /**
     * Starting the game (can be forced)
     *
     * @param force
     */
    public static void start(boolean force){
        if(force){
            Logger.debug("The game is started by force.");
        }

        randomTeam();
    }

    /**
     * This method allows to apply a random team to the players
     */
    public static void randomTeam(){
        for(GamePlayer player : getPlayers()){
            Team.logicTeam(player);
        }
    }

    public static GameState getState() {
        return state;
    }

    public static boolean isState(GameState state){
        return getState() == state;
    }

    public static GamePlayer getGamePlayer(Player player){
        return players.get(player);
    }

    public static Collection<GamePlayer> getPlayers() {
        return players.values();
    }

    public static void init(){
        new ScoreboardRunnable().start();

        Arrays.asList(Team.values()).forEach(team -> team.clearPlayers());
    }
}
