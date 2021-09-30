package doryanbessiere.capturetheflag.minecraft.schedulers;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.commons.config.ConfigurationUtils;
import doryanbessiere.capturetheflag.minecraft.commons.logger.Logger;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

public class FireworksRunnable implements Runnable {

    private int task;
    private Team team;

    public FireworksRunnable(Team team) {
        this.team = team;
    }

    public void start(){
        this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(CaptureTheFlag.getInstance(), this,0, 30);
        Logger.debug("FireworksRunnable : start.");
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(task);
    }

    @Override
    public void run() {
        for (Location location : GameManager.getMap().getSpawns().values()) {
            location = location.add(0, 10, 0);
            Firework firework = location.getWorld().spawn(location, Firework.class);
            FireworkMeta fm = firework.getFireworkMeta();
            Random random = new Random();
            fm.addEffect(FireworkEffect.builder().flicker(false).trail(false).with(FireworkEffect.Type.values()[random.nextInt(FireworkEffect.Type.values().length - 1)])
                    .withColor(team == null ? Color.GRAY : team.getColor(), Color.WHITE).build());
            fm.setPower(0);
            firework.setFireworkMeta(fm);

            Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureTheFlag.getInstance(), new Runnable() {
                @Override
                public void run() {
                    firework.detonate();
                }
            }, (long) new Random().nextInt(60));
        }
    }
}
