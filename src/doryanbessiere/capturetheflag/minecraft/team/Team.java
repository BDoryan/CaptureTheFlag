package doryanbessiere.capturetheflag.minecraft.team;

import doryanbessiere.capturetheflag.minecraft.flag.Flag;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.player.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;
import java.util.Locale;

public enum Team {

    RED("§c", DyeColor.RED, Color.RED,"Rouge", "§7[§cRouge§7]", null),
    BLUE("§9",  DyeColor.BLUE, Color.BLUE,"Bleu","§7[§bBlue§7]", null);

    public static final int MAX_CAPTURE = 3;

    private String nameColor;
    private DyeColor dyeColor;
    private Color color;
    private String name;
    private String prefix;
    private Flag flag;
    private int capture = 0;

    private org.bukkit.scoreboard.Team team;

    private ArrayList<GamePlayer> players = new ArrayList<>();

    Team(String nameColor, DyeColor dyeColor, Color color, String name, String prefix, Flag flag) {
        this.nameColor = nameColor;
        this.dyeColor = dyeColor;
        this.color = color;
        this.name = name;
        this.prefix = prefix;
        this.flag = flag;
    }

    public Color getColor() {
        return color;
    }

    public void capture(){
        capture++;
        if(capture == MAX_CAPTURE){
            GameManager.finish(this);
        }
    }

    public int getCapture() {
        return capture;
    }

    public org.bukkit.scoreboard.Team getScoreboardTeam(GamePlayer player){
        String teamName = toString().toLowerCase();

        Scoreboard scoreboard = player.getPlayer().getScoreboard();
        org.bukkit.scoreboard.Team scoreboardTeam = scoreboard.getTeam(teamName);

        if (scoreboardTeam == null) {
            scoreboard.registerNewTeam(teamName);
            scoreboardTeam = scoreboard.getTeam(teamName);
            scoreboardTeam.setNameTagVisibility(NameTagVisibility.ALWAYS);
            scoreboardTeam.setPrefix(getNameColor());
        }
        return scoreboardTeam;
    }

    public void clearPlayers(){
    }

    /**
     * Puts the player in a team that is the least filled
     *
     * @param player
     */
    public static void logicTeam(GamePlayer player) {
        if(Team.RED.getPlayers().size() < Team.BLUE.getPlayers().size()){
            Team.RED.addPlayer(player);
        } else {
            Team.BLUE.addPlayer(player);
        }
    }

    public ArrayList<GamePlayer> getPlayers() {
        return players;
    }

    public void addPlayer(GamePlayer player){
        players.add(player);

        for(GamePlayer p : GameManager.getPlayers()){
            org.bukkit.scoreboard.Team scoreboardTeam = getScoreboardTeam(p);
            scoreboardTeam.addPlayer(player.getPlayer());
        }
        player.setTeam(this);
    }

    public boolean containsPlayer(GamePlayer player){
        return players.contains(player);
    }

    public void removePlayer(GamePlayer player){
        players.remove(player);
        for(GamePlayer p : GameManager.getPlayers()){
            org.bukkit.scoreboard.Team scoreboardTeam = getScoreboardTeam(p);
            scoreboardTeam.removePlayer(player.getPlayer());
        }

        player.setTeam(null);
    }

    public static Team fromName(String name){
        for(Team team : values()){
            if(team.toString().equalsIgnoreCase(name))
                return team;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public DyeColor getDyeColor() {
        return dyeColor;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    public Flag getFlag() {
        return flag;
    }

    public String getNameColor() {
        return nameColor;
    }
}
