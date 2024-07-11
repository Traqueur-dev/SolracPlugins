package fr.traqueur.solrac.randomtp.listeners;

import fr.traqueur.solrac.randomtp.RandomTPManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class RandomTPListener implements Listener {

    private RandomTPManager randomTPManager;

    public RandomTPListener(RandomTPManager randomTPManager) {
        this.randomTPManager = randomTPManager;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        this.randomTPManager.handleListener(event);
    }
}
