package fr.traqueur.solracreplanter;

import fr.traqueur.solracreplanter.replanter.ReplanterManager;
import fr.traqueur.solracreplanter.replanter.exceptions.ReplanterNotExistException;
import fr.traqueur.solracreplanter.replanter.listeners.ReplanterListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SolracReplanter extends JavaPlugin {

    private ReplanterManager replanterManager;

    @Override
    public void onEnable() {
        if(this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }

        this.saveDefaultConfig();

        this.replanterManager = new ReplanterManager(this);
        try {
            this.replanterManager.init();
        } catch (ReplanterNotExistException e) {
            e.printStackTrace();
            this.getServer().shutdown();
        }

        Bukkit.getPluginManager().registerEvents(new ReplanterListener(this.replanterManager), this);
    }

    @Override
    public void onDisable() {
        this.saveConfig();
    }
}
