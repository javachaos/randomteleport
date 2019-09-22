package net.ethermod.utils;

import io.netty.buffer.Unpooled;
import net.ethermod.RandomTeleportMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.PacketByteBuf;


public class GameUtils {

    @Environment(EnvType.CLIENT)
    public static void displayTextInGame(String text) {
        if (text != null && !text.isEmpty() && text.matches("^[0-9a-zA-Z &_.!?$%^#/,\\[\\]\\-()@]*$")) {
            MinecraftClient.getInstance().inGameHud.setOverlayMessage(text, false);
        } else {
            MinecraftClient.getInstance().inGameHud.setOverlayMessage("Mod error. In mod: " + RandomTeleportMod.MODID, false);
        }
    }

    public static void sendTextToClient(PlayerEntity p, String info) {
        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        passedData.writeString(info);
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(p,RandomTeleportMod.SEND_TOAST_TO_CLIENT_PACKET_ID, passedData);
    }
}
