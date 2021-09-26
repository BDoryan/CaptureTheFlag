package doryanbessiere.capturetheflag.minecraft.command.commands;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.commons.command.SimpleCommand;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.game.GameState;
import doryanbessiere.capturetheflag.minecraft.player.GamePlayer;
import doryanbessiere.capturetheflag.minecraft.team.Team;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamChatCommand extends SimpleCommand {

    public TeamChatCommand() {
        super("tc", "Vous permet de suivre les logs du plugin.");
    }

    @Override
    public boolean onExecute(CommandSender sender, String[] arguments) {
        if(!(sender instanceof Player)){
            CaptureTheFlag.sendMessage(sender, "§cVous n'êtes pas un joueur !");
            return false;
        }
        if(!GameManager.isState(GameState.INGAME)){
            CaptureTheFlag.sendMessage(sender, "§cLa partie n'a pas démarré !");
            return false;
        }
        if(arguments.length < 1){
            CaptureTheFlag.sendMessage(sender, "§cErreur, essayez : §f/tc §7<message>");
            return false;
        }

        Player player = (Player) sender;
        GamePlayer gamePlayer = GameManager.getGamePlayer(player);
        String message = "";
        for(int i = 0; i < arguments.length; i++){
            String argument = arguments[i];
            if(i == 0)
                message += argument;
            else if(i > 0)
                message += " "+argument;
        }

        Team team = gamePlayer.getTeam();
        String finalMessage = message;
        team.getPlayers().forEach((gameMember -> {
            gameMember.getPlayer().sendMessage("§7[TEAM] "+team.getNameColor()+">> §7" +player.getName()+": §f"+ finalMessage);
        }));
        return false;
    }

    @Override
    public void onHelp(CommandSender sender) {

    }
}
