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
import doryanbessiere.capturetheflag.minecraft.player.GamePlayer;
import doryanbessiere.capturetheflag.minecraft.projector.ProjectorConfig;
import doryanbessiere.capturetheflag.minecraft.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CaptureTheFlagCommand extends SimpleCommand {

    public static final HashMap<GamePlayer, ProjectorConfig> projectorConfig = new HashMap<>();

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
            } else if (arguments[0].equalsIgnoreCase("stop")){
                GameManager.finish(null);
            } else if (arguments[0].equalsIgnoreCase("addfirework")){
                if(!(sender instanceof Player)) {
                    CaptureTheFlag.sendMessage(sender, "§cVous devez être un joueur!");
                    return false;
                }

                Player player = (Player) sender;

                FileConfiguration configuration = CaptureTheFlag.getConfiguration();
                ConfigurationUtils.setLocation(configuration, player.getLocation(), "fireworks."+ UUID.randomUUID());
                CaptureTheFlag.sendMessage(player, "§aVous avez ajouter un nouvelle emplacement d'apparition de feu d'artifice.");
                CaptureTheFlag.saveConfiguration();
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
             *
             * - /ctf projector tool
             *
             * - /ctf settime <seconcds>
             */
            String arg1 = arguments[0];
            String arg2 = arguments[1];
            if(arg1.equalsIgnoreCase("map")) {
                if (arg2.equalsIgnoreCase("list")) {
                    ArrayList<Map> maps = MapManager.getMaps();
                    sender.sendMessage(Commons.lineSeparator("Liste des maps"));
                    sender.sendMessage("");
                    sender.sendMessage("§eNombre de map");
                    sender.sendMessage("  §6» §c" + maps.size());
                    sender.sendMessage("");
                    sender.sendMessage("§eMaps:");
                    for (Map map : maps) {
                        sender.sendMessage("  §6» §c" + map.getName());
                    }
                    sender.sendMessage("");
                } else if (arg2.equalsIgnoreCase("wand")) {
                    if (!(sender instanceof Player)) {
                        CaptureTheFlag.sendMessage(sender, "§cVous devez être un joueur!");
                        return false;
                    }
                    Player player = (Player) sender;

                    player.getInventory().addItem(GameManager.WAND);
                    CaptureTheFlag.sendMessage(player, "§fCette outil permet de sélectionner une zone");
                    CaptureTheFlag.sendMessage(player, "§8» §7Vous devez sélectionner un point A (clic-droit) et un point gauche B (clic-gauche)");
                }
            } else if(arg1.equalsIgnoreCase("projector")){
                if(arg2.equalsIgnoreCase("tool")){
                    if(!(sender instanceof Player)) {
                        CaptureTheFlag.sendMessage(sender, "§cVous devez être un joueur!");
                        return false;
                    }

                    Player player = (Player) sender;
                    player.getInventory().addItem(GameManager.PROJECTOR_TOOL);
                    CaptureTheFlag.sendMessage(player, "§aVous avez reçu l'outil pour les projecteurs.");
                } else {
                    CaptureTheFlag.sendMessage(sender, "§cCommande inconnue, essayer: §e/ctf help");
                }
            } else if(arg1.equalsIgnoreCase("settime")){
                try {
                    Integer seconds = Integer.valueOf(arg2);
                    GameManager.getGameRunnable().setSeconds(seconds);
                    CaptureTheFlag.sendMessage(sender, "§aLe temps restant est désormais défini.");
                } catch (NumberFormatException exception){
                    CaptureTheFlag.sendMessage(sender, "§cMerci de définir un nombre");
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
             *
             * - /ctf setteam <player> <team>
             *
             * - /ctf projector setconfig <power>
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
            } else if (arg1.equalsIgnoreCase("projector")){
                if(!(sender instanceof Player)){
                    CaptureTheFlag.sendMessage(sender, "§cVous devez être un joueur !");
                    return false;
                }
                Player player = (Player)sender;
                GamePlayer gamePlayer = GameManager.getGamePlayer(player);

                if(arg2.equalsIgnoreCase("setconfig")){
                    try {
                        Double power = Double.valueOf(arg3);
                        projectorConfig.put(gamePlayer, new ProjectorConfig(player.getLocation().getYaw(), player.getLocation().getPitch(), power));
                        gamePlayer.sendMessage("§aVotre configuration à bien été défini.");
                    } catch (NumberFormatException exception){
                        CaptureTheFlag.sendMessage(sender, "§cMerci de définir nombre entier ou décimal");
                    }
                } else {
                    CaptureTheFlag.sendMessage(sender, "§cCommande inconnue, essayer: §e/ctf help");
                }
            } else if (arg1.equalsIgnoreCase("setteam")){
                Player target = Bukkit.getPlayer(arg2);
                if(target == null){
                    CaptureTheFlag.sendMessage(sender, "§cCe joueur n'est pas en ligne !");
                    return false;
                }

                Team team = Team.fromName(arg3);
                if(team != null){
                    GamePlayer gameTarget = GameManager.getGamePlayer(target);
                    if(gameTarget.getTeam() == team){
                        CaptureTheFlag.sendMessage(sender, "§cCe joueur est déjà dans cette équipe !");
                        return false;
                    }
                    gameTarget.getTeam().removePlayer(gameTarget);
                    team.addPlayer(gameTarget);
                    gameTarget.death();
                    CaptureTheFlag.sendMessage(target, "§f"+sender.getName()+" §7vous a mis dans l'équipe "+team.getNameColor()+team.getName());
                    CaptureTheFlag.sendMessage(sender, "§f"+target.getName()+" §7est désormais dans l'équipe "+team.getNameColor()+team.getName());
                } else {
                    CaptureTheFlag.sendMessage(sender, "§cCette équipe n'existe pas !");
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
        sender.sendMessage("  §7- /ctf setteam §7<joueur> <blue|red>");
        sender.sendMessage("    §fPermet de rajouter, retirer, et de se téléporter à une map");
        sender.sendMessage(" ");
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
        sender.sendMessage("  §7- /ctf projector §7config <power>");
        sender.sendMessage("    §fPermet de définir les paramètre de propulsion, ");
        sender.sendMessage("    §cAttention » §eLa direction où vous regardez lors de la commande sera la direction de propulsion.");
        sender.sendMessage(" ");
        sender.sendMessage("  §7- /ctf projector §7delete");
        sender.sendMessage("    §fPermet de de retirer les blocs de cette sélection.");
        sender.sendMessage(" ");
        sender.sendMessage("  §7- /ctf projector §7tool");
        sender.sendMessage("    §fPermet de d'enregister des nouveaux blocs projecteur");
        sender.sendMessage("    §cAttention » §cvous devez avoir défini une configuration auparavant d'enregistrer un bloc.");
        sender.sendMessage(" ");
        sender.sendMessage("  §7- /ctf map setspawn <blue|red>");
        sender.sendMessage("    §fPermet de définir l'emplacement d'apparition d'une équipe");
        sender.sendMessage("    §cAttention » §evous devez être sur le monde (la map).");
        sender.sendMessage(" ");
        sender.sendMessage("  §7- /ctf map setflag <blue|red>");
        sender.sendMessage("    §fPermet de définir l'emplacement du drapeau d'une équipe");
        sender.sendMessage("    §cAttention » §evous devez être sur le monde (la map) et vous devez être placez sur le bloc de la bannière.");
        sender.sendMessage(" ");
        sender.sendMessage("  §7- /ctf settime §7<seconds>");
        sender.sendMessage("    §fPermet de définir le temps restant §c(seulement en partie)");
        sender.sendMessage(" ");
        sender.sendMessage("  §7- /ctf forcestart");
        sender.sendMessage("    §fPermet de forcer le lancement de la partie");
        sender.sendMessage(" ");
        sender.sendMessage("  §7- /ctf stop");
        sender.sendMessage("    §fPermet de terminer la partie");
        sender.sendMessage(" ");
        sender.sendMessage("  §7- /ctf setlobby");
        sender.sendMessage("    §fPermet de définir le point d'apparition du lobby");
        sender.sendMessage(" ");
        sender.sendMessage("  §7- /ctf addfirework");
        sender.sendMessage("    §fPermet d'ajouter l'emplacement d'apparition des feux d'artifices");
        sender.sendMessage(" ");
        sender.sendMessage("  §7- /ctf getlobby");
        sender.sendMessage("    §fPermet de vous téléporter au point d'apparition");
        sender.sendMessage(" ");
        sender.sendMessage("  §7- /ctf debug");
        sender.sendMessage("    §fPermet d'activer ou désactiver le journal de bord");
        sender.sendMessage(" ");
    }
}
