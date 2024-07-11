package fr.traqueur.solracreplanter.replanter;

import fr.traqueur.solracreplanter.InventoryUtils;
import fr.traqueur.solracreplanter.SolracReplanter;
import fr.traqueur.solracreplanter.replanter.exceptions.ReplanterNotExistException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class ReplanterManager {

    private final SolracReplanter plugin;

    private Material PLANTER_MATERIAL;
    private int PLANTER_CUSTOMMODELDATA;

    public ReplanterManager(SolracReplanter plugin) {
        this.plugin = plugin;
    }

    public void init() throws ReplanterNotExistException {
        FileConfiguration config = this.plugin.getConfig();

        String materialName = config.getString("replanter.material");
        if (materialName == null) {
            throw new ReplanterNotExistException();
        }
        Material material = Material.getMaterial(materialName);

        if (material == null || !material.isItem()) {
            throw new ReplanterNotExistException();
        }

        if (!(new ItemStack(material).getItemMeta() instanceof Damageable)) {
            throw new ReplanterNotExistException();
        }

        PLANTER_MATERIAL = material;
        PLANTER_CUSTOMMODELDATA = config.getInt("replanter.custommodeldata");
    }
    public boolean isReplanter(ItemStack item) {
        if(item == null || item.isEmpty() || !item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        return (item.getType() == PLANTER_MATERIAL)
                && meta.hasCustomModelData()
                && (meta.getCustomModelData() == PLANTER_CUSTOMMODELDATA);
    }

    public void handleListener(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        ItemStack itemInHand = inventory.getItemInMainHand();

        if(!this.isReplanter(itemInHand)) {
            return;
        }

        if(!(block.getBlockData() instanceof Ageable ageable)) {
            return;
        }

        if(!this.isFullyGrown(ageable)) {
            return;
        }

        event.setCancelled(true);

        Material seedMaterial = ageable.getPlacementMaterial();
        Material blockMaterial = ageable.getMaterial();
        ItemStack seed = new ItemStack(seedMaterial, 1);
        this.decrementDurability(itemInHand);
        block.breakNaturally();

        if(InventoryUtils.hasItemInInventory(inventory, seedMaterial)) {
            inventory.removeItem(seed);
            block.setType(blockMaterial);
            block.setBlockData(blockMaterial.createBlockData());
        }
    }

    private boolean isFullyGrown(Ageable ageable) {
        return ageable.getAge() == ageable.getMaximumAge();
    }

    private void decrementDurability(ItemStack itemInHand) {
        Damageable wandMeta = (Damageable) itemInHand.getItemMeta();

        if(wandMeta.getDamage() + 1 <= PLANTER_MATERIAL.getMaxDurability()){
            wandMeta.setDamage(wandMeta.getDamage() + 1);
            itemInHand.setItemMeta(wandMeta);
        } else {
            itemInHand.setAmount(0);
        }
    }

}
