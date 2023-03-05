package me.moteloff.bossapi.utils;

import me.moteloff.bossapi.BossAPI;
import me.moteloff.bossapi.abilities.Ability;
import me.moteloff.bossapi.hologram.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class EntityBuilder {

    private final BossAPI plugin = BossAPI.getInstance();
    private EntityType entityType;
    private String displayName;
    private double health;
    private double damage = 5;
    private double armor = 5;
    private ItemStack weapon;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private double speed = 0.2;
    private Consumer<EntityDamageByEntityEvent> onHit;
    private Consumer<EntityDamageByEntityEvent> onDamaged;
    private List<Ability> abilities;
    private List<EntityDamageEvent.DamageCause> ignoredDamageType;
    private int timeBeforeSpawn = 5;
    private Location spawnLocation;
    private List<Hologram> holograms;
    private List<EntityType> targets;

    public EntityBuilder(EntityType entityType) {
        this.entityType = entityType;
    }

    /**
     * Устанавливает максимальное здоровье для LivingEntity
     *
     * @param health максимальное здоровье
     */
    public EntityBuilder setHealth(double health) {
        this.health = health;
        return this;
    }

    public EntityBuilder ignoredDamageType(EntityDamageEvent.DamageCause... ignoredDamageType) {
        this.ignoredDamageType = Arrays.asList(ignoredDamageType);
        return this;
    }

    public List<EntityDamageEvent.DamageCause> getIgnoredDamageType() {
        return this.ignoredDamageType;
    }

    public EntityBuilder targets(EntityType... targets) {
        this.targets = Arrays.asList(targets);
        return this;
    }

    public List<EntityType> getTargets() {
        return this.targets;
    }

    /**
     * Устанавливает видимое имя для LivingEntity
     *
     * @param displayName видимое имя
     */
    public EntityBuilder setDisplayName(String displayName){
        this.displayName = Formatter.translate(displayName);
        return this;
    }
    /**
     * Устанавливает время до появления LivingEntity
     *
     * @param timeBeforeSpawn промежуток времени через который появится LivingEntity, указывается в секундах
     */
    public EntityBuilder setTimeBeforeSpawn(int timeBeforeSpawn) {
        this.timeBeforeSpawn = timeBeforeSpawn;
        return this;
    }
    /**
     * Устанавливает локацию появления LivingEntity
     *
     * @param spawnLocation локация появления
     */
    public EntityBuilder setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
        return this;
    }
    /**
     * Устанавливает урон наносимый LivingEntity
     *
     * @param damage урон LivingEntity
     */
    public EntityBuilder setDamage(double damage) {
        this.damage = damage;
        return this;
    }
    /**
     * Устанавливает оружие в основной руке LivingEntity
     *
     * @param weapon предмет для основной руки
     */
    public EntityBuilder setWeapon(ItemStack weapon) {
        this.weapon = weapon;
        return this;
    }
    /**
     * Устанавливает шлем для LivingEntity
     *
     * @param helmet шлем
     */
    public EntityBuilder setHelmet(ItemStack helmet) {
        this.helmet = helmet;
        return this;
    }
    /**
     * Устанавливает нагрудник для LivingEntity
     *
     * @param chestplate нагрудник
     */
    public EntityBuilder setChestplate(ItemStack chestplate) {
        this.chestplate = chestplate;
        return this;
    }
    /**
     * Устанавливает штаны для LivingEntity
     *
     * @param leggings нагрудник
     */
    public EntityBuilder setLeggings(ItemStack leggings) {
        this.leggings = leggings;
        return this;
    }
    /**
     * Устанавливает ботинки для LivingEntity
     *
     * @param boots ботинки
     */
    public EntityBuilder setBoots(ItemStack boots) {
        this.boots = boots;
        return this;
    }
    /**
     * Устанавливает базовое значение передвижения LivingEntity
     *
     * @param speed скорость, default player speed - 0.2
     */
    public EntityBuilder setSpeed(double speed) {
        this.speed = speed;
        return this;
    }

    /**
     * Устанавливает базовое значение брони LivingEntity
     *
     * @param armor базовое значение брони
     */
    public EntityBuilder setArmor(int armor) {
        this.armor = armor;
        return this;
    }

    /**
     * Удаляет способность LivingEntity
     *
     * @param index индекс способности в List<Ability>
     */
    public EntityBuilder removeAbility(int index) {
        this.abilities.remove(index);
        return this;
    }

    /**
     * Выполняет Consumer при ударе по живому существу
     *
     * @param onHit Consumer<EntityDamageByEntityEvent>, который выполняется при ударе
     */
    public EntityBuilder onHit(Consumer<EntityDamageByEntityEvent> onHit) {
        this.onHit = onHit;
        return this;
    }
    /**
     * Выполняет Consumer при получении урона
     *
     * @param onDamaged Consumer<EntityDamageByEntityEvent>, который выполняется при получении урона
     */
    public EntityBuilder onDamaged(Consumer<EntityDamageByEntityEvent> onDamaged) {
        this.onDamaged = onDamaged;
        return this;
    }

    /**
     * Устанавливает способности для LivingEntity
     *
     * @param abilities массив класса Ability который позволяет удобно создавать способности для LivingEntity
     */
    public EntityBuilder abilities(Ability... abilities) {
        this.abilities = Arrays.asList(abilities);
        return this;
    }

    /**
     * Появление LivingEntity
     */
    public LivingEntity spawn() {
        LivingEntity entity = (LivingEntity) spawnLocation.getWorld().spawnEntity(spawnLocation, entityType);
        entity.setMaxHealth(health);
        entity.setHealth(entity.getMaxHealth());
        entity.setCustomName(displayName);
        entity.setCustomNameVisible(true);
        entity.getEquipment().setItemInMainHand(weapon);
        entity.getEquipment().setHelmet(helmet);
        entity.getEquipment().setChestplate(chestplate);
        entity.getEquipment().setLeggings(leggings);
        entity.getEquipment().setBoots(boots);
        entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(damage);
        entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
        entity.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(armor);

        BossAPI.entityMap.put(entity, this);

        if (onHit != null && onDamaged != null) {plugin.getServer().getPluginManager().registerEvents(new CustomEntityListener(onHit, onDamaged, entity, this), plugin);}

        if (abilities != null && abilities.size() >= 1) {
            plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
                for (Ability ability: this.abilities) {
                    if (ability.isReady(entity)) {
                        List<Entity> nearbyEntities = entity.getNearbyEntities(7, 5, 7);
                        for (Entity nearbyEntity : nearbyEntities) {
                            if (nearbyEntity instanceof Player) {
                                ability.use(entity);
                                break;
                            }
                        }
                    }
                }
            }, 60L, 20L);
        }
        return entity;
    }

    public int getTimeBeforeSpawn() {
        return timeBeforeSpawn;
    }

    public String  getDisplayName() {
        return displayName;
    }

    public Location  getSpawnLocation() {
        return spawnLocation;
    }

    public void setHolograms(List<Hologram> holograms) {
        this.holograms = holograms;
    }

    public List<Hologram> getHolograms() {
        return holograms;
    }
}
class CustomEntityListener implements Listener {

