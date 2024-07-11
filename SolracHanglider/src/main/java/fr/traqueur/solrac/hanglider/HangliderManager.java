package fr.traqueur.solrac.hanglider;

import fr.traqueur.solrac.SolracHanglider;
import fr.traqueur.solrac.hanglider.exceptions.HangliderMostBeDamageableItemException;
import fr.traqueur.solrac.hanglider.exceptions.HangliderNoExistException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.UUID;

public class HangliderManager {

    private final SolracHanglider plugin;

    private final ArrayList<UUID> playersGliding;
    private Material HANGLIDER_MATERIAL;
    private int HANGLIDER_CUSTOMMODELDATA;
    private double HANGLIDER_VELOCITY;

    public HangliderManager(SolracHanglider plugin) {
        this.plugin = plugin;
        this.playersGliding = new ArrayList<>();
    }

    public void init() throws HangliderNoExistException, HangliderMostBeDamageableItemException {
        String materialName = this.plugin.getConfig().getString("hanglider.material");
        if(materialName == null) {
            throw new HangliderNoExistException();
        }
        
        Material material = Material.getMaterial(materialName);
        int data = this.plugin.getConfig().getInt("hanglider.custommodeldata");
        double velocity = this.plugin.getConfig().getDouble("hanglider.velocity");
        
        if(material == null) {
            throw new HangliderNoExistException();
        }

        if(!material.isItem()) {
            throw new HangliderMostBeDamageableItemException();
        }
        
        HANGLIDER_MATERIAL = material;
        HANGLIDER_CUSTOMMODELDATA = data;
        HANGLIDER_VELOCITY = velocity;
    }

    public boolean isHanglider(ItemStack item) {
        if(item == null || item.isEmpty() || !item.hasItemMeta()) {
            return false;
        }

        ItemMeta hangliderMeta = item.getItemMeta();

        return (item.getType() == HANGLIDER_MATERIAL) &&
                hangliderMeta.hasCustomModelData() &&
                (hangliderMeta.getCustomModelData() == HANGLIDER_CUSTOMMODELDATA);
    }

    public void decreaseDurability(ItemStack item) {
        Damageable hangliderMeta = (Damageable) item.getItemMeta();

        if(hangliderMeta.getDamage() + 1 <= HANGLIDER_MATERIAL.getMaxDurability()){
            hangliderMeta.setDamage(hangliderMeta.getDamage() + 1);
            item.setItemMeta(hangliderMeta);
        } else {
            item.setAmount(0);
        }

    }

    public void handleFlyEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventoryPlayer = player.getInventory();
        ItemStack itemInHand = inventoryPlayer.getItemInMainHand();

        if(!this.isHanglider(itemInHand)) {
            this.playersGliding.remove(player.getUniqueId());
            return;
        }

        if(player.getPitch() < 0f) {
            if(!this.playersGliding.contains(player.getUniqueId())) {
                this.playersGliding.add(player.getUniqueId());
            }
            return;
        }

        if(player.getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.AIR) {
            if(!this.playersGliding.contains(player.getUniqueId())) {
                this.playersGliding.add(player.getUniqueId());
            }

            Location playerLocation = player.getLocation();
            Vector direction = playerLocation.getDirection();
            Vector newDirection = direction.multiply(HANGLIDER_VELOCITY);

            player.setVelocity(newDirection);
            this.decreaseDurability(itemInHand);
        }

    }

    public void handleFallEvent(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL &&
                this.playersGliding.contains(player.getUniqueId())) {
            event.setCancelled(true);
            this.playersGliding.remove(player.getUniqueId());
        }
    }

    public void handleInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();

        if(this.isHanglider(item)) {
            event.setCancelled(true);
        }

    }

    public void handleAnvil(PrepareAnvilEvent event) {
        ItemStack[] items = event.getInventory().getContents();
        for(ItemStack item: items) {
            if(this.isHanglider(item)) {
                event.setResult(new ItemStack(Material.AIR));
                break;
            }
        }
    }
}
