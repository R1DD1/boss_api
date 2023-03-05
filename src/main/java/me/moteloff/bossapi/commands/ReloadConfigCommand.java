package me.moteloff.bossapi.commands;

import me.moteloff.bossapi.BossAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadConfigCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        BossAPI.getInstance().reloadConfig();
        return false;
    }
}
