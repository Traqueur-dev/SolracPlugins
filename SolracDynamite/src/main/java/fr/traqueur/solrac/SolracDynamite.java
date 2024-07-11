package fr.traqueur.solrac;

import fr.traqueur.solrac.dynamite.DynamiteManager;
import fr.traqueur.solrac.dynamite.exceptions.DynamiteNoExistException;
import fr.traqueur.solrac.dynamite.exceptions.DynamiteWrongMaterialException;
import fr.traqueur.solrac.dynamite.listeners.DynamiteListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SolracDynamite extends JavaPlugin {

    private DynamiteManager dynamiteManager;

    @Override
    public void onEnable() {
        if(!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }

        this.saveDefaultConfig();

        this.dynamiteManager = new DynamiteManager(this);
        try {
            this.dynamiteManager.init();
        } catch (DynamiteNoExistException | DynamiteWrongMaterialException e) {
            e.printStackTrace();
        }

        Bukkit.getPluginManager().registerEvents(new DynamiteListener(this.dynamiteManager), this);
    }

    @Override
    public void onDisable() {
        this.saveConfig();
    }
}
