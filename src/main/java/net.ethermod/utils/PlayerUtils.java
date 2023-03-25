package net.ethermod.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Set;

public class PlayerUtils {

    public static void teleport(Entity entity, ServerWorld serverWorld,
                                double x, double y, double z,
                                Set<PositionFlag> set,
                                float pitch, float yaw) {
        BlockPos blockPos = BlockPos.ofFloored(x, y, z);
        if (!World.isValid(blockPos)) {
            throw new IllegalArgumentException("RTP: Bad teleport location.");
        } else {
            float i = MathHelper.wrapDegrees(pitch);
            float j = MathHelper.wrapDegrees(yaw);
            if (entity.teleport(serverWorld, x, y, z, set, i, j)) {
                tag: {
                    if (entity instanceof LivingEntity livingEntity) {
                        if (livingEntity.isFallFlying()) {
                            break tag;
                        }
                    }
                    entity.setVelocity(entity.getVelocity().multiply(1.0, 0.0, 1.0));
                    entity.setOnGround(true);
                }
            }
        }
    }

}
