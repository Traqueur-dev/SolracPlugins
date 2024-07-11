package fr.traqueur.solrac;

import fr.traqueur.solrac.randomtp.RandomTPManager;
import fr.traqueur.solrac.randomtp.commands.RandomTPCommand;
import fr.traqueur.solrac.randomtp.listeners.RandomTPListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SolracRandomTP extends JavaPlugin {

    private RandomTPManager randomTPManager;

    @Override
    public void onEnable() {
        if(!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }

        this.saveDefaultConfig();

        this.randomTPManager = new RandomTPManager(this);
        this.randomTPManager.init();

        Bukkit.getPluginManager().registerEvents(new RandomTPListener(this.randomTPManager), this);
        this.getCommand("randomtp").setExecutor(new RandomTPCommand(this.randomTPManager));
    }

    @Override
    public void onDisable() {
        this.saveConfig();
    }
}
