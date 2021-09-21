package doryanbessiere.capturetheflag.minecraft.command.commands;

import doryanbessiere.capturetheflag.minecraft.commons.command.SimpleCommand;
import org.bukkit.command.CommandSender;

public class TeamChatCommand extends SimpleCommand {

    public TeamChatCommand() {
        super("tc", "Vous permet de suivre les logs du plugin.");
    }

    @Override
    public boolean onExecute(CommandSender sender, String[] arguments) {
        return false;
    }

    @Override
    public void onHelp(CommandSender sender) {

    }
}
