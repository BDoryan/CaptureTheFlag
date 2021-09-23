package doryanbessiere.capturetheflag.minecraft.map;

import doryanbessiere.capturetheflag.minecraft.CaptureTheFlag;
import doryanbessiere.capturetheflag.minecraft.commons.logger.Logger;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Random;

public class MapManager {

    public static ArrayList<Map> maps = new ArrayList<>();

    public static void loads(FileConfiguration configuration){
        for (String mapName : configuration.getConfigurationSection("maps").getKeys(false)) {
            maps.add(Map.load(configuration, mapName));
        }
    }

    public static boolean exist(String mapName){
        return getMap(mapName) != null;
    }

    public static Map getMap(String mapName){
        for(Map map : maps){
            if(map.getName().equalsIgnoreCase(mapName)){
                return map;
            }
        }
        return null;
    }

    public static Map createMap(String name){
        Map map = new Map(name);
        map.save();
        maps.add(map);

        return map;
    }

    public static void removeMap(String name) {
        Map map = getMap(name);
        map.remove();
        if(map != null){
            maps.remove(map);
        }
    }

    public static ArrayList<Map> getMaps() {
        return maps;
    }

    public static Map randomMap() {
        if(maps.size() == 0)return null;
        if(maps.size() == 1)return maps.get(0);
        Map map = maps.get(new Random().nextInt(maps.size()) - 1);
        return map;
    }
}
