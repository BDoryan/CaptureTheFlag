package doryanbessiere.capturetheflag.minecraft.schedulers;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.commons.logger.Logger;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import org.bukkit.Bukkit;

public class PlayerRunnable implements Runnable {

    public void start(){
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(CaptureTheFlag.getInstance(), this,0, 5);
        Logger.debug("PlayerRunnable : start.");
    }

    @Override
    public void run() {
        GameManager.getPlayers().forEach(gamePlayer -> gamePlayer.update());
    }
}
