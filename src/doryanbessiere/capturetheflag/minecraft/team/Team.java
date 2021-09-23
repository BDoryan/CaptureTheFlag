package doryanbessiere.capturetheflag.minecraft.team;

import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.player.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;
import java.util.Locale;

public enum Team {

    RED("§c", "§7[§cRouge§7]"),
    BLUE("§9",  "§7[§bBlue§7]");

    private String nameColor;
    private String prefix;

    private org.bukkit.scoreboard.Team team;

    private ArrayList<GamePlayer> players = new ArrayList<>();

    Team(String nameColor, String prefix) {
        this.nameColor = nameColor;
        this.prefix = prefix;
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

    public String getPrefix() {
        return prefix;
    }

    public String getNameColor() {
        return nameColor;
    }
}
