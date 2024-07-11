package fr.traqueur.solrac.magicwand;

import fr.traqueur.solrac.SolracMagicWand;
import fr.traqueur.solrac.magicwand.exceptions.WandNotExistException;
import fr.traqueur.solrac.magicwand.utils.CountdownUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class MagicWandManager {

    private final SolracMagicWand plugin;

    private Material WAND_MATERIAL;
    private int WAND_CUSTOMMODELDATA;
    private int WAND_DISTANCE;
    private int WAND_DELAY;
    private  int WAND_TICKS;
    private String WAND_DELAY_MESSAGE;

    public MagicWandManager(SolracMagicWand plugin) {
        this.plugin = plugin;
    }

    public void init() throws WandNotExistException {
        FileConfiguration config = this.plugin.getConfig();

        String materialName = config.getString("itemwand.material");
        if(materialName == null) {
            throw new WandNotExistException();
        }
        Material material = Material.getMaterial(materialName);

        if(material == null || !material.isItem()) {
            throw new WandNotExistException();
        }

        if(!(new ItemStack(material).getItemMeta() instanceof Damageable)) {
            throw new WandNotExistException();
        }

        WAND_MATERIAL = material;
        WAND_CUSTOMMODELDATA = config.getInt("itemwand.custommodeldata");
        WAND_DISTANCE = config.getInt("itemwand.distance");
        WAND_DELAY = config.getInt("itemwand.delay");
        WAND_TICKS = config.getInt("itemwand.ticks");
        WAND_DELAY_MESSAGE = config.getString("itemwand.delay_message");}

    public boolean isWand(ItemStack item) {
        if(item == null || item.isEmpty() || !item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        return (item.getType() == WAND_MATERIAL)
                && meta.hasCustomModelData()
                && (meta.getCustomModelData() == WAND_CUSTOMMODELDATA);
    }

    public void handleListener(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        ItemStack itemInHand = inventory.getItemInMainHand();
        Action action = event.getAction();

        if(!action.isRightClick()) {
            return;
        }

        if(!this.isWand(itemInHand)) {
            return;
        }


        event.setCancelled(true);

        if(CountdownUtils.isOnCountdown("magicwand", player)) {
            String remaining = CountdownUtils.getCountdownRemaining(player, "magicwand");
            String message = WAND_DELAY_MESSAGE.replace("%countdown%", remaining);
            player.sendMessage(Component.text(message).color(NamedTextColor.RED));
            return;
        }

        Location eyeLocation = player.getEyeLocation().clone();
        Vector eyeDirection = eyeLocation.getDirection();
        eyeLocation.setYaw(player.getEyeLocation().getYaw() + 90);
        eyeLocation.setPitch(0);
        Location handLoc = player.getLocation().clone().add(0, 1, 0);
        handLoc.add(eyeDirection.normalize().multiply(0.2));
        RayTraceResult ray = player.getWorld().rayTrace(handLoc, eyeDirection, WAND_DISTANCE, FluidCollisionMode.ALWAYS, false, 0.2, (e -> !e.equals(player)));
        if (ray != null && ray.getHitEntity() != null) {
            Entity entity = ray.getHitEntity();
            entity.setFireTicks(WAND_TICKS * 20);
        }
        this.decrementDurability(itemInHand);
        this.drawLine(handLoc, player.getEyeLocation().clone().add(eyeDirection.normalize().multiply(WAND_DISTANCE)), 0.1);
        CountdownUtils.addCountdown("magicwand", player, WAND_DELAY);
    }

    private void decrementDurability(ItemStack itemInHand) {
        Damageable wandMeta = (Damageable) itemInHand.getItemMeta();

        if(wandMeta.getDamage() + 1 <= WAND_MATERIAL.getMaxDurability()){
            wandMeta.setDamage(wandMeta.getDamage() + 1);
            itemInHand.setItemMeta(wandMeta);
        } else {
            itemInHand.setAmount(0);
        }
    }

    public void drawLine(Location point1, Location point2, double space) {
        World world = point1.getWorld();
        double distance = point1.distance(point2);
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
        double length = 0;
        for (; length < distance; p1.add(vector)) {
            Particle.DustOptions options = new Particle.DustOptions(Color.RED, 1);
            world.spawnParticle(Particle.REDSTONE, p1.getX(), p1.getY(), p1.getZ(), 1, 0, 0, 0, options);
            length += space;
        }
    }

}
