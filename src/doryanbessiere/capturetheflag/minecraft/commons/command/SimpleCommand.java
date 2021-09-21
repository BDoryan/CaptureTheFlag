package doryanbessiere.capturetheflag.minecraft.commons.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class SimpleCommand implements CommandExecutor {

    private String command;
    private String description;

    public SimpleCommand(String command) {
        this.command = command;
    }

    public SimpleCommand(String command, String description) {
        this.command = command;
        this.description = description;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String cmd, String[] arguments) {
        if(arguments.length == 1 && arguments[0].equalsIgnoreCase("help")){
            onHelp(commandSender);
            return true;
        }
        return onExecute(commandSender, arguments);
    }

    public abstract boolean onExecute(CommandSender sender, String[] arguments);
    public abstract void onHelp(CommandSender sender);

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }
}
