package me.moteloff.bossapi.commands;

import me.moteloff.bossapi.BossAPI;
import me.moteloff.bossapi.utils.Formatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadConfigCommand implements CommandExecutor {
    private final BossAPI plugin = BossAPI.getInstance();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        BossAPI.getInstance().reloadConfig();
        commandSender.sendMessage(Formatter.translate(plugin.getConfig().getString("messages.success_reload")));
        return false;
    }
}
