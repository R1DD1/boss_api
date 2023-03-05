package me.moteloff.bossapi.listener;

import me.moteloff.bossapi.BossAPI;
import me.moteloff.bossapi.hologram.Hologram;
import me.moteloff.bossapi.utils.EntityBuilder;
import me.moteloff.bossapi.utils.Formatter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

import static me.moteloff.bossapi.BossManager.bossesHolograms;

public class JoinListeners implements Listener {

    private final BossAPI plugin = BossAPI.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        for (EntityBuilder builder: bossesHolograms.keySet()) {
            List<Hologram> holograms = bossesHolograms.get(builder);
            List<String> hologramLines = BossAPI.getInstance().getConfig().getStringList("hologram");
            for (int i = 0; i <= 1; i++) {
                holograms.get(i).setText(player, Formatter.translate(hologramLines.get(i)
                        .replace("%nick%", player.getPlayerListName())
                        .replace("%boss%", builder.getDisplayName())
                        .replace("%time%", String.valueOf(builder.getTimeBeforeSpawn()))));
            }
        }
    }
}
