package doryanbessiere.capturetheflag.minecraft.flag;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.commons.items.ItemBuilder;
import doryanbessiere.capturetheflag.minecraft.commons.logger.Logger;
import doryanbessiere.capturetheflag.minecraft.player.GamePlayer;
import doryanbessiere.capturetheflag.minecraft.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.scheduler.BukkitTask;

public class Flag {

    private Team team;
    private GamePlayer thief;
    private Location base;
    private Location location;

    public Flag(Team team, Location base) {
        this.team = team;
        this.base = base;
        this.location = base;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getBase() {
        return base;
    }

    public Team getTeam() {
        return team;
    }

    public GamePlayer getThief() {
        return thief;
    }

    public Location getLocation() {
        return location;
    }

    public void stolen(GamePlayer gamePlayer) {
        if(task != null){
            task.cancel();
        }

        this.thief = gamePlayer;
        this.thief.setFlag(this);

        Player player = gamePlayer.getPlayer();
        ItemStack banner_item = new ItemBuilder(Material.BANNER).setName(team.getNameColor()+"Drapeau de l'équipe "+team.getName()).toItemStack();
        BannerMeta banner = (BannerMeta) banner_item.getItemMeta();
        banner.setBaseColor(team.getDyeColor());
        banner_item.setItemMeta(banner);
        player.getEquipment().setHelmet(banner_item);

        Block bannerBlock = base.getBlock();
        Banner bannerState = (Banner) bannerBlock.getState();
        bannerState.setBaseColor(DyeColor.WHITE);
        bannerState.update();

        Logger.debug(team.getName()+" team flag has just been stolen by the player "+thief.getName());
    }

    private BukkitTask task;

    public void drop(){
        this.thief.setFlag(null);
        this.thief = null;

        task = Bukkit.getScheduler().runTaskLater(CaptureTheFlag.getInstance(), new Runnable() {
            @Override
            public void run() {
                backToBase();
            }
        }, 20 * 15);

        Logger.debug("the "+team.getName()+" team flag is falling to the ground");
    }

    public void backToBase(){
        Block bannerBlock = base.getBlock();
        Banner bannerState = (Banner) bannerBlock.getState();
        bannerState.setBaseColor(team.getDyeColor());
        bannerState.update();

        location.getBlock().setType(Material.AIR);
        location = base;
    }

    public boolean hasThief(){
        return thief != null;
    }

    public boolean atBase(){
        return location.getBlockX() == base.getBlockX() &&
                location.getBlockY() == base.getBlockY() &&
                location.getBlockZ() == base.getBlockZ();
    }
}
