package doryanbessiere.capturetheflag.minecraft.flag;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.commons.Commons;
import doryanbessiere.capturetheflag.minecraft.commons.items.ItemBuilder;
import doryanbessiere.capturetheflag.minecraft.commons.logger.Logger;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.player.GamePlayer;
import doryanbessiere.capturetheflag.minecraft.team.Team;
import org.bukkit.*;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class Flag {

    private Team team;
    private GamePlayer carrier;
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

    public GamePlayer getCarrier() {
        return carrier;
    }

    public Location getLocation() {
        return location;
    }

    public void stolen(GamePlayer gamePlayer) {
        if(task != null){
            task.cancel();
            task = null;
        }

        this.carrier = gamePlayer;
        this.carrier.saveHelmet();
        this.carrier.setFlag(this);

        if(!atBase()){
            this.location.getBlock().setType(Material.AIR);
        }

        this.location = gamePlayer.getPlayer().getLocation();

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

        Logger.debug(team.getName()+" team flag has just been stolen by the player "+player.getName());
        CaptureTheFlag.broadcast("§c"+gamePlayer.getTeam().getNameColor()+gamePlayer.getName()+" §7a volé le drapeau "+team.getNameColor()+team.getName()+"§7!");
        GameManager.getMap().getWorld().strikeLightningEffect(location);
    }

    public void init(){
        Block bannerBlock = location.getBlock();
        Banner bannerState = (Banner) bannerBlock.getState();
        bannerState.setBaseColor(team.getDyeColor());
        bannerState.update();
    }

    private BukkitTask task;

    public void dropInVoid(){
        this.location = null;
        backToBase();
    }

    public void drop(){
        GamePlayer oldCarrier = this.carrier;

        this.carrier.setFlag(null);
        this.carrier = null;

        Location ground = oldCarrier.getPlayer().getLocation();
        while(true){
            ground.add(0, -1, 0);

            if(ground.getBlock().getType() != Material.AIR && !ground.getBlock().isLiquid())
                break;

            if(ground.getBlockY() < 0)
            {
                ground = null;
                break;
            }
        }

        if(ground == null){
            dropInVoid();
            return;
        }

        this.location = ground.add(0, 1, 0);

        Block bannerBlock = location.getBlock();
        bannerBlock.setType(Material.STANDING_BANNER);
        Banner bannerState = (Banner) bannerBlock.getState();
        bannerState.setBaseColor(team.getDyeColor());
        bannerState.update();

        int seconds= 15;
        task = Bukkit.getScheduler().runTaskLater(CaptureTheFlag.getInstance(), new Runnable() {
            @Override
            public void run() {
                backToBase();
            }
        }, 20 * seconds);

        Logger.debug("the "+team.getName()+" team flag is falling to the ground");
        GameManager.getMap().getWorld().strikeLightningEffect(location);
        CaptureTheFlag.broadcast("§dLe drapeau "+team.getNameColor()+team.getName()+" §dvas être reset dans §a"+seconds+" secondes§d!");
    }

    public void capture(GamePlayer carrier) {
        Block bannerBlock = base.getBlock();
        Banner bannerState = (Banner) bannerBlock.getState();
        bannerState.setBaseColor(team.getDyeColor());
        bannerState.update();

        this.carrier.getPlayer().getEquipment().setHelmet(new ItemStack(Material.AIR));
        this.carrier.setFlag(null);
        this.carrier.getTeam().capture();
        this.carrier.recoverHelmet();
        this.carrier = null;

        GameManager.getMap().getWorld().strikeLightningEffect(carrier.getTeam().getFlag().getLocation());

        location = base;
        CaptureTheFlag.broadcast("§c"+carrier.getTeam().getNameColor()+carrier.getName()+" §7a déposé le drapeau "+team.getNameColor()+team.getName()+"§7 dans son camp!");
    }

    public void returnToBase(GamePlayer carrier) {
        if(task != null){
            task.cancel();
            task = null;
        }

        Block bannerBlock = base.getBlock();
        Banner bannerState = (Banner) bannerBlock.getState();
        bannerState.setBaseColor(team.getDyeColor());
        bannerState.update();

        if(location != null)
            location.getBlock().setType(Material.AIR);

        location = base;
        CaptureTheFlag.broadcast("§c"+carrier.getTeam().getNameColor()+carrier.getName()+" §7a ramené le drapeau "+team.getNameColor()+team.getName()+"§7 dans son camp!");
    }

    public void backToBase(){
        Block bannerBlock = base.getBlock();
        Banner bannerState = (Banner) bannerBlock.getState();
        bannerState.setBaseColor(team.getDyeColor());
        bannerState.update();

        if(location != null)
            location.getBlock().setType(Material.AIR);

        location = base;
        CaptureTheFlag.broadcast("§dLe drapeau "+team.getNameColor()+team.getName()+" §da été reset.");
    }

    public boolean hasCarrier(){
        return carrier != null;
    }

    public boolean atBase(){
        return Commons.compareLocation(location, base);
    }

    public boolean compareLocation(Location location) {
        return Commons.compareLocation(location, this.location);
    }

    public boolean compareBase(Location location) {
        return Commons.compareLocation(location, this.base);
    }

    public static void interact(GamePlayer gamePlayer, Location to){
        for(Team team : Team.values()) {
            Flag flag = team.getFlag();

            if (Commons.compareLocation(flag.getLocation(), to)) {
                if (flag.getCarrier() != null) {
                    Logger.debug("stolen() or returnToBase() : if(flag.getCarrier() != null); " + (flag.getCarrier() != null));
                    return;
                }
                if (!flag.atBase() && flag.getTeam() == gamePlayer.getTeam()) {
                    flag.returnToBase(gamePlayer);
                } else if (gamePlayer.getFlag() == null && flag.getTeam() != gamePlayer.getTeam()) {
                    flag.stolen(gamePlayer);
                }
            }
        }
        if(gamePlayer.getFlag() != null){
            Flag flag = gamePlayer.getTeam().getFlag();
            if(gamePlayer.getFlag().getCarrier() == null){
                Logger.debug("capture() : if(gamePlayer.getFlag().getCarrier() == null); "+(gamePlayer.getFlag().getCarrier() == null));
                return;
            }

            if(flag.compareBase(to)){
                if(!gamePlayer.getTeam().getFlag().atBase()) {
                    gamePlayer.sendMessage("§cVous devez avoir votre drapeau !");
                    return;
                }
                gamePlayer.getFlag().capture(gamePlayer);
            }
        }
    }

    @Override
    public String toString() {
        return "Flag{" +
                "team=" + team +
                ", carrier=" + carrier +
                ", base=" + base +
                '}';
    }
}
