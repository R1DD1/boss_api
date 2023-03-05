package me.moteloff.bossapi;

import me.moteloff.bossapi.hologram.Hologram;
import me.moteloff.bossapi.utils.EntityBuilder;
import me.moteloff.bossapi.utils.Formatter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static me.moteloff.bossapi.BossAPI.*;

public class BossManager {

    private static final BossAPI plugin = BossAPI.getInstance();
    public static final Map<EntityBuilder, List<Hologram>> bossesHolograms = new HashMap<>();
    private static final Map<EntityBuilder, BukkitTask> schedulerTasksMap = new HashMap<>();

    public static void register(EntityBuilder builder) {
        for (Hologram hologram : bossesHolograms.getOrDefault(builder, new ArrayList<>())) {hologram.remove();}

        if (schedulerTasksMap.containsKey(builder)) {schedulerTasksMap.get(builder).cancel();}

        bossesHolograms.put(builder, new ArrayList<>());
        Location location = builder.getSpawnLocation().clone();
        List<String> hologramLines = plugin.getConfig().getStringList("hologram");
        for (String line: hologramLines) {
            Hologram hologram = addHologram(location.subtract(0, 0.24, 0));
            List<Hologram> holograms = bossesHolograms.get(builder);
            holograms.add(hologram);
            bossesHolograms.put(builder, holograms);
        }

        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onEntityDeath(EntityDamageByEntityEvent e) {
                LivingEntity entity = (LivingEntity) e.getEntity();
                if (e.getDamager() instanceof Player) {
                    if (registeredBosses.contains(entity)){
                        if (e.getDamage() >= entity.getHealth()) {
                            register(entityMap.get(entity));
                            updateHolograms();
                        }
                    }
                }
            }
        }, BossAPI.getInstance());
        AtomicInteger time = new AtomicInteger(builder.getTimeBeforeSpawn());
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            time.getAndDecrement();
            updateTimerHologram(builder, time.get());
            if (time.get() <= 0) {
                System.out.println(Bukkit.getScheduler().getPendingTasks().size());
                LivingEntity entity = builder.spawn();
                damageMap.put(entity, new HashMap<>());
                registeredBosses.add(entity);
                for (Hologram hologram : bossesHolograms.get(builder)) {hologram.remove();}
                for (String str : plugin.getConfig().getStringList("messages.boss_spawn")) {
                    plugin.getServer().broadcastMessage(Formatter.translate(str.replace("%boss%", builder.getDisplayName())));
                }
            }
        }, 60 * 20, 60 * 20);
        schedulerTasksMap.put(builder, task);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (schedulerTasksMap.containsKey(builder)) {
                schedulerTasksMap.get(builder).cancel();
            }
        }, 20L*60*builder.getTimeBeforeSpawn());
    }

    private static Hologram addHologram(Location location) {
        return new Hologram(location, " ");
    }

    private static void updateTimerHologram(EntityBuilder builder, int time) {
        bossesHolograms.get(builder).get(1).setText(Formatter.translate(plugin.getConfig().getStringList("hologram").get(1)
                .replace("%boss%", builder.getDisplayName())
                .replace("%time%", String.valueOf(time))));
    }

    private static void updateHolograms() {
        for (EntityBuilder builder: bossesHolograms.keySet()) {
            List<Hologram> holograms = bossesHolograms.get(builder);
            List<String> hologramLines = BossAPI.getInstance().getConfig().getStringList("hologram");
            for (int i = 0; i <= 1; i++) {
                for (Player player: Bukkit.getOnlinePlayers()) {
                    holograms.get(i).setText(player, Formatter.translate(hologramLines.get(i))
                            .replace("%nick%", player.getPlayerListName())
                            .replace("%boss%", builder.getDisplayName())
                            .replace("%time%", String.valueOf(builder.getTimeBeforeSpawn())));
                }
            }
        }
    }
}
