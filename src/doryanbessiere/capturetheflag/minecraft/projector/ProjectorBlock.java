package doryanbessiere.capturetheflag.minecraft.projector;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.commons.config.ConfigurationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class ProjectorBlock {

    private Location location;
    private ProjectorConfig config;

    public ProjectorBlock(Location location, ProjectorConfig config) {
        this.location = location;
        this.config = config;
    }

    public static ProjectorBlock load(FileConfiguration configuration, String path) {
        Location location = ConfigurationUtils.getLocation(configuration, path+".location");
        double power = configuration.getDouble(path+".power");
        float yaw = (float) configuration.getDouble(path+".direction.yaw");
        float pitch = (float) configuration.getDouble(path+".direction.pitch");
        ProjectorConfig config = new ProjectorConfig(yaw, pitch, power);
        return new ProjectorBlock(location, config);
    }

    public void delete(){
        FileConfiguration configuration = CaptureTheFlag.getConfiguration();
        String path = "maps."+location.getWorld().getName()+".projectors."+location.getBlockX()+"|"+location.getBlockY()+"|"+location.getBlockZ();
        configuration.set(path, null);
        CaptureTheFlag.saveConfiguration();
    }

    public void save(){
        FileConfiguration configuration = CaptureTheFlag.getConfiguration();
        String path = "maps."+location.getWorld().getName()+".projectors."+location.getBlockX()+"|"+location.getBlockY()+"|"+location.getBlockZ();

        ConfigurationUtils.setLocation(configuration, location, path+".location");
        configuration.set(path+".power", getConfig().getPower());
        configuration.set(path+".direction.yaw", getConfig().getYaw());
        configuration.set(path+".direction.pitch", getConfig().getPitch());
        CaptureTheFlag.saveConfiguration();
    }

    public Location getLocation() {
        return location;
    }

    public ProjectorConfig getConfig() {
        return config;
    }
}
