package me.moteloff.bossapi.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class ActionBarUtil {

    public static void sendActionBar(Player player, String message) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager()
                .createPacket(PacketType.Play.Server.CHAT);
        packet.getChatTypes().write(0, EnumWrappers.ChatType.GAME_INFO);
        packet.getChatComponents().write(0,
                WrappedChatComponent.fromJson(
                        ChatColor.translateAlternateColorCodes('&', "{\"text\":\"" + message + "\"}")));
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
