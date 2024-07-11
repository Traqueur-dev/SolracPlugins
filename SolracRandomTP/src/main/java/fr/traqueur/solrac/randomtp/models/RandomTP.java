package fr.traqueur.solrac.randomtp.models;

import fr.traqueur.solrac.randomtp.RandomTPManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RandomTP implements Runnable {

    private RandomTPManager randomTPManager;
    private ArrayList<UUID> waiters;
    private UUID playerUUID;
    private long runtime;
    private int taskID;
    private Location destination;

    public RandomTP(RandomTPManager randomTPManager, UUID playerUUID, Location destination) {
        this.randomTPManager = randomTPManager;
        this.playerUUID = playerUUID;
        this.runtime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(randomTPManager.getRANDOMTP_DELAY() + 1);
        this.waiters = this.randomTPManager.getWaiterRandomTP();
        this.taskID = -1;
        this.destination = destination;

    }

    @Override
    public void run() {

        if(!waiters.contains(playerUUID)) {
            this.cancel(true);
            return;
        }

        Player player = Bukkit.getPlayer(playerUUID);
        if(player == null) {
            this.cancel(false);
            return;
        }

        if(this.getRemaining() <= 0L) {
            this.cancel(false);
            TextComponent component = Component
                    .text("Vous venez d'être téléporté.")
                    .color(NamedTextColor.GREEN);
           player.sendMessage(component);

           if(!destination.isChunkLoaded()) {
               destination.getChunk().load();
           }
           player.teleport(destination);
        } else {
            long secondes = TimeUnit.MILLISECONDS.toSeconds(this.getRemaining());
            if(secondes >= 1L) {
                Title title = Title.title(Component.text("Solrac RandomTP").color(NamedTextColor.RED),
                        Component.text( secondes + (secondes > 1L ? " secondes." : " seconde.")).color(NamedTextColor.YELLOW));
                player.showTitle(title);
            }
        }


    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    private long getRemaining() {
        return this.runtime - System.currentTimeMillis();
    }

    private void cancel(boolean notify) {
        waiters.remove(playerUUID);
        Bukkit.getScheduler().cancelTask(taskID);
        if (notify) {
            TextComponent component = Component.text("Votre téléportation vient d'être annulée.").color(NamedTextColor.RED);
            Bukkit.getPlayer(playerUUID).sendMessage(component);
        }
    }
}
