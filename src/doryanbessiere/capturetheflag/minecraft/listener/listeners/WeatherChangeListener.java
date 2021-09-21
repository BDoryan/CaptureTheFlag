package doryanbessiere.capturetheflag.minecraft.listener.listeners;

import doryanbessiere.capturetheflag.minecraft.commons.logger.Logger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChangeListener implements Listener {

    @EventHandler
    public void WeatherChangeEvent_(WeatherChangeEvent event){
        event.setCancelled(true);
        Logger.debug("Weather change cancelled.");
    }
}
