package me.moteloff.bossapi;

import me.moteloff.bossapi.commands.ReloadConfigCommand;
import me.moteloff.bossapi.database.DatabaseConstructor;
import me.moteloff.bossapi.hologram.Hologram;
import me.moteloff.bossapi.listener.DamageListener;
import me.moteloff.bossapi.listener.JoinListeners;
import me.moteloff.bossapi.utils.EntityBuilder;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class BossAPI extends JavaPlugin {

    private static BossAPI instance;

    public static HashMap<LivingEntity, EntityBuilder> entityMap;
    public static Set<LivingEntity> registeredBosses = new HashSet<>();
    public static HashMap<LivingEntity, HashMap<Player, Double>> damageMap;


    @Override
    public void onEnable() {
        instance = this;

        Hologram.holograms = new ArrayList<>();
        registeredBosses = new HashSet<>();
        entityMap = new HashMap<>();
        damageMap = new HashMap<>();

        getServer().getPluginManager().registerEvents(new JoinListeners(),this);
        getServer().getPluginManager().registerEvents(new DamageListener(),this);

        new DatabaseConstructor();
        getServer().getPluginCommand("reloadcfg").setExecutor(new ReloadConfigCommand());
    }

    @Override
    public void onDisable() {
        if (!Hologram.holograms.isEmpty()) {
            for (Hologram hologram: Hologram.holograms) {
                hologram.remove();
            }
        }
    }

    public static BossAPI getInstance() {
        return instance;
    }

}
