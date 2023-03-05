package me.moteloff.bossapi;

import me.moteloff.bossapi.hologram.Hologram;
import me.moteloff.bossapi.utils.EntityBuilder;
import me.moteloff.bossapi.utils.Formatter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfigBosses {
    private static final BossAPI plugin = BossAPI.getInstance();

    /**
     * Создает LivingEntity используя данные из конфига
     *
     * @param entityType enum значение, вариант Entity
     * @param bossName String который отвечает за секцию LivingEntity в конфиге
     */
    public static EntityBuilder getConfigBoss(EntityType entityType, String bossName) {
        ConfigurationSection bossConfig = BossAPI.getInstance().getConfig().getConfigurationSection("bosses." + bossName);

        String displayName = bossConfig.getString("display_name");
        int health = bossConfig.getInt("health");
        int damage = bossConfig.getInt("damage");
        Location spawnLocation = new Location(
                Bukkit.getWorld(Objects.requireNonNull(bossConfig.getString("spawn_location.world"))),
                bossConfig.getDouble("spawn_location.x"),
                bossConfig.getDouble("spawn_location.y"),
                bossConfig.getDouble("spawn_location.z")
        );
        int timeBeforeSpawn = bossConfig.getInt("time_before_spawn");
        EntityBuilder builder =  new EntityBuilder(entityType)
                .setDisplayName(displayName)
                .setHealth(health)
                .setDamage(damage)
                .setSpawnLocation(spawnLocation)
                .setTimeBeforeSpawn(timeBeforeSpawn);
        BossAPI.stringConfigEntityBuilder.put(bossName, builder);
        return builder;
    }





}
