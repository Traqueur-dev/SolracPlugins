package fr.traqueur.solrac.healthkit;

import fr.traqueur.solrac.healthkit.exceptions.HealthKitNoExistException;
import fr.traqueur.solrac.healthkit.exceptions.HealthKitWrongMaterialException;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class HealthKitManager {

    private final JavaPlugin plugin;

    private Material HEALTHKIT_MATERIAL;
    private int HEALTHKIT_CUSTOMMODELDATA;
    private double HEALTH_TO_GIVE;

    public HealthKitManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void init() throws HealthKitNoExistException, HealthKitWrongMaterialException {
        FileConfiguration config = plugin.getConfig();
        if(!config.contains("healthkit") || !config.contains("healthkit.material")
                || !config.contains("healthkit.custommodeldata") || !config.contains("healthkit.health")) {
            throw new HealthKitNoExistException();
        }
        String materialName = config.getString("healthkit.material");
        int data = config.getInt("healthkit.custommodeldata");
        Material material = Material.getMaterial(materialName.toUpperCase());
        if(material == null) {
            throw new HealthKitWrongMaterialException();
        }
        HEALTHKIT_MATERIAL = material;
        HEALTHKIT_CUSTOMMODELDATA = data;
        HEALTH_TO_GIVE = config.getDouble("healthkit.health");
    }

    public void handleInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if(!action.isRightClick()) {
            return;
        }

        if(!this.isHealthKit(item)) {
            return;
        }

        event.setCancelled(true);

        double health = player.getHealth() + HEALTH_TO_GIVE*2;
        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

        item.setAmount(0);
        player.setHealth(Math.min(health, maxHealth));
    }

    private boolean isHealthKit(ItemStack item) {
        if(item == null || item.isEmpty() || !item.hasItemMeta()) {
            return false;
        }

        ItemMeta healthMeta = item.getItemMeta();

        return (item.getType() == HEALTHKIT_MATERIAL) &&
                healthMeta.hasCustomModelData() &&
                (healthMeta.getCustomModelData() == HEALTHKIT_CUSTOMMODELDATA);
    }
}
