package doryanbessiere.capturetheflag.minecraft.schedulers;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.commons.logger.Logger;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.player.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class ScoreboardRunnable implements Runnable {

    public void start(){
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(CaptureTheFlag.getInstance(), this,0, 5);
        Logger.debug("ScoreboardRunnable : start.");
    }

    @Override
    public void run() {
        GameManager.getPlayers().forEach(gamePlayer -> gamePlayer.updateScoreboard());
        Logger.debug("ScoreboardRunnable : run();");
    }
}
