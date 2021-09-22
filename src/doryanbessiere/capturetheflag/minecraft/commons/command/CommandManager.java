package doryanbessiere.capturetheflag.minecraft.commons.command;

import doryanbessiere.capturetheflag.minecraft.commons.Commons;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public abstract class CommandManager {

    private ArrayList<SimpleCommand> commands = new ArrayList<>();
    private JavaPlugin plugin;

    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void defaultCommand(){
        setCommand(new SimpleCommand(plugin.getName()) {
            @Override
            public boolean onExecute(CommandSender sender, String[] arguments) {
                onHelp(sender);
                return true;
            }

            @Override
            public void onHelp(CommandSender sender) {
                sender.sendMessage(Commons.lineSeparator(plugin.getName()));
                sender.sendMessage("");
                sender.sendMessage("§5Auteur: §fDoryan BESSIERE");
                sender.sendMessage("§5Discord: §fDoryan#7216");
                sender.sendMessage("");
                sender.sendMessage("§5Liste des commandes:");
                for(SimpleCommand command : commands){
                    sender.sendMessage(" §f- /"+command.getCommand());
                    if(command.getDescription() != null)
                        sender.sendMessage("   §d"+command.getDescription());
                    sender.sendMessage("");
                }
                sender.sendMessage("");
                sender.sendMessage("§5Contact: ");
                sender.sendMessage("  §f- doryanbessiere.pro@gmail.com");
                sender.sendMessage("  §f- contact@doryanbessiere.fr");
                sender.sendMessage("");
            }
        });
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public ArrayList<SimpleCommand> getCommands() {
        return commands;
    }

    public void setCommand(SimpleCommand command){
        plugin.getCommand(command.getCommand()).setExecutor(command);
        commands.add(command);
    }

    public abstract void loadCommands();

}
