package me.moteloff.bossapi.listener;

import me.moteloff.bossapi.BossAPI;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class TargetListener implements Listener {
    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent e) {
        LivingEntity entity = (LivingEntity) e.getEntity();
        LivingEntity target = e.getTarget();
        if (BossAPI.registeredBosses.contains(entity)) {
            
        }
    }
}
