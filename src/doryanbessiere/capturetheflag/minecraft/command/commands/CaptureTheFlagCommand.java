package doryanbessiere.capturetheflag.minecraft.command.commands;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.commons.Commons;
import doryanbessiere.capturetheflag.minecraft.commons.command.SimpleCommand;
import doryanbessiere.capturetheflag.minecraft.commons.config.ConfigurationUtils;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.listener.listeners.LoggerListener;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CaptureTheFlagCommand extends SimpleCommand {

    public CaptureTheFlagCommand() {
        super("ctf");
    }

    @Override
    public boolean onExecute(CommandSender sender, String[] arguments) {
        if(arguments.length == 1){
            if(arguments[0].equalsIgnoreCase("help")) {
                onHelp(sender);
            } else if (arguments[0].equalsIgnoreCase("forcestart")){
                GameManager.start(true);
            } else if (arguments[0].equalsIgnoreCase("setlobby")){
                if(sender instanceof Player){
                    Player player = (Player) sender;
                    ConfigurationUtils.setLocation(CaptureTheFlag.getConfiguration(), player.getLocation(),"locations.lobby");
                    CaptureTheFlag.saveConfiguration();
                    CaptureTheFlag.sendMessage(player, "§aVous avez appliquer le nouveau point de d'apparition du lobby.");
                } else {
                    CaptureTheFlag.sendMessage(sender, "§cVous devez être un joueur!");
                }
            } else if (arguments[0].equalsIgnoreCase("getlobby")){
                if(sender instanceof Player){
                    Player player = (Player) sender;
                    GameManager.teleportToLobby(player);
                    CaptureTheFlag.sendMessage(player, "§aVous avez été téléporter au point d'apparition.");
                } else {
                    CaptureTheFlag.sendMessage(sender, "§cVous devez être un joueur!");
                }
            } else if (arguments[0].equalsIgnoreCase("debug")){
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
                    CaptureTheFlag.sendMessage(sender, "§cVous devez être un joueur!");
                }
            } else if(arguments[0].equalsIgnoreCase("info")){
                sender.sendMessage(Commons.lineSeparator("CaptureTheFlag"));
                sender.sendMessage("");
                sender.sendMessage("§7Auteur: §fDoryan BESSIERE");
                sender.sendMessage("§7Discord: §fDoryan#7216");
                sender.sendMessage("");
                sender.sendMessage("§7Liste des commandes:");
                for(SimpleCommand command : CaptureTheFlag.getInstance().getCommandManager().getCommands()){
                    sender.sendMessage(" §8- /"+command.getCommand());
                    if(command.getDescription() != null)
                        sender.sendMessage("   §f"+command.getDescription());
                    sender.sendMessage("");
                }
                sender.sendMessage("");
                sender.sendMessage("§7Contact: ");
                sender.sendMessage("  §f- doryanbessiere.pro@gmail.com");
                sender.sendMessage("  §f- contact@doryanbessiere.fr");
                sender.sendMessage("");
            }
        } else if(arguments.length == 0) {
            onHelp(sender);
        }
        return true;
    }

    @Override
    public void onHelp(CommandSender sender) {
        sender.sendMessage(Commons.lineSeparator("CaptureTheFlag"));
        sender.sendMessage("  §7- /ctf forcestart");
        sender.sendMessage("    §fPermet de forcer le lancement de la partie");
        sender.sendMessage(" ");
        sender.sendMessage("  §7- /ctf setlobby");
        sender.sendMessage("    §fPermet de définir le point d'apparition du lobby");
        sender.sendMessage(" ");
        sender.sendMessage("  §7- /ctf getlobby");
        sender.sendMessage("    §fPermet de vous téléporter au point d'apparition");
        sender.sendMessage(" ");
        sender.sendMessage("  §7- /ctf debug");
        sender.sendMessage("    §fPermet d'activer ou désactiver le journal de bord");
        sender.sendMessage(" ");
    }
}
