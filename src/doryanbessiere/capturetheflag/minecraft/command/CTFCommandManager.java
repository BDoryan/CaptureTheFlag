package doryanbessiere.capturetheflag.minecraft.command;

import doryanbessiere.capturetheflag.minecraft.command.commands.CaptureTheFlagCommand;
import doryanbessiere.capturetheflag.minecraft.command.commands.TeamChatCommand;
import doryanbessiere.capturetheflag.minecraft.commons.command.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CTFCommandManager extends CommandManager {

    public CTFCommandManager(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void loadCommands() {
        setCommand(new TeamChatCommand());
        setCommand(new CaptureTheFlagCommand());
    }
}
