package doryanbessiere.capturetheflag.minecraft.commons;

import org.bukkit.Location;

public class Commons {

    public static String lineSeparator() {
        return "§7-----------------------------------------------------";
    }

    public static String lineSeparator(String title) {
        int max = 53;
        int length = title.length() + 4;

        int diff = (max - length) / 2;

        String line = "§7";

        for(int i = 0; i < diff; i++){
            line += "-";
        }
        line += "[ §f"+title +" §7]";
        for(int i = 0; i < diff; i++){
            line += "-";
        }

        return line;
    }

    public static boolean compareLocation(Location location1, Location location2){
        return location1.getBlockX() == location2.getBlockX()
                && location1.getBlockY() == location2.getBlockY()
                && location1.getBlockZ() == location2.getBlockZ();
    }
}
