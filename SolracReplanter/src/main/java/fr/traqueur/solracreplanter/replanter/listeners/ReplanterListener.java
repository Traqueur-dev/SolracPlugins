package fr.traqueur.solracreplanter.replanter.listeners;

import fr.traqueur.solracreplanter.replanter.ReplanterManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ReplanterListener implements Listener {

    private final ReplanterManager replanterManager;

    public ReplanterListener(ReplanterManager replanterManager) {
        this.replanterManager = replanterManager;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        replanterManager.handleListener(event);
    }
}
