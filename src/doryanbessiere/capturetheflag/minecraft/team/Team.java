package doryanbessiere.capturetheflag.minecraft.team;

import doryanbessiere.capturetheflag.minecraft.player.GamePlayer;

import java.util.ArrayList;

public enum Team {

    RED("§c", "§7[§cRouge§7]"),
    BLUE("§9", "§7[§bBlue§7]");

    private String nameColor;
    private String prefix;
    private ArrayList<GamePlayer> players = new ArrayList<>();

    Team(String nameColor, String prefix) {
        this.nameColor = nameColor;
        this.prefix = prefix;
    }

    public ArrayList<GamePlayer> getPlayers() {
        return players;
    }

    public void addPlayer(GamePlayer player){
        players.add(player);
        player.setTeam(this);
    }

    public boolean containsPlayer(GamePlayer player){
        return players.contains(player);
    }

    public void removePlayer(GamePlayer player){
        players.remove(player);
        player.setTeam(null);
    }

    public String getPrefix() {
        return prefix;
    }

    public String getNameColor() {
        return nameColor;
    }
}
