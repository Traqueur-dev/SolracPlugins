package fr.traqueur.solrac.dynamite;

import fr.traqueur.solrac.SolracDynamite;
import fr.traqueur.solrac.dynamite.exceptions.DynamiteNoExistException;
import fr.traqueur.solrac.dynamite.exceptions.DynamiteWrongMaterialException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DynamiteManager {

    private final SolracDynamite plugin;

    private Material DYNAMITE_MATERIAL;
    private int DYNAMITE_CUSTOMMODELDATA;

    public DynamiteManager(SolracDynamite plugin)  {
        this.plugin = plugin;
    }

    public void init() throws DynamiteNoExistException, DynamiteWrongMaterialException {
        FileConfiguration config = plugin.getConfig();
        if(!config.contains("item") || !config.contains("item.material") || !config.contains("item.customodeldata")) {
            throw new DynamiteNoExistException();
        }

        String materialName = config.getString("item.material");
        int data = config.getInt("item.customodeldata");
        Material material = Material.getMaterial(materialName.toUpperCase());
        if(material == null) {
            throw new DynamiteWrongMaterialException();
        }
        DYNAMITE_MATERIAL = material;
        DYNAMITE_CUSTOMMODELDATA = data;
    }

    public void handleEggShoot(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();

        if(!(projectile instanceof Egg egg)) {
            return;
        }

        ItemStack itemProjectile = egg.getItem();
        int range = plugin.getConfig().getInt("explosion.range");

        if(!itemProjectile.hasItemMeta() || !itemProjectile.getType().equals(DYNAMITE_MATERIAL)) {
            return;
        }

        ItemMeta metaProjectile = itemProjectile.getItemMeta();
        if(!metaProjectile.hasCustomModelData()) {
            return;
        }

        if(metaProjectile.getCustomModelData() != DYNAMITE_CUSTOMMODELDATA) {
            return;
        }

        Location location = egg.getLocation();
        location.getWorld().createExplosion(location, range);

    }

}
