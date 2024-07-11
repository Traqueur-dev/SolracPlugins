package fr.traqueur.solrac.randomtp;

import fr.traqueur.solrac.SolracRandomTP;
import fr.traqueur.solrac.randomtp.models.RandomTP;
import fr.traqueur.solrac.randomtp.utils.CountdownUtils;
import fr.traqueur.solrac.randomtp.utils.Cuboid;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class RandomTPManager {

    private SolracRandomTP plugin;

    private ArrayList<UUID> waiterRandomTP;

    private ArrayList<Material> badMaterials;

    private int RANDOMTP_COUNTDOWN;
    private int RANDOMTP_DELAY;
    private int RANDOMTP_XMIN;

    private int RANDOMTP_XMAX;

    private int RANDOMTP_ZMIN;

    private int RANDOMTP_ZMAX;

    private int RANDOMTP_LIMIT;
    private String RANDOMTP_MESSAGE;

    public RandomTPManager(SolracRandomTP plugin) {
        this.plugin = plugin;
        this.waiterRandomTP = new ArrayList<>();
        this.badMaterials = new ArrayList<>();

        this.badMaterials.add(Material.LAVA);
        this.badMaterials.add(Material.WATER);
        this.badMaterials.add(Material.FIRE);
        this.badMaterials.add(Material.CACTUS);
    }

    public void init() {
        FileConfiguration config = plugin.getConfig();

        RANDOMTP_DELAY = config.getInt("randomtp.delay");
        RANDOMTP_COUNTDOWN = config.getInt("randomtp.countdown");
        RANDOMTP_MESSAGE = config.getString("randomtp.message");
        RANDOMTP_XMIN = config.getInt("blacklist.xmin");
        RANDOMTP_XMAX = config.getInt("blacklist.xmax");
        RANDOMTP_ZMIN = config.getInt("blacklist.zmin");
        RANDOMTP_ZMAX = config.getInt("blacklist.zmax");
        RANDOMTP_LIMIT = config.getInt("blacklist.limit");

        CountdownUtils.createCountdown("RTP");
    }

    public void handleCommand(Player player) {
        if (CountdownUtils.isOnCountdown("RTP", player)) {
            String countdown = CountdownUtils.getCountdownRemaining(player, "RTP");
            String newMessage = RANDOMTP_MESSAGE.replaceFirst("%countdown%", countdown);
            player.sendMessage(Component.text(newMessage).color(NamedTextColor.RED));
            return;
        }

        Title title = Title.title(Component.text("Solrac RandomTP").color(NamedTextColor.RED),
                Component.text( "Génération en cours...").color(NamedTextColor.YELLOW));
        player.showTitle(title);

        Random random = new Random();
        World world = player.getWorld();

        int x,z;
        do {
            x = random.nextInt(RANDOMTP_LIMIT * 2) - RANDOMTP_LIMIT;
            z = random.nextInt(RANDOMTP_LIMIT * 2) - RANDOMTP_LIMIT;
        } while (!verifyLocation(player.getWorld(), x, z));

        CountdownUtils.addCountdown("RTP", player, RANDOMTP_COUNTDOWN);
        Location location = new Location(player.getWorld(), x, world.getHighestBlockYAt(x, z) + 2, z);
        waiterRandomTP.add(player.getUniqueId());
        RandomTP request = new RandomTP(this, player.getUniqueId(), location);
        request.setTaskID(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, request, 0L, 20L));
    }

    public void handleListener(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        if(from.getX() == to.getX() && from.getZ() == to.getZ() && from.getY() == to.getY()) {
            return;
        }

        this.waiterRandomTP.remove(player.getUniqueId());

    }

    private boolean verifyLocation(World world, int x, int z) {
        Cuboid cuboid = new Cuboid(world.getName(), RANDOMTP_XMIN, -64, RANDOMTP_ZMIN, RANDOMTP_XMAX, 319, RANDOMTP_ZMAX);

        int y = world.getHighestBlockYAt(x, z) + 1;
        Location location = new Location(world, x, y, z);
        Block block = location.getWorld().getBlockAt(x, y, z);
        Block below = location.getWorld().getBlockAt(x, y - 1, z);
        Block above = location.getWorld().getBlockAt(x, y + 1, z);

        boolean isSafe = !(this.badMaterials.contains(above.getType()))
                && !(this.badMaterials.contains(block.getType()))
                && !(this.badMaterials.contains(below.getType()))
                && !(above.getType().isSolid())
                && !(block.getType().isSolid())
                && below.getType().isSolid();

        return isSafe && !cuboid.contains(x,0,z);
    }

    public int getRANDOMTP_DELAY() {
        return RANDOMTP_DELAY;
    }

    public ArrayList<UUID> getWaiterRandomTP() {
        return waiterRandomTP;
    }
}
