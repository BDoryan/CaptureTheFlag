package doryanbessiere.capturetheflag.minecraft.commons.config;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.commons.cuboid.Cuboid;
import doryanbessiere.capturetheflag.minecraft.commons.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationUtils {

    /**
     *
     * This method only allows to define the values, the saving of the configuration file must be done by you.
     *
     * @param configuration
     * @param location
     * @param path
     */
    public static void setLocation(FileConfiguration configuration, Location location, String path, boolean direction){
        configuration.set(path+".world", location.getWorld().getName());
        configuration.set(path+".x", location.getX());
        configuration.set(path+".y", location.getY());
        configuration.set(path+".z", location.getZ());
        if(direction){
            configuration.set(path+".yaw", location.getYaw());
            configuration.set(path+".pitch", location.getPitch());
        }
    }

    /**
     *
     * This method only allows to define the values, the saving of the configuration file must be done by you.
     *
     * @param configuration
     * @param location
     * @param path
     */
    public static void setLocation(FileConfiguration configuration, Location location, String path){
        setLocation(configuration, location,path,true);
    }

    /**
     * Allows you to retrieve a location defined in a configuration file
     *
     * @param configuration
     * @param path
     * @return
     */
    public static Location getLocation(FileConfiguration configuration, String path){
        String world = configuration.getString(path+".world");
        double z = configuration.getDouble(path+".z");
        double y = configuration.getDouble(path+".y");
        double x = configuration.getDouble(path+".x");
        float yaw = 0;
        float pitch = 0;
        if(configuration.contains(path+".yaw"))
            yaw = (float) configuration.getDouble(path+".yaw");

        if(configuration.contains(path+".pitch"))
            pitch = (float) configuration.getDouble(path+".pitch");

        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public static void setArea(FileConfiguration configuration, Cuboid cuboid, String path){
        setLocation(configuration, cuboid.getMinimum(), path+".minimum");
        setLocation(configuration, cuboid.getMaximum(), path+".maximum");
    }

    public static Cuboid getArea(FileConfiguration configuration, String path) {
        Location minimum = getLocation(configuration, path+".minimum");
        Location maximum = getLocation(configuration, path+".maximum");

        return new Cuboid(minimum, maximum);
    }
}
