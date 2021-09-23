package doryanbessiere.capturetheflag.minecraft.command.commands;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.commons.Commons;
import doryanbessiere.capturetheflag.minecraft.commons.command.SimpleCommand;
import doryanbessiere.capturetheflag.minecraft.commons.config.ConfigurationUtils;
import doryanbessiere.capturetheflag.minecraft.commons.cuboid.Cuboid;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.listener.listeners.LoggerListener;
import doryanbessiere.capturetheflag.minecraft.map.Map;
import doryanbessiere.capturetheflag.minecraft.map.MapManager;
import doryanbessiere.capturetheflag.minecraft.team.Team;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CaptureTheFlagCommand extends SimpleCommand {

    public CaptureTheFlagCommand() {
        super("ctf");
    }

    @Override
    public boolean onExecute(CommandSender sender, String[] arguments) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                onInfo(sender);
                return true;
            }
        }
        if(arguments.length == 1){
            if(arguments[0].equalsIgnoreCase("help")) {
                onHelp(sender);
            } else if (arguments[0].equalsIgnoreCase("forcestart")){
                GameManager.start(true);
            } else if (arguments[0].equalsIgnoreCase("setlobby")){
                if(sender instanceof Player){
                    Player player = (Player) sender;
                    if(!player.isOp()) {
                        CaptureTheFlag.sendMessage(player, "§cVous n'avez pas la permission d'éxécuter cette commande!");
                        return false;
                    }
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
                onInfo(sender);
            } else {
                CaptureTheFlag.sendMessage(sender, "§cCommande inconnue, essayer: §e/ctf help");
            }
        } else if(arguments.length == 2) {
            /**
             * - /ctf map list
             * - /ctf map wand
             */
            String arg1 = arguments[0];
            String arg2 = arguments[1];
            if(arg1.equalsIgnoreCase("map")){
                if(arg2.equalsIgnoreCase("list")){
                    ArrayList<Map> maps = MapManager.getMaps();
                    sender.sendMessage(Commons.lineSeparator("Liste des maps"));
                    sender.sendMessage("");
                    sender.sendMessage("§eNombre de map");
                    sender.sendMessage("  §6» §c"+maps.size());
                    sender.sendMessage("");
                    sender.sendMessage("§eMaps:");
                    for(Map map : maps){
                        sender.sendMessage("  §6» §c"+map.getName());
                    }
                    sender.sendMessage("");
                } else if(arg2.equalsIgnoreCase("wand")){
                    if(!(sender instanceof Player)) {
                        CaptureTheFlag.sendMessage(sender, "§cVous devez être un joueur!");
                        return false;
                    }
                    Player player = (Player) sender;

                    player.getInventory().addItem(GameManager.WAND);
                    CaptureTheFlag.sendMessage(player, "§fCette outil permet de sélectionner une zone");
                    CaptureTheFlag.sendMessage(player, "§8» §7Vous devez sélectionner un point A (clic-droit) et un point gauche B (clic-gauche)");
                }
            } else {
                CaptureTheFlag.sendMessage(sender, "§cCommande inconnue, essayer: §e/ctf help");
            }
        } else if(arguments.length == 3) {
            /**
             * - /ctf map add <map>
             * - /ctf map remove <map>
             * - /ctf map teleport <map>
             * - /ctf map setspawn <blue|red>
             * - /ctf map setflaf <blue|red>
             * - /ctf map setzone <blue|red>
             */
            String arg1 = arguments[0];
            String arg2 = arguments[1];
            String arg3 = arguments[2];
            if(arg1.equalsIgnoreCase("map")){
                if(arg2.equalsIgnoreCase("add")){
                    if(!MapManager.exist(arg3)){
                        MapManager.createMap(arg3);
                        CaptureTheFlag.sendMessage(sender, "§aLa map §f'"+arg3+"' §avient d'être rajouté.");
                    } else {
                        CaptureTheFlag.sendMessage(sender, "§cCette map existe déjà !");
                    }
                } else if(arg2.equalsIgnoreCase("setspawn")){
                    if(!(sender instanceof Player)) {
                        CaptureTheFlag.sendMessage(sender, "§cVous devez être un joueur!");
                        return false;
                    }

                    Player player = (Player) sender;
                    Team team = Team.fromName(arg3);
                    if(team != null){
                        String mapName = player.getLocation().getWorld().getName();
                        Map map = MapManager.getMap(mapName);
                        if(map != null){
                            map.getSpawns().put(team, player.getLocation());
                            map.save();
                            CaptureTheFlag.sendMessage(sender, "§aVous avez bien défini le point d'apparition de l'équipe "+team.getNameColor()+team.getName()+"§a.");
                        } else {
                            CaptureTheFlag.sendMessage(sender, "§cCette map n'existe pas !");
                        }
                    } else {
                        CaptureTheFlag.sendMessage(sender, "§cCette équipe n'existe pas !");
                    }
                } else if(arg2.equalsIgnoreCase("setflag")){
                    if(!(sender instanceof Player)) {
                        CaptureTheFlag.sendMessage(sender, "§cVous devez être un joueur!");
                        return false;
                    }

                    Player player = (Player) sender;
                    Team team = Team.fromName(arg3);
                    if(team != null){
                        String mapName = player.getLocation().getWorld().getName();
                        Map map = MapManager.getMap(mapName);
                        if(map != null){
                            Block block = player.getLocation().getBlock();
                            if(block.getType() == Material.STANDING_BANNER){
                                map.getFlags().put(team, player.getLocation());
                                map.save();
                                CaptureTheFlag.sendMessage(sender, "§aVous avez bien défini le l'emplacement du drapeau de l'équipe "+team.getNameColor()+team.getName()+"§a.");
                            } else {
                                CaptureTheFlag.sendMessage(sender, "§cVous devez être placé sur un le bloc où est placé la bannière! §7(bloc actuel : "+ block.getType()+")");
                            }
                        } else {
                            CaptureTheFlag.sendMessage(sender, "§cCette map n'existe pas !");
                        }
                    } else {
                        CaptureTheFlag.sendMessage(sender, "§cCette équipe n'existe pas !");
                    }
                } else if(arg2.equalsIgnoreCase("setarea")){
                    if(!(sender instanceof Player)) {
                        CaptureTheFlag.sendMessage(sender, "§cVous devez être un joueur!");
                        return false;
                    }

                    Player player = (Player) sender;
                    Team team = Team.fromName(arg3);
                    if(team != null){
                        String mapName = player.getLocation().getWorld().getName();
                        Map map = MapManager.getMap(mapName);
                        if(map != null){
                            Location position1 = GameManager.getPosition1().get(player);
                            Location position2 = GameManager.getPosition2().get(player);

                            position1.getBlock().setType(Material.AIR);
                            position2.getBlock().setType(Material.AIR);

                            map.getAreas().put(team, new Cuboid(position1, position2));
                            map.save();

                            CaptureTheFlag.sendMessage(sender, "§aVous avez défini la zone d'apparition pour l'équipe "+team.getNameColor()+team.getName()+"§a.");
                        } else {
                            CaptureTheFlag.sendMessage(sender, "§cCette map n'existe pas !");
                        }
                    } else {
                        CaptureTheFlag.sendMessage(sender, "§cCette équipe n'existe pas !");
                    }
                } else if(arg2.equalsIgnoreCase("remove")){
                    if(MapManager.exist(arg3)){
                        MapManager.removeMap(arg3);
                        CaptureTheFlag.sendMessage(sender, "§cLa map §f'"+arg3+"' §cvient d'être retiré.");
                    } else {
                        CaptureTheFlag.sendMessage(sender, "§cCette map n'existe pas !");
                    }
                } else if(arg2.equalsIgnoreCase("teleport")){
                    if(sender instanceof Player){
                        Player player = (Player)sender;
                        if(MapManager.exist(arg3)){
                            Map map = MapManager.getMap(arg3);
                            player.teleport(map.getSpawns().get(Team.RED));

                            CaptureTheFlag.sendMessage(sender, "§aVous avez été téléporté à la map §f'"+arg3+"'§a.");
                        } else {
                            CaptureTheFlag.sendMessage(sender, "§cCette map n'existe pas !");
                        }
                    } else {
                        CaptureTheFlag.sendMessage(sender, "§cVous devez être un joueur!");
                    }
                } else {
                    CaptureTheFlag.sendMessage(sender, "§cCommande inconnue, essayer: §e/ctf help");
                }
            } else {
                CaptureTheFlag.sendMessage(sender, "§cCommande inconnue, essayer: §e/ctf help");
            }
        } else if(arguments.length == 0) {
            onHelp(sender);
        } else {
            onHelp(sender);
            CaptureTheFlag.sendMessage(sender, "§cCommande inconnue, essayer: §e/ctf help");
        }
        return true;
    }

    public void onInfo(CommandSender sender){
        sender.sendMessage(Commons.lineSeparator("CaptureTheFlag"));
        sender.sendMessage("");
        sender.sendMessage("§eAuteur:");
        sender.sendMessage("§6» §cDoryan BESSIERE");
        sender.sendMessage("");
        sender.sendMessage("§eDiscord:");
        sender.sendMessage("§6» §cDoryan#7216");
        sender.sendMessage("");
        sender.sendMessage("§eListe des commandes:");
        for(SimpleCommand command : CaptureTheFlag.getInstance().getCommandManager().getCommands()){
            sender.sendMessage(" §6» §c/"+command.getCommand());
            if(command.getDescription() != null)
                sender.sendMessage("   §e"+command.getDescription());
            sender.sendMessage("");
        }
        sender.sendMessage("");
        sender.sendMessage("§eContact: ");
        sender.sendMessage("  §6» §fdoryanbessiere.pro@gmail.com");
        sender.sendMessage("  §6» §fcontact@doryanbessiere.fr");
        sender.sendMessage("");
    }

    @Override
    public void onHelp(CommandSender sender) {
        sender.sendMessage(Commons.lineSeparator("CaptureTheFlag"));
        sender.sendMessage("  §7- /ctf map §7<add|remove|teleport> <map>");
        sender.sendMessage("    §fPermet de rajouter, retirer, et de se téléporter à une map");
        sender.sendMessage(" ");
        sender.sendMessage("  §7- /ctf map §7list");
        sender.sendMessage("    §fPermet de voir la liste des maps");
        sender.sendMessage(" ");
        sender.sendMessage("  §7- /ctf map §7wand");
        sender.sendMessage("    §fPermet de vous donner l'outil qui permet de sélectionner une zone");
        sender.sendMessage("    §cAttention » §evous devez être sur le monde (la map).");
        sender.sendMessage(" ");
        sender.sendMessage("  §7- /ctf map setspawn <blue|red>");
        sender.sendMessage("    §fPermet de définir l'emplacement d'apparition d'une équipe");
        sender.sendMessage("    §cAttention » §evous devez être sur le monde (la map).");
        sender.sendMessage(" ");
        sender.sendMessage("  §7- /ctf map setflag <blue|red>");
        sender.sendMessage("    §fPermet de définir l'emplacement du drapeau d'une équipe");
        sender.sendMessage("    §cAttention » §evous devez être sur le monde (la map) et vous devez être placez sur le bloc de la bannière.");
        sender.sendMessage(" ");
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
