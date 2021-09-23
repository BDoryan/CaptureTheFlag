package doryanbessiere.capturetheflag.minecraft.game;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.commons.config.ConfigurationUtils;
import doryanbessiere.capturetheflag.minecraft.commons.items.ItemBuilder;
import doryanbessiere.capturetheflag.minecraft.commons.logger.Logger;
import doryanbessiere.capturetheflag.minecraft.map.Map;
import doryanbessiere.capturetheflag.minecraft.map.MapManager;
import doryanbessiere.capturetheflag.minecraft.player.GamePlayer;
import doryanbessiere.capturetheflag.minecraft.schedulers.GameFinishRunnable;
import doryanbessiere.capturetheflag.minecraft.schedulers.GameRunnable;
import doryanbessiere.capturetheflag.minecraft.schedulers.GameStartingRunnable;
import doryanbessiere.capturetheflag.minecraft.schedulers.ScoreboardRunnable;
import doryanbessiere.capturetheflag.minecraft.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GameManager {

    public static int MIN_PLAYERS = 6;
    public static int MAX_PLAYERS = 40;

    private static HashMap<String, GamePlayer> players = new HashMap<>();
    private static GameState state = GameState.WAITING;
    private static Map map = null;
    private static GameRunnable gameRunnable;

    public static final ItemStack WAND = new ItemBuilder(Material.STICK).setName("§6Outil de zone §7(clic-droit ou clic-gauche)").toItemStack();

    private static HashMap<Player, Location> position1 = new HashMap<>();
    private static HashMap<Player, Location> position2 = new HashMap<>();

    public static HashMap<Player, Location> getPosition1() {
        return position1;
    }

    public static HashMap<Player, Location> getPosition2() {
        return position2;
    }

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
            lobby(gamePlayer);
        }
    }

    public static void lobby(GamePlayer gamePlayer){
        Player player = gamePlayer.getPlayer();
        player.setGameMode(GameMode.ADVENTURE);
        gamePlayer.clean();
        teleportToLobby(player);
    }

    /**
     * This method is executed when a player leaves the server (the game)
     *
     * @param player
     */
    public static void quit(Player player){
        GamePlayer gamePlayer = getGamePlayer(player);
        gamePlayer.leaveTeam();
        players.remove(gamePlayer.getUUIDString());

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

    public static void finish(Team team) {
        Bukkit.getWorlds().get(0).setTime(15000);
        getPlayers().forEach(gamePlayer -> lobby(gamePlayer));
        new GameFinishRunnable(team).start();
    }

    public static GameStartingRunnable startingRunnable;

    /**
     * Starting the game (can be forced)
     *
     * @param force
     */
    public static void start(boolean force){
        map = MapManager.randomMap();
        map.init();
        map.getWorld().setTime(new Random().nextInt(8000));

        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                randomTeam();

                for(Team team : Team.values()){
                    team.getPlayers().forEach(player -> player.getPlayer().teleport(map.getSpawns().get(team)));
                }
                state = GameState.INGAME;
                getPlayers().forEach(gamePlayer -> {
                    Player player = gamePlayer.getPlayer();
                    player.setGameMode(GameMode.SURVIVAL);
                });
                gameRunnable = new GameRunnable();
                gameRunnable.start();
            }
        };

        if(force){
            Logger.debug("The game is started by force.");
            runnable.run();
        } else {
            startingRunnable = new GameStartingRunnable(runnable);
            startingRunnable.start();
        }
    }

    /**
     * This method allows to apply a random team to the players
     */
    public static void randomTeam(){
        for(GamePlayer player : getPlayers()){
            Team.logicTeam(player);
        }
    }

    public static Map getMap() {
        return map;
    }

    public static GameState getState() {
        return state;
    }

    public static boolean isState(GameState state){
        return getState() == state;
    }

    public static GamePlayer getGamePlayer(Player player){
        return players.get(player.getUniqueId().toString());
    }

    public static Collection<GamePlayer> getPlayers() {
        return players.values();
    }

    public static void init(){
        new ScoreboardRunnable().start();
        Bukkit.getWorlds().get(0).setTime(8000);

        Arrays.asList(Team.values()).forEach(team -> team.clearPlayers());
    }

    public static GameRunnable getGameRunnable() {
        return gameRunnable;
    }
}
