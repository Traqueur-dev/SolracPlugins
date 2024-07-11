package fr.traqueur.solrac;

import fr.traqueur.solrac.hanglider.HangliderManager;
import fr.traqueur.solrac.hanglider.exceptions.HangliderMostBeDamageableItemException;
import fr.traqueur.solrac.hanglider.exceptions.HangliderNoExistException;
import fr.traqueur.solrac.hanglider.listeners.HangliderListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SolracHanglider extends JavaPlugin {

    private HangliderManager hangliderManager;

    @Override
    public void onEnable() {

        if(!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }

        this.saveDefaultConfig();

        this.hangliderManager = new HangliderManager(this);
        try {
            this.hangliderManager.init();
        } catch (HangliderNoExistException | HangliderMostBeDamageableItemException e) {
            e.printStackTrace();
            this.getServer().shutdown();
        }

        Bukkit.getPluginManager().registerEvents(new HangliderListener(hangliderManager), this);

    }

    @Override
    public void onDisable() {
        this.saveConfig();
    }
}
