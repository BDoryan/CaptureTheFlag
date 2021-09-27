package doryanbessiere.capturetheflag.minecraft.schedulers;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.commons.logger.Logger;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.team.Team;
import org.bukkit.Bukkit;

public class GameRunnable implements Runnable {

    private int task;
    private int seconds;

    private final int TIME = 60 * 30;

    public GameRunnable() {
    }

    public void start(){
        this.seconds = TIME;

        this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(CaptureTheFlag.getInstance(), this,0, 20);
        Logger.debug("GameRunnable : start.");
    }

    public int getSeconds() {
        return seconds;
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(task);
    }

    @Override
    public void run() {
        if(seconds==0) {
            cancel();
            Team winner = Team.RED.getCapture() != Team.BLUE.getCapture() ? (Team.RED.getCapture() > Team.BLUE.getCapture() ? Team.RED : Team.BLUE) : null;
            GameManager.finish(winner);
            return;
        }

        int minute = seconds / 60;

        if(seconds == 60 * 1 || seconds == 60 * 2 || seconds == 60 * 3 || seconds == 60 * 4 || seconds == 60 * 5 || seconds == 60 * 20 || seconds == 60 * 15 || seconds == 60 * 10 || seconds == 60 * 5){
            CaptureTheFlag.broadcast("§dIl reste "+(minute)+" "+(minute == 1 ? "minute" : "minutes")+" de jeu.");
        } else if(seconds == 30 || seconds <= 10){
            CaptureTheFlag.broadcast("§dIl reste "+seconds+" "+(seconds == 1 ? "seconde" : "secondes")+" de jeu.");
        }

        seconds--;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }
}
