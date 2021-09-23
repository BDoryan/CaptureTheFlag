package doryanbessiere.capturetheflag.minecraft.schedulers;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.commons.logger.Logger;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class GameStartingRunnable implements Runnable {

    private int task;
    private int seconds;
    private Runnable runnable;

    public GameStartingRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public void start(){
        this.seconds = 30;

        this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(CaptureTheFlag.getInstance(), this,0, 20);
        Logger.debug("GameStartingRunnable : start.");
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(task);
    }

    @Override
    public void run() {
        if(seconds==0) {
            cancel();
            runnable.run();
            return;
        }

        GameManager.getPlayers().forEach(player -> player.getPlayer().setLevel(seconds));
        seconds--;
    }
}
