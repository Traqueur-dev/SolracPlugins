package fr.traqueur.solrac.magicwand.utils;


import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CountdownUtils {
    private static HashMap<String, HashMap<UUID, Long>> countdowns = new HashMap<>();

    public static void createCountdown(String alias) {
        if (countdowns.containsKey(alias)) {
            return;
        }
        countdowns.put(alias, new HashMap<>());
    }

    public static void addCountdown(String alias, Player player, int seconds) {
        if (!countdowns.containsKey(alias)) {
            CountdownUtils.createCountdown(alias);
        }
        long next = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds);
        countdowns.get(alias).put(player.getUniqueId(), next);
    }

    public static String getCountdownRemaining(Player player, String name) {
        return DurationFormatter.getRemaining(CountdownUtils.getCountdownForPlayerLong(name, player), true);
    }

    public static boolean isOnCountdown(String alias, Player player) {
        if (countdowns.containsKey(alias) && countdowns.get(alias).containsKey(player.getUniqueId())
                && System.currentTimeMillis() <= countdowns.get(alias).get(player.getUniqueId())) {
            return true;
        }
        return false;
    }

    public static long getCountdownForPlayerLong(String alias, Player player) {
        return countdowns.get(alias).get(player.getUniqueId()) - System.currentTimeMillis();
    }
}
