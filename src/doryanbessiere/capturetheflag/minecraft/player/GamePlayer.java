package doryanbessiere.capturetheflag.minecraft.player;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.player.scoreboard.PlayerScoreboard;
import doryanbessiere.capturetheflag.minecraft.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.UUID;

public class GamePlayer {

    private Player player;
    private Team team;
    private PlayerScoreboard scoreboard;
    private String name;

    public GamePlayer(Player player) {
        this.player = player;
        this.name = player.getName();
        this.scoreboard = new PlayerScoreboard(this);
        this.scoreboard.create();

        setTeam(null);
    }

    /**
     * Allows to define the player's team (null = no team), but also to apply the properties (player's name with team colors)
     *
     * @param team if the value is null the player is without team
     */
    public void setTeam(Team team) {
        this.team = team;

        String customName = "§7"+getName();
        if(team != null){
            customName = team.getNameColor()+getName();
        }
        player.setPlayerListName(customName);
        //setDisplayName(customName);
    }

    public void sendMessage(String message){
        CaptureTheFlag.sendMessage(player, message);
    }

    public String getName() {
        return name;
    }

    public Player getPlayer() {
        return player;
    }

    public void clearAllInventory() {
        player.getInventory().clear();
        player.getEquipment().setHelmet(null);
        player.getEquipment().setChestplate(null);
        player.getEquipment().setLeggings(null);
        player.getEquipment().setBoots(null);
    }

    public Team getTeam() {
        return team;
    }

    public void heal() {
        player.setHealth(20);
    }

    public void feed() {
        player.setFoodLevel(20);
    }

    public void clearXp() {
        player.setExp(0);
    }

    public void clearLevel() {
        player.setLevel(0);
    }

    public void clean(){
        heal();
        feed();
        clearXp();
        clearLevel();
        clearAllInventory();
    }

    public void updateScoreboard() {
        this.scoreboard.update();
    }

    public String getUUIDString() {
        return getUUID().toString();
    }

    public UUID getUUID() {
        return player.getUniqueId();
    }

    public void leaveTeam() {
        if(team!= null)
            team.removePlayer(this);
    }
}
