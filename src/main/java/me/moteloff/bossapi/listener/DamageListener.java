package me.moteloff.bossapi.listener;

import me.moteloff.bossapi.BossAPI;
import me.moteloff.bossapi.database.DatabaseConstructor;
import me.moteloff.bossapi.utils.ActionBarUtil;
import me.moteloff.bossapi.utils.Formatter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.time.LocalDateTime;
import java.util.*;

public class DamageListener implements Listener {

    private final BossAPI plugin = BossAPI.getInstance();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        LivingEntity entity = (LivingEntity) e.getEntity();
        if (BossAPI.registeredBosses.contains(entity)) {
            if (e.getDamager() instanceof Player) {
                Player player = ((Player) e.getDamager()).getPlayer();

                addDamage(entity, player, e.getFinalDamage());
            }
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        LivingEntity entity = (LivingEntity) e.getEntity();
        if (BossAPI.registeredBosses.contains(entity)) {
            LocalDateTime currentTime = LocalDateTime.now();
            String db = "[{player:\"%nick1%\", damage:\"%damage1%\"},{player:\"%nick2%\", damage:\"%damage2%\"},{player:\"%nick3%\", damage:\"%damage3%\"}]";
            List<Player> topDamagers = getTopDamageDealers(3, entity);
            HashMap<Player, Double> subDamageMap = BossAPI.damageMap.get(entity);
            List<String> messages = plugin.getConfig().getStringList("messages.boss_kill");
            Bukkit.broadcastMessage(Formatter.translate(messages.get(0).replace("%display_name%", entity.getCustomName())));
            Bukkit.broadcastMessage(Formatter.translate(messages.get(1).replace("%display_name%", entity.getCustomName())));
            if (topDamagers.size() >= 1) {
                Bukkit.broadcastMessage(Formatter.translate(messages.get(2).replace("%player_top1%", topDamagers.get(0).getPlayerListName()).replace("%damage%", String.valueOf(subDamageMap.get(topDamagers.get(0))))));
                db = db.replace("%nick1%", topDamagers.get(0).getPlayerListName()).replace("%damage1%", String.valueOf(subDamageMap.get(topDamagers.get(0))));
            }
            if (topDamagers.size() >= 2) {
                Bukkit.broadcastMessage(Formatter.translate(messages.get(3).replace("%player_top2%", topDamagers.get(1).getPlayerListName()).replace("%damage%", String.valueOf(subDamageMap.get(topDamagers.get(1))))));
                db =db.replace("%nick2%", topDamagers.get(1).getPlayerListName()).replace("%damage2%", String.valueOf(subDamageMap.get(topDamagers.get(1))));
            }
            if (topDamagers.size() == 3) {
                Bukkit.broadcastMessage(Formatter.translate(messages.get(4).replace("%player_top3%", topDamagers.get(2).getPlayerListName()).replace("%damage%", String.valueOf(subDamageMap.get(topDamagers.get(2))))));
                db = db.replace("%nick3%", topDamagers.get(2).getPlayerListName()).replace("%damage3%", String.valueOf(subDamageMap.get(topDamagers.get(2))));
            }

            db = db.replace(",{player:\"%nick2%\", damage:\"%damage2%\"}", "").replace(",{player:\"%nick3%\", damage:\"%damage3%\"}", "");
            DatabaseConstructor.insertData(entity.getCustomName(), String.valueOf(currentTime), db);
        }
    }

    public List<Player> getTopDamageDealers(int count, LivingEntity entity) {
        Set<Map.Entry<Player, Double>> entries = BossAPI.damageMap.get(entity).entrySet();

        List<Player> topDamageDealers = new ArrayList<>();

        List<Map.Entry<Player, Double>> sortedEntries = entries.stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).toList();

        for (Map.Entry<Player, Double> entry : sortedEntries) {
            if (topDamageDealers.size() < count) {
                topDamageDealers.add(entry.getKey());
            } else {
                break;
            }
        }
        return topDamageDealers;
    }

    public void addDamage(LivingEntity entity, Player player, double damage) {
        HashMap<Player, Double> damageMap = BossAPI.damageMap.getOrDefault(entity, new HashMap<>());
        double currentDamage = damageMap.getOrDefault(player, 0.0);
        damageMap.put(player, currentDamage + damage);
        BossAPI.damageMap.put(entity, damageMap);
    }

}
