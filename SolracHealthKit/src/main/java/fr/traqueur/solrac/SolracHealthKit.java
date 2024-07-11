package fr.traqueur.solrac;

import fr.traqueur.solrac.healthkit.HealthKitManager;
import fr.traqueur.solrac.healthkit.exceptions.HealthKitNoExistException;
import fr.traqueur.solrac.healthkit.exceptions.HealthKitWrongMaterialException;
import fr.traqueur.solrac.healthkit.listeners.HealthKitListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SolracHealthKit extends JavaPlugin {

    private HealthKitManager healthKitManager;

    @Override
    public void onEnable() {
        if(!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }

        this.saveDefaultConfig();

        this.healthKitManager = new HealthKitManager(this);
        try {
            this.healthKitManager.init();
        } catch (HealthKitNoExistException | HealthKitWrongMaterialException e) {
            e.printStackTrace();
        }

        Bukkit.getPluginManager().registerEvents(new HealthKitListener(this.healthKitManager), this);

    }

    @Override
    public void onDisable() {
        this.saveConfig();
    }
}