    private final BossAPI api = BossAPI.getInstance();
    private final Consumer<EntityDamageByEntityEvent> onHit;
    private final Consumer<EntityDamageByEntityEvent> onDamaged;
    private final LivingEntity entity;
    private final EntityBuilder builder;


    protected CustomEntityListener(Consumer<EntityDamageByEntityEvent> onHit, @Nullable Consumer<EntityDamageByEntityEvent> onDamaged, LivingEntity entity, EntityBuilder builder) {
        this.onHit = onHit;
        this.onDamaged = onDamaged;
        this.entity = entity;
        this.builder = builder;
    }

    @EventHandler
    protected void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() == entity && onHit != null) {
            onHit.accept(event);
        }
    }

    @EventHandler
    protected void onDamaged(EntityDamageByEntityEvent event) {
        if (onDamaged != null && event.getEntity() == entity) {
            onDamaged.accept(event);
            if (builder.getIgnoredDamageType() != null) {if (builder.getIgnoredDamageType().contains(event.getCause())) {event.setCancelled(true);}}
            if (event.getDamager() instanceof Player) {
                Bukkit.getScheduler().runTaskLater(api, ()->ActionBarUtil.sendActionBar((Player) event.getDamager(), Formatter.translate(BossAPI.getInstance().getConfig().getString("actionbar").replace("%cur_h%", String.valueOf((int) entity.getHealth())).replace("%boss%", entity.getCustomName()))), 2);
            }
        }
    }

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent event) {
        if (entity ==  event.getEntity()) {
            LivingEntity target = event.getTarget();
            EntityType targetType;
            if (target != null) {
                if (target.getType() != null) {
                    targetType = target.getType();
                    if (builder.getTargets() != null) {
                        if (!builder.getTargets().contains(targetType)) {event.setCancelled(true);}
                    }
                }
            }

        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (entity == event.getEntity()) {
            entity.remove();
        }
    }
}