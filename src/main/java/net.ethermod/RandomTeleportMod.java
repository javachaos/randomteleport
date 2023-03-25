package net.ethermod;

import net.ethermod.commands.RandomTeleportCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;


public class RandomTeleportMod implements ModInitializer {

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(RandomTeleportCommand::register);
	}
}
