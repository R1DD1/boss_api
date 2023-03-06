package me.moteloff.bossapi.hologram;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.*;

public class Hologram {

    private final Location location;
    private final ArrayList<Player> players;
    private final Map<Player, String> textMap = new HashMap<>();
    private final ArmorStand armorStand;
    public static ArrayList<Hologram> holograms;

    public Hologram(Location location, String text) {
        this.location = location;
        this.players = new ArrayList<>();
        this.armorStand = location.getWorld().spawn(location, ArmorStand.class);

        armorStand.setGravity(false);
        armorStand.setCanPickupItems(false);
        armorStand.setVisible(false);
        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName(text);
        holograms.add(this);

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(((CraftArmorStand) armorStand).getHandle());
        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }
    /**
     * Отправка пакета игроку
     */
    public void addPlayer(Player player) {
        players.add(player);
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(((CraftArmorStand) armorStand).getHandle());
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

    }
    /**
     * Отправка пакета с названием арморстэнда всем игрокам этой голограммы
     */
    public void setText(String text) {
        EntityArmorStand armorStand = ((CraftArmorStand) this.armorStand).getHandle();

        IChatBaseComponent displayName = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}");

        armorStand.setCustomName(displayName);
        armorStand.setCustomNameVisible(true);

        for (Player player : players) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutEntityMetadata(armorStand.getId(), armorStand.getDataWatcher(), true));
        }
        this.armorStand.setVisible(false);
    }
    /**
     * Отправка пакета с названием арморстэнда одному игроку этой голограммы
     */
    public void setText(Player player, String text) {
        EntityArmorStand armorStand = ((CraftArmorStand) this.armorStand).getHandle();

        IChatBaseComponent displayName = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}");

        armorStand.setCustomName(displayName);
        armorStand.setCustomNameVisible(true);

        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutEntityMetadata(armorStand.getId(), armorStand.getDataWatcher(), true));
        this.armorStand.setVisible(false);
    }
    /**
     * Отправка пакета с удалением арморстэнда всем игрокам этой голограммы
     */
    public void remove() {
        // Удаляем голограмму для каждого игрока
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(((CraftArmorStand) armorStand).getHandle().getId());
        for (Player player : players) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }

        // Удаляем голограмму из мира
        armorStand.remove();
    }

}