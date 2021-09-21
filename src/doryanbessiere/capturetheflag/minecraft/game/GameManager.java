package doryanbessiere.capturetheflag.minecraft.game;

import doryanbessiere.capturetheflag.minecraft.player.GamePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class GameManager {

    public static HashMap<Player, GamePlayer> players = new HashMap<>();

    public static void join(Player player){
        GamePlayer gamePlayer = new GamePlayer(player);
        gamePlayer.join();
        players.put(player, gamePlayer);
    }

    public static void remove(Player player){
        GamePlayer gamePlayer = players.get(player);
        gamePlayer.quit();
        players.remove(player);
    }

    public static GamePlayer getGamePlayer(Player player){
        return players.get(player);
    }

    public static Collection<GamePlayer> getPlayers() {
        return players.values();
    }

    public static void init(){

    }
}
