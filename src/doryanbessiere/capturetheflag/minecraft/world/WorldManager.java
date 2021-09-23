package doryanbessiere.capturetheflag.minecraft.world;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class WorldManager {

    public static void initWorld(World world){
        world.setGameRuleValue("doDaylightCycle", "false");
    }
}
