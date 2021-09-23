package doryanbessiere.capturetheflag.minecraft.player.scoreboard;

import doryanbessiere.capturetheflag.minecraft.commons.scoreboard.SimpleScoreboard;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.game.GameState;
import doryanbessiere.capturetheflag.minecraft.player.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

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

    @Override
    public void update() {
        if(index >= titles.length)
            index = 0;

        setTitle(titles[index]);
        index++;

        if(GameManager.isState(GameState.WAITING)){
            lines.put(15, "§8§m-------------------");
            lines.put(14, "§eJoueurs:");
            lines.put(13, "§6» §c"+ GameManager.getPlayers().size() +"§7/§c"+GameManager.MAX_PLAYERS);
            lines.put(12, "§f§7");
            lines.put(11, "§eKit §6» §caucun");
            if(GameManager.map != null){
                lines.put(10, "§eMap §6» §c"+GameManager.map.getName());
            }
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
