package doryanbessiere.capturetheflag.minecraft.command.commands;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.commons.command.SimpleCommand;
import doryanbessiere.capturetheflag.minecraft.listener.listeners.LoggerListener;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugCommand extends SimpleCommand {

    public DebugCommand() {
        super("ctfdebug", "Vous permet de suivre les logs du plugin.");
    }

    @Override
    public boolean onExecute(CommandSender sender, String[] arguments) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(!LoggerListener.contains(player)){
                LoggerListener.addListener(player);
                CaptureTheFlag.sendMessage(player, "§aActivation de l'écoute du journal.");
            } else {
                LoggerListener.removeListener(player);
                CaptureTheFlag.sendMessage(player, "§cDésactivation de l'écoute du journal.");
            }
        } else {
            CaptureTheFlag.sendMessage(sender, "§cVous devez être un joueur pour éxécuter sa commande.");
        }
        return false;
    }

    @Override
    public void onHelp(CommandSender sender) {
        CaptureTheFlag.sendMessage(sender, "§bCette commande permet d'écouter ou de ne plus écouter le journal du plugin.");
    }
}
