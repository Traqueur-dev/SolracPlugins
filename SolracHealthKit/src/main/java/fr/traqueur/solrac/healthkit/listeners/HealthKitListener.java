package fr.traqueur.solrac.healthkit.listeners;

import fr.traqueur.solrac.healthkit.HealthKitManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class HealthKitListener implements Listener {

    private final HealthKitManager healthKitManager;

    public HealthKitListener(HealthKitManager healthKitManager) {
        this.healthKitManager = healthKitManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        this.healthKitManager.handleInteract(event);
    }
}
