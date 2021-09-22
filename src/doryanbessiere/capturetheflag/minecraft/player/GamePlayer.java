package doryanbessiere.capturetheflag.minecraft.player;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.commons.logger.Logger;
import doryanbessiere.capturetheflag.minecraft.player.scoreboard.PlayerScoreboard;
import doryanbessiere.capturetheflag.minecraft.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Random;
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
        setDisplayName(customName);
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

    /**
     * WARNING: this method can trigger problems, indeed depending on the version of spigot you use an error
     *
     * @param name
     */
    public void setDisplayName(String name){
        player.setCustomName(player.getName());
        player.setPlayerListName(name);

        Logger.debug(player.getName()+".setDisplayName("+name.replace("§", "&")+")");

        try {
            Method getHandle = player.getClass().getMethod("getHandle");
            Object entityPlayer = getHandle.invoke(player);
            boolean gameProfileExists = false;
            try {
                Class.forName("net.minecraft.util.com.mojang.authlib.GameProfile");
                gameProfileExists = true;
            } catch (ClassNotFoundException ignored) {
            }
            try {
                Class.forName("com.mojang.authlib.GameProfile");
                gameProfileExists = true;
            } catch (ClassNotFoundException ignored) {
            }
            if (!gameProfileExists) {
                Field nameField = entityPlayer.getClass().getSuperclass().getDeclaredField("name");
                nameField.setAccessible(true);
                nameField.set(entityPlayer, name);
                Logger.debug("setDisplayName(): nameField.set("+name+")");
            } else {
                Object profile = entityPlayer.getClass().getMethod("getProfile").invoke(entityPlayer);
                Field ff = profile.getClass().getDeclaredField("name");
                ff.setAccessible(true);
                ff.set(profile, name);
                Logger.debug("setDisplayName(): ff.set("+name+")");
            }
            if (Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0]).getReturnType() == Collection.class) {
                @SuppressWarnings("unchecked")
                Collection<? extends Player> players = (Collection<? extends Player>) Bukkit.class.getMethod("getOnlinePlayers").invoke(null);
                for (Player p : players) {
                    p.hidePlayer(player);
                    p.showPlayer(player);
                }
            } else {
                Player[] players = ((Player[]) Bukkit.class.getMethod("getOnlinePlayers").invoke(null));
                for (Player p : players) {
                    p.hidePlayer(player);
                    p.showPlayer(player);
                }
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException | InvocationTargetException e) {
            e.printStackTrace();
        }
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
}
