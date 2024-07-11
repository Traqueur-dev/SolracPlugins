package fr.traqueur.solrac.magicwand.listeners;

import fr.traqueur.solrac.magicwand.MagicWandManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class MagicWandListener implements Listener {

    private MagicWandManager magicWandManager;


    public MagicWandListener(MagicWandManager magicWandManager) {
        this.magicWandManager = magicWandManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        magicWandManager.handleListener(event);
    }
}
