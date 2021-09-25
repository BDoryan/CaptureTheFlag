package doryanbessiere.capturetheflag.minecraft.listener.listeners;

import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.game.GameState;
import doryanbessiere.capturetheflag.minecraft.player.GamePlayer;
import doryanbessiere.capturetheflag.minecraft.team.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {

    @EventHandler
    public void AsyncPlayerChatEvent_(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        GamePlayer gamePlayer = GameManager.getGamePlayer(player);

        if(GameManager.isState(GameState.INGAME)){
            Team team = gamePlayer.getTeam();
            event.setFormat(team.getNameColor()+"» " +player.getName()+"§7: §f"+ event.getMessage());
        } else {
            event.setFormat("§7"+player.getName()+": §f"+event.getMessage());
        }
    }
}
