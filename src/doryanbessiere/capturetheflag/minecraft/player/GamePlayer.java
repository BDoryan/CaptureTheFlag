package doryanbessiere.capturetheflag.minecraft.player;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.team.Team;
import org.bukkit.entity.Player;

public class GamePlayer {

    private Player player;
    private Team team;

    public GamePlayer(Player player) {
        this.player = player;
    }

    public void join(){

    }

    public void quit(){

    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void sendMessage(String message){
        CaptureTheFlag.sendMessage(player, message);
    }

    public Player getPlayer() {
        return player;
    }
}
