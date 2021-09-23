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

public class GameFinishRunnable implements Runnable {

    private int task;
    private Team team;

    public GameFinishRunnable(Team team) {
        this.team = team;
    }

    public void start(){
        this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(CaptureTheFlag.getInstance(), this,0, 20);
        Logger.debug("GameFinishRunnable : start.");
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(task);
    }

    @Override
    public void run() {
        FileConfiguration configuration = CaptureTheFlag.getConfiguration();
        for (String locationPath : configuration.getConfigurationSection("fireworks").getKeys(false)) {
            Location location = ConfigurationUtils.getLocation(configuration, "fireworks."+locationPath);
            Firework firework = location.getWorld().spawn(location, Firework.class);
            FireworkMeta fm = firework.getFireworkMeta();
            Random random = new Random();
            fm.addEffect(FireworkEffect.builder().flicker(false).trail(false).with(FireworkEffect.Type.values()[random.nextInt(FireworkEffect.Type.values().length - 1)])
                    .withColor(team == null ? Color.GRAY : team.getColor(), Color.YELLOW, Color.WHITE).build());
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
