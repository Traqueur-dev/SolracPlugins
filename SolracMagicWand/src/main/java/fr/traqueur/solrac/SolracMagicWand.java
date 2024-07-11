package fr.traqueur.solrac;

import fr.traqueur.solrac.magicwand.MagicWandManager;
import fr.traqueur.solrac.magicwand.exceptions.WandNotExistException;
import fr.traqueur.solrac.magicwand.listeners.MagicWandListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SolracMagicWand extends JavaPlugin {

    private MagicWandManager magicWandManager;

    @Override
    public void onEnable() {
        if(this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }

        this.saveDefaultConfig();

        this.magicWandManager = new MagicWandManager(this);
        try {
            this.magicWandManager.init();
        } catch (WandNotExistException e) {
            e.printStackTrace();
            this.getServer().shutdown();
        }

        Bukkit.getPluginManager().registerEvents(new MagicWandListener(this.magicWandManager), this);
    }

    @Override
    public void onDisable() {
        this.saveConfig();
    }
}
