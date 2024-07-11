package fr.traqueur.solrac.dynamite.listeners;

import fr.traqueur.solrac.dynamite.DynamiteManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class DynamiteListener implements Listener {

    private DynamiteManager dynamiteManager;

    public DynamiteListener(DynamiteManager dynamiteManager) {
         this.dynamiteManager = dynamiteManager;
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileHitEvent event) {
        dynamiteManager.handleEggShoot(event);
    }

}
