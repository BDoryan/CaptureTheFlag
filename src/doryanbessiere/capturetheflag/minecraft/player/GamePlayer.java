package doryanbessiere.capturetheflag.minecraft.player;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.commons.cuboid.Cuboid;
import doryanbessiere.capturetheflag.minecraft.commons.items.ItemBuilder;
import doryanbessiere.capturetheflag.minecraft.commons.logger.Logger;
import doryanbessiere.capturetheflag.minecraft.compass.Compass;
import doryanbessiere.capturetheflag.minecraft.flag.Flag;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.player.scoreboard.PlayerScoreboard;
import doryanbessiere.capturetheflag.minecraft.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class GamePlayer {

    private Player player;
    private Team team;
    private PlayerScoreboard scoreboard;
    private String name;
    private Compass compass;
    private Flag flag;

    private ItemStack old_helmet;
    private boolean canProjection = true;
    private boolean projected = false;

    private ArrayList<Cuboid> areas = new ArrayList<>();

    public GamePlayer(Player player) {
        this.player = player;
        this.name = player.getName();

        this.compass = new Compass(this);

        this.scoreboard = new PlayerScoreboard(this);
        this.scoreboard.create();

        setTeam(null);
    }

    public void setProjected(boolean projected) {
        this.projected = projected;
    }

    public boolean isProjected() {
        return projected;
    }

    public void enter(Cuboid area){
        if(inArea(area))return;
        areas.add(area);

        if(area == GameManager.getMap().getAreas().get(getTeam() == Team.RED ? Team.BLUE : Team.RED)){
            Logger.debug(player.getLocation().getDirection().getY()+"");
            if(player.getLocation().getDirection().getY() < -0.5 || player.getLocation().getDirection().getY() > 0.5){
                player.getLocation().getDirection().setY(0.50);
                Logger.debug("setDirection");
            }
            player.setVelocity(player.getLocation().getDirection().multiply(-2).setY(0.5));
        }
    }

    public void exit(Cuboid area){
        if(!inArea(area))return;
        areas.remove(area);
    }

    public boolean inArea(Cuboid area){
        return areas.contains(area);
    }

    public ArrayList<Cuboid> getAreas() {
        return areas;
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

    public ArrayList<Cuboid> getCopyAreas() {
        ArrayList<Cuboid> areas = new ArrayList<>();
        areas.addAll(this.areas);
        return areas;
    }

    public Flag getFlag() {
        return flag;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    public void death() {
        if(flag != null)
            flag.drop();

        clean();

        respawn();
        deaths++;
    }

    public void spawn(){
        clean();
        player.setGameMode(GameMode.SURVIVAL);
        player.getEquipment().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).toItemStack());
        player.getEquipment().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).toItemStack());
        player.getEquipment().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).toItemStack());
        player.getEquipment().setBoots(new ItemBuilder(Material.LEATHER_BOOTS).toItemStack());

        player.getInventory().setItem(0, new ItemBuilder(Material.IRON_SWORD).toItemStack());
        player.teleport(GameManager.getMap().getSpawns().get(getTeam()));
        this.compass.giveCompass();
    }

    public void saveHelmet(){
        old_helmet = player.getEquipment().getHelmet();
    }

    public void recoverHelmet(){
        if(old_helmet != null) {
            player.getEquipment().setHelmet(old_helmet);
            old_helmet = null;
        }
    }

    public Compass getCompass() {
        return compass;
    }

    public void update(){
        compass.update();
    }

    private boolean hasRespawn = true;
    private int respawnLeft = 5;
    private int respawnTask;

    public void respawn(){
        player.teleport(GameManager.getMap().getSpawns().get(team));
        hasRespawn = false;
        player.setGameMode(GameMode.SPECTATOR);

        respawnTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(CaptureTheFlag.getInstance(), new Runnable() {
            @Override
            public void run() {
                if(respawnLeft == 0){
                    respawnLeft= 5;
                    hasRespawn = true;
                    spawn();
                    Bukkit.getScheduler().cancelTask(respawnTask);
                    player.sendTitle("", "");
                    return;
                }
                player.sendTitle("§aRéapparition dans §f"+respawnLeft+" secondes§a.", "");
                respawnLeft--;
            }
        }, 0, 20);
    }

    @Override
    public String toString() {
        return "GamePlayer{" +
                "team=" + team +
                ", scoreboard=" + scoreboard +
                ", name='" + name + '\'' +
                ", flag=" + flag +
                ", areas=" + areas +
                ", hasRespawn=" + hasRespawn +
                ", respawnLeft=" + respawnLeft +
                ", respawnTask=" + respawnTask +
                ", kills=" + kills +
                ", deaths=" + deaths +
                '}';
    }

    public boolean hasRespawn() {
        return hasRespawn;
    }

    private int kills=0;
    private int deaths=0;

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void kill() {
        kills++;
    }

    public boolean canProjection() {
        return canProjection;
    }

    public void setCanProjection(boolean canProjection) {
        this.canProjection = canProjection;
    }
}
