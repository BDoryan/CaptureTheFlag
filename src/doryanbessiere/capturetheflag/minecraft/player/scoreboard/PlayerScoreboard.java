package doryanbessiere.capturetheflag.minecraft.player.scoreboard;

import doryanbessiere.capturetheflag.minecraft.commons.logger.Logger;
import doryanbessiere.capturetheflag.minecraft.commons.scoreboard.SimpleScoreboard;
import doryanbessiere.capturetheflag.minecraft.flag.Flag;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.game.GameState;
import doryanbessiere.capturetheflag.minecraft.player.GamePlayer;
import doryanbessiere.capturetheflag.minecraft.schedulers.GameRunnable;
import doryanbessiere.capturetheflag.minecraft.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class PlayerScoreboard extends SimpleScoreboard {

    private GamePlayer player;

    String[] titles = new String[] {
        "§7§lCaptureTheFlag",
        "§9§lCaptureTheFlag",
        "§c§lCaptureTheFlag",
    };

    public PlayerScoreboard(GamePlayer player) {
        super("");

        this.player = player;
    }

    /**
     * Apply the scoreboard to the player
     */
    public void create(){
        send(player.getPlayer());
    }

    private HashMap<Integer, String> lines = new HashMap<>();

    int index = 0;
    GameState state;

    @Override
    public void update() {
        if(index >= titles.length)
            index = 0;

        if(state != GameManager.getState()){
            delete();
            create();
            state = GameManager.getState();
            Logger.debug("reset");
        }

        setTitle(titles[index]);
        index++;

        if(GameManager.isState(GameState.WAITING)) {
            lines.put(8, "§8§m-------------------");
            lines.put(7, "§eJoueurs:");
            lines.put(6, "§6» §c" + GameManager.getPlayers().size() + "§7/§c" + GameManager.MAX_PLAYERS);
            lines.put(5, "§6");
            lines.put(4, "§eKit §6» §cpas disponible");
            if (GameManager.getMap() != null) {
                lines.put(3, "§eMap §6» §c" + GameManager.getMap().getName());
            }
            lines.put(2, "§f§8§m-------------------");
            lines.put(1, "§7play.enantia.fr");
        } else if(GameManager.isState(GameState.INGAME)){
            lines.put(20, "§8§m-------------------");
            int index = 19;
            for(Team team : Team.values()){
                lines.put(index, team.getNameColor()+team.getNameColor()+team.getName()+ " §f: §f"+team.getCapture()+"§7/§f3");
                Flag flag = team.getFlag();
                lines.put(index - 1, " §fDrapeau §6» "+(flag.hasCarrier() ? "§7["+"§c"+flag.getCarrier().getName()+"§7]" : flag.atBase() ? "§7["+team.getNameColor()+"Home"+"§7]" : ""));
                index-=2;
                if(!flag.hasCarrier()  && !flag.atBase()){
                    lines.put(index - 1, "   §eX §6» §d"+flag.getLocation().getBlockX());
                    lines.put(index - 2, "   §eZ §6» §d"+flag.getLocation().getBlockZ());
                    index-=2;
                }
            }
            lines.put(11, "§e§6");
            lines.put(10, "§fKills §6» §c"+player.getKills());
            lines.put(9, "§fMorts §6» §c"+player.getDeaths());
            lines.put(8, "§f§e");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
            String time = simpleDateFormat.format(new Date(GameManager.getGameRunnable().getSeconds() * 1000));
            lines.put(7, "§eTemps restant §6» §c"+time);
            lines.put(5, "§e");
            lines.put(4, "§eKit §6» §cpas disponible");
            lines.put(3, "§eMap §6» §c"+GameManager.getMap().getName());
            lines.put(2, "§f§8§m-------------------");
            lines.put(1, "§7play.enantia.fr");
        } else {

        }

        for (Integer slot : lines.keySet()) {
            String line = lines.get(slot);
            add(line, slot);
        }

        for (int loc = 15; loc > 0; loc--) {
            String myLine = lines.get(loc);
            String sbLine = this.get(loc);

            if (myLine == null && sbLine != null) {
                this.remove(loc);
                this.reset();
            } else {
                if (sbLine != null && myLine != null) {
                    if (!sbLine.equals(myLine)) {
                        this.remove(loc, sbLine);
                        this.add(myLine, loc);
                    }
                }
            }
        }
        super.update();
        lines.clear();

        Objective objective = getScoreboard()
                .getObjective("showhealth");

        if (objective == null) {
            objective = getScoreboard()
                    .registerNewObjective("showhealth", "health");

            objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
            objective.setDisplayName("§C"+"❤");
        }

        GameManager.getPlayers().forEach(player -> player.getPlayer().setHealth(player.getPlayer().getHealth()));
    }
}
