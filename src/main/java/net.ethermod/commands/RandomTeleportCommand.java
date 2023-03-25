package net.ethermod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.ethermod.utils.PlayerUtils;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import java.util.EnumSet;
import java.util.Objects;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;

public class RandomTeleportCommand {

    private static int execNoArgs(CommandContext<ServerCommandSource> ctx) {
        PlayerEntity p = ctx.getSource().getPlayer();
        ServerWorld w = ctx.getSource().getWorld();
        if (p == null) {
            ctx.getSource().sendMessage(Text.literal("Player null."));
            return 0;
        } else {
            Random r = p.getRandom();
            if (r == null) {
                ctx.getSource().sendMessage(Text.literal("Random function null."));
                return 0;
            }
            double delta_x = 10000 * r.nextGaussian() + 1;
            double delta_z = 10000 * r.nextGaussian() + 1;
            double new_x = delta_x + p.getX();
            double new_z = delta_z + p.getZ();
            double new_y = 0;
            int i = w.getSeaLevel();
            while (!w.isAir(new BlockPos((int) new_x, i++, (int) new_z)) && new_y + w.getSeaLevel() < w.getHeight()) {
                new_y++;
            }
            if (w.isWater(new BlockPos((int) new_x, i - 2, (int) new_z))) {
                return execNoArgs(ctx);
            }
            PlayerUtils.teleport(p, w, new_x, i, new_z, EnumSet.noneOf(PositionFlag.class), p.getYaw(), p.getPitch());
            String info = "Default radius set to 10000, spawning " +
                    p.getDisplayName().getString() +
                    " at [" + (int) new_x + "] [" + (int) p.getY() + "] [" + (int) new_z + "]";
            ctx.getSource().sendMessage(Text.literal(info));
            return 1;
        }
    }

    private static int execArgs(CommandContext<ServerCommandSource> ctx) {
        PlayerEntity p = ctx.getSource().getPlayer();
        ServerWorld w = ctx.getSource().getWorld();
        if (p == null) {
            ctx.getSource().sendMessage(Text.literal("Player null."));
            return 0;
        } else {
            int r = getInteger(ctx, "radius");
            Random rand = p.getRandom();
            if (rand == null) {
                ctx.getSource().sendMessage(Text.literal("Random function null."));
                return 0;
            }
            double delta_x = r * rand.nextGaussian() + 1;
            double delta_z = r * rand.nextGaussian() + 1;
            double new_x = delta_x + p.getX();
            double new_z = delta_z + p.getZ();
            double new_y = 0;
            int i = w.getSeaLevel();
            while (!w.isAir(new BlockPos((int) new_x, i++, (int) new_z)) && new_y + w.getSeaLevel() < w.getHeight()) {
                new_y++;
            }
            if (w.isWater(new BlockPos((int) new_x, i - 2, (int) new_z))) {
                return execArgs(ctx);
            }
            PlayerUtils.teleport(p, w, new_x, i, new_z, EnumSet.noneOf(PositionFlag.class), p.getYaw(), p.getPitch());
            String info = "Radius set to " + r + ", spawning " + p.getDisplayName().getString() +
                    " at [" + (int) new_x + "] [" + (int) p.getY() + "] [" + (int) new_z + "]";
            ctx.getSource().sendMessage(Text.literal(info));
            return 1;
        }
    }

    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("rtp")
                .then(CommandManager.argument("radius", integer(-1000000000,1000000000))
                        .executes(RandomTeleportCommand::execArgs))
                .executes(RandomTeleportCommand::execNoArgs)
        );
    }
}
