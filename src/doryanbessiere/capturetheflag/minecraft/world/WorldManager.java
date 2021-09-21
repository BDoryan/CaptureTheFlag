package doryanbessiere.capturetheflag.minecraft.world;

import org.bukkit.World;

public class WorldManager {

    public static void initWorld(World world){
        world.setGameRuleValue("doDaylightCycle", "false");
    }
}
