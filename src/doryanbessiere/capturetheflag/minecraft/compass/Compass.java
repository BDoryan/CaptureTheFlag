package doryanbessiere.capturetheflag.minecraft.compass;

import doryanbessiere.capturetheflag.minecraft.commons.Commons;
import doryanbessiere.capturetheflag.minecraft.commons.items.ItemBuilder;
import doryanbessiere.capturetheflag.minecraft.flag.Flag;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.game.GameState;
import doryanbessiere.capturetheflag.minecraft.player.GamePlayer;
import doryanbessiere.capturetheflag.minecraft.team.Team;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Compass {

    private GamePlayer player;

    private CompassMode compassMode = CompassMode.AGGRESSIF;

    public Compass(GamePlayer player) {
        this.player = player;

    }

    public void update(){
        if(!GameManager.isState(GameState.INGAME))return;
        ItemStack itemStack = getItemStack();
        if(itemStack == null)return;
        Team team = player.getTeam();
        Team oppenentTeam = Team.RED == team ? Team.BLUE : Team.RED;

        Flag teamFlag = team.getFlag();
        Flag opppenentFlag = oppenentTeam.getFlag();

        if(compassMode == CompassMode.PASSIF){
            updateCompass(opppenentFlag, teamFlag, oppenentTeam, team);
        } else if (compassMode == CompassMode.AGGRESSIF){
            updateCompass(teamFlag,opppenentFlag, team, oppenentTeam);
        }
    }

    private void updateCompass(Flag flagTarget, Flag flag, Team teamTarget, Team team){
        ItemStack itemStack = getItemStack();
        if(itemStack == null)return;
        ItemMeta itemMeta = itemStack.getItemMeta();
        String displayName = "§7Tracker §6» ";

        if(!flagTarget.atBase()){
            GamePlayer carrier = flagTarget.getCarrier();
            if(carrier != null){
                displayName += carrier.getTeam().getNameColor()+carrier.getName();
            } else {
                displayName += "§d"+flagTarget.getLocation().getBlockX()+"x, "+flagTarget.getLocation().getBlockZ()+"z";
            }
            player.getPlayer().setCompassTarget(flagTarget.getLocation());
        } else {
            displayName += "§fdrapeau "+team.getNameColor()+team.getName();
            player.getPlayer().setCompassTarget(flag.getBase());
        }
        if(!itemMeta.hasDisplayName() || !itemMeta.getDisplayName().equals(displayName)){
            Commons.sendActionBar(player.getPlayer(), displayName);
        }
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
    }

    public void giveCompass(){
        update();

        ItemStack itemStack = new ItemStack(Material.COMPASS);
        Player player = this.player.getPlayer();
        player.getInventory().setItem(1, itemStack);
        player.updateInventory();
    }

    public ItemStack getItemStack(){
        Player player = this.player.getPlayer();
        return player.getInventory().getItem(1);
    }

    /**
     * mode passif:
     *     1er cas : cible son drapeau
     *     2ème cas : cible le drapeau de l'équipe adversaire que son collègue à volé
     *
     * mode aggresif:
     *     1er cas : cible le drapeau de l'équipe adversaire
     *     2ème cas : cible son drapeau qui est volé par l'adversaire
     *
     */
    public void setCompassMode(CompassMode compassMode) {
        this.compassMode = compassMode;
    }

    public CompassMode getCompassMode() {
        return compassMode;
    }

    public void switchMode(){
        int target = 0;
        for(CompassMode mode : CompassMode.values()){
            if(mode == this.compassMode)
                break;

            target++;
        }
        target++;
        setCompassMode(CompassMode.values()[target >= CompassMode.values().length ? 0 : target]);
    }

    public boolean compare(ItemStack itemStack) {
        return itemStack.hasItemMeta() &&
                getItemStack().hasItemMeta() &&
                getItemStack().getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName());
    }
}
