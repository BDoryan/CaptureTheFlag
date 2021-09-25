package doryanbessiere.capturetheflag.minecraft.compass;

import doryanbessiere.capturetheflag.minecraft.commons.items.ItemBuilder;
import doryanbessiere.capturetheflag.minecraft.flag.Flag;
import doryanbessiere.capturetheflag.minecraft.player.GamePlayer;
import doryanbessiere.capturetheflag.minecraft.team.Team;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Compass {

    private GamePlayer player;
    private ItemStack itemStack;

    private CompassMode compassMode = CompassMode.PASSIF;

    public Compass(GamePlayer player) {
        this.player = player;

        this.itemStack = new ItemStack(Material.COMPASS);
    }

    public void update(){
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        String displayName = "§7Tracker §6» ";
        Team team = player.getTeam();
        Team oppenentTeam = Team.RED == team ? Team.BLUE : Team.RED;
        Flag teamFlag = team.getFlag();
        Flag opppenentFlag = team.getFlag();

        if(compassMode == CompassMode.PASSIF){
            updateCompass(teamFlag, opppenentFlag, team, oppenentTeam);
        } else if (compassMode == CompassMode.AGGRESSIF){
            updateCompass(opppenentFlag, teamFlag, oppenentTeam, team);
        }
        itemMeta.setDisplayName(displayName);
    }

    private void updateCompass(Flag flagTarget, Flag flag, Team teamTarget, Team team){
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        String displayName = "§7Tracker §6» ";

        if(!flagTarget.atBase()){
            GamePlayer carrier = flagTarget.getCarrier();
            if(carrier != null){
                displayName += team.getNameColor()+player.getName();
            } else {
                displayName += "§d"+flagTarget.getLocation().getBlockX()+"x, "+flagTarget.getLocation().getBlockZ()+"z";
            }
            player.getPlayer().setCompassTarget(flagTarget.getLocation());
        } else {
            displayName += "§fdrapeau "+team.getNameColor()+team.getName();
            player.getPlayer().setCompassTarget(flag.getLocation());
        }
        itemMeta.setDisplayName(displayName);
    }

    public void giveCompass(){
        update();

        Player player = this.player.getPlayer();
        player.getInventory().setItem(7, itemStack);
        player.updateInventory();
    }

    /**
     * mode passif:
     *     1er cas : cible le drapeau de l'équipe adversaire
     *     2ème cas : cible son drapeau qui est volé par l'adversaire
     *
     * mode aggresif:
     *     1er cas : cible son drapeau
     *     2ème cas : cible le drapeau de l'équipe adversaire que son collègue à volé
     *
     */
    public void setCompassMode(CompassMode compassMode) {
        this.compassMode = compassMode;
    }

    public CompassMode getCompassMode() {
        return compassMode;
    }
}
