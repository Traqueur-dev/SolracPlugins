package fr.traqueur.solracreplanter;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {

    public static boolean hasItemInInventory(Inventory inv, Material material) {
        for(ItemStack item : inv.getContents()) {
            if(item == null || item.isEmpty()) {
                continue;
            }
            if(item.getType().equals(material)) {
                return true;
            }
        }
        return false;
    }

}
