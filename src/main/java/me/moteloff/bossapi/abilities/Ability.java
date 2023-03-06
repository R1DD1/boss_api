package me.moteloff.bossapi.abilities;

import me.moteloff.bossapi.BossAPI;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Ability {
    private final Predicate<LivingEntity> triggerCondition;
    private final Consumer<LivingEntity> action;
    private long lastUsedTime;
    private long cooldownTime;

    private Deque<LivingEntity> stack;
    private final long stackRemoveDelay = 100; // 5 секунд


    public Ability(Predicate<LivingEntity> triggerCondition, Consumer<LivingEntity> action, long cooldownTime) {
        this.triggerCondition = triggerCondition;
        this.action = action;
        this.cooldownTime = cooldownTime;
        this.lastUsedTime = System.currentTimeMillis();
        this.stack = new ArrayDeque<>();

    }
    /**
     * Установка нового значения для перезарядки способности
     *
     */
    public void setCooldownTime(long cooldownTime) {
        this.cooldownTime = cooldownTime;
    }
    /**
     * Инициализация способности, момент начала отсчета использование способности
     *
     */
    public void initUsedTime() {
        this.lastUsedTime = System.currentTimeMillis();
    }
    /**
     * Проверка на готовность к использованию способности данной сущности
     *
     * @param entity данная сущность
     */
    public boolean isReady(LivingEntity entity) {
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastUsedTime >= cooldownTime) && triggerCondition.test(entity);
    }
    /**
     * Использование способности данной сущности
     *
     * @param entity данная сущность
     */
    public void use(LivingEntity entity) {
        lastUsedTime = System.currentTimeMillis();
        if (stack.isEmpty()) {action.accept(entity);}

        stack.addLast(entity);
        BossAPI.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(BossAPI.getInstance(), () -> {
            stack.pollFirst();
        }, stackRemoveDelay);
    }
}
