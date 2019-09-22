package net.ethermod;

import net.ethermod.commands.RandomTeleportCommand;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.ethermod.utils.GameUtils;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;


public class RandomTeleportMod implements ModInitializer {

	public static final String MODID = "rtp";
	public static final Identifier SEND_TOAST_TO_CLIENT_PACKET_ID = new Identifier(MODID, "rtp_showtext");

	@Override
	public void onInitialize() {
		CommandRegistry.INSTANCE.register(false, RandomTeleportCommand::register);
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			ClientSidePacketRegistry.INSTANCE.register(SEND_TOAST_TO_CLIENT_PACKET_ID,
					(packetContext, attachedData) -> {
						String text = attachedData.readString();
						packetContext.getTaskQueue().execute(() -> GameUtils.displayTextInGame(text));
					});
		}
	}
}
