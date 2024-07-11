package fr.traqueur.solrac.randomtp.commands;

import fr.traqueur.solrac.randomtp.RandomTPManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RandomTPCommand implements CommandExecutor {

    private RandomTPManager randomTPManager;

    public RandomTPCommand(RandomTPManager randomTPManager) {
        this.randomTPManager = randomTPManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            return false;
        }

        randomTPManager.handleCommand(player);

        return true;
    }
}
