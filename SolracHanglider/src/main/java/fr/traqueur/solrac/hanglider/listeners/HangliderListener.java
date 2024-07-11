package fr.traqueur.solrac.hanglider.listeners;

import fr.traqueur.solrac.hanglider.HangliderManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class HangliderListener implements Listener {

    private HangliderManager hangliderManager;

    public HangliderListener(HangliderManager hangliderManager) {
        this.hangliderManager = hangliderManager;
    }

    @EventHandler
    public void onFly(PlayerMoveEvent event) {
        this.hangliderManager.handleFlyEvent(event);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        this.hangliderManager.handleInteract(event);
    }

    public void onRepair(PrepareAnvilEvent event) {
        this.hangliderManager.handleAnvil(event);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        this.hangliderManager.handleFallEvent(event);
    }
}
