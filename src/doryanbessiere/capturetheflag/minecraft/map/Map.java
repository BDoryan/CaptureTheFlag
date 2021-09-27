package doryanbessiere.capturetheflag.minecraft.map;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.commons.Commons;
import doryanbessiere.capturetheflag.minecraft.commons.config.ConfigurationUtils;
import doryanbessiere.capturetheflag.minecraft.commons.cuboid.Cuboid;
import doryanbessiere.capturetheflag.minecraft.commons.logger.Logger;
import doryanbessiere.capturetheflag.minecraft.flag.Flag;
import doryanbessiere.capturetheflag.minecraft.game.GameManager;
import doryanbessiere.capturetheflag.minecraft.projector.ProjectorBlock;
import doryanbessiere.capturetheflag.minecraft.team.Team;
import doryanbessiere.capturetheflag.minecraft.world.WorldManager;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;

public class Map {

    private String name;

    private HashMap<Team, Location> spawns = new HashMap<Team, Location>();
    private HashMap<Team, Location> flags = new HashMap<Team, Location>();
    private HashMap<Team, Cuboid> areas = new HashMap<Team, Cuboid>();
    private ArrayList<ProjectorBlock> projectors = new ArrayList<ProjectorBlock>();

    public Map() {
    }

    public Map(String name) {
        this.name = name;
        World world = loadWorld();

        Logger.debug("world="+world);
        for(Team team : Team.values()){
            spawns.put(team, new Location(world, 0, 20, 0));
            flags.put(team, new Location(world, 0, 20, 0));
            areas.put(team, new Cuboid(new Location(world, 0, 0, 0),new Location(world, 0, 0, 0)));
        }

        save();
    }

    public static Map load(FileConfiguration configuration, String name){
        Map map = new Map();

        map.name = name;
        map.loadWorld();

        for(Team team : Team.values()){
            map.flags.put(team, ConfigurationUtils.getLocation(configuration, "maps."+name+"."+team.toString().toLowerCase()+".flag"));
            map.spawns.put(team, ConfigurationUtils.getLocation(configuration, "maps."+name+"."+team.toString().toLowerCase()+".spawn"));
            map.areas.put(team, ConfigurationUtils.getArea(configuration, "maps."+name+"."+team.toString().toLowerCase()+".area.spawn"));
        }

        if(configuration.contains("maps."+name+".projectors")){
            for (String projector : configuration.getConfigurationSection("maps."+name+".projectors").getKeys(false)) {
                map.projectors.add(ProjectorBlock.load(configuration, "maps."+name+".projectors."+projector));
            }
        } else {
            map.projectors = new ArrayList<>();
        }

        map.save();
        Logger.debug("Map.load("+name+");");

        return map;
    }

    public void addProjector(ProjectorBlock projectorBlock){
        if(existsProjector(projectorBlock.getLocation())){
            return;
        }

        projectors.add(projectorBlock);
        projectorBlock.save();
    }

    public ProjectorBlock getProjector(Location location){
        for(ProjectorBlock projectorBlock : projectors){
            if(Commons.compareLocation(location, projectorBlock.getLocation()))
                return projectorBlock;
        }
        return null;
    }

    public boolean existsProjector(Location location){
        return getProjector(location) != null;
    }

    public void removeProjector(ProjectorBlock projectorBlock){
        if(!existsProjector(projectorBlock.getLocation())){
            return;
        }

        projectors.remove(projectorBlock);
        projectorBlock.delete();
    }

    public void remove() {
        FileConfiguration configuration = CaptureTheFlag.getConfiguration();
        configuration.set("maps."+name, null);
        getWorld().getPlayers().forEach(player -> GameManager.teleportToLobby(player));
        Bukkit.unloadWorld(name, true);
        CaptureTheFlag.saveConfiguration();
    }

    public void save(){
        FileConfiguration configuration = CaptureTheFlag.getConfiguration();

        for(Team team : Team.values()){
            ConfigurationUtils.setLocation(configuration, flags.get(team), "maps."+name+"."+team.toString().toLowerCase()+".flag");
            ConfigurationUtils.setLocation(configuration, spawns.get(team), "maps."+name+"."+team.toString().toLowerCase()+".spawn");
            ConfigurationUtils.setArea(configuration, areas.get(team), "maps."+name+"."+team.toString().toLowerCase()+".area.spawn");
        }

        CaptureTheFlag.saveConfiguration();
    }

    public void init(){
        flags.forEach((team, location) -> {
            Flag flag = new Flag(team, location);
            flag.init();
            team.setFlag(flag);
        });
    }

    public World loadWorld(){
        World world = Bukkit.getWorld(name);
        if(world == null){
            WorldCreator worldCreator = new WorldCreator(name).environment(World.Environment.NORMAL);
            world = worldCreator.createWorld();
            WorldManager.initWorld(world);
            Bukkit.getWorlds().add(world);
            Logger.debug("loadWorld("+name+") : OK");
        } else {
            Logger.debug("loadWorld("+name+") : ALREADY LOADED");
        }
        return world;
    }

    public HashMap<Team, Location> getFlags() {
        return flags;
    }

    public HashMap<Team, Location> getSpawns() {
        return spawns;
    }

    public HashMap<Team, Cuboid> getAreas() {
        return areas;
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return Bukkit.getWorld(name);
    }
}
