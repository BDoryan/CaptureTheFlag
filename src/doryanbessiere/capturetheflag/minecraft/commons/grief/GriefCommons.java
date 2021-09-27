package doryanbessiere.capturetheflag.minecraft.commons.grief;

import doryanbessiere.capturetheflag.minecraft.commons.Commons;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.ArrayList;

public class GriefCommons {

    public static final ArrayList<Block> blocks = new ArrayList<>();

    public static void addBlock(org.bukkit.block.Block block){
        if(getBlock(block.getLocation()) != null)
            return;

        blocks.add(new Block(block.getType(), block.getLocation(), block.getState()));
    }

    public static Block getBlock(Location location){
        for(Block block : blocks)
            if(Commons.compareLocation(block.getLocation(), location))
                return block;

        return null;
    }

    public static void recoverBlock(Location location){
        Block block = getBlock(location);
        if(block == null)
            return;

        org.bukkit.block.Block minecraftBlock = block.getLocation().getBlock();
        minecraftBlock.setType(block.getType());
        minecraftBlock.getState().setData(block.getState().getData());
        minecraftBlock.getState().update();

        blocks.remove(block);
    }

    private static class Block {

        private Material type;
        private Location location;
        private BlockState state;

        public Block(Material type, Location location, BlockState state) {
            this.type = type;
            this.location = location;
            this.state = state;
        }

        public Location getLocation() {
            return location;
        }

        public Material getType() {
            return type;
        }

        public BlockState getState() {
            return state;
        }
    }
}
