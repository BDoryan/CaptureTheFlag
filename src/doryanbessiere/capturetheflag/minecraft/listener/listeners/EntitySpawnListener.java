package doryanbessiere.capturetheflag.minecraft.listener.listeners;

import doryanbessiere.capturetheflag.minecraft.commons.logger.Logger;
import doryanbessiere.capturetheflag.minecraft.commons.logger.LoggerType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntitySpawnListener implements Listener {

    @EventHandler
    public void EntitySpawnEvent_(EntitySpawnEvent event){
        if(!(event.getEntity() instanceof Player)){
            Logger.log(LoggerType.DEBUG, event.getEntity()+" cannot appear (spawn) !");
            event.setCancelled(true);
            return;
        }
    }
}
