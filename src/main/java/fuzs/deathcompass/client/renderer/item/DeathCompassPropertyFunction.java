package fuzs.deathcompass.client.renderer.item;

import fuzs.deathcompass.world.item.DeathCompassItem;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * mostly copied from {@link net.minecraft.item.ItemModelsProperties}
 */
public class DeathCompassPropertyFunction implements IItemPropertyGetter {
    private final CompassWobble wobble = new CompassWobble();
    private final CompassWobble wobbleRandom = new CompassWobble();

    @Override
    public float call(ItemStack stack, @Nullable ClientWorld level, @Nullable LivingEntity livingEntity) {
        Entity entity = (Entity)(livingEntity != null ? livingEntity : stack.getEntityRepresentation());
        if (entity == null || !stack.hasTag()) {
            return 0.0F;
        } else {
            if (level == null && entity.level instanceof ClientWorld) {
                level = (ClientWorld)entity.level;
            }

            BlockPos blockpos = this.getLastDeathPosition(level, stack.getOrCreateTag());
            long gameTime = level.getGameTime();
            if (blockpos != null && !(entity.position().distanceToSqr((double)blockpos.getX() + 0.5D, entity.position().y(), (double)blockpos.getZ() + 0.5D) < (double)1.0E-5F)) {
                boolean flag = livingEntity instanceof PlayerEntity && ((PlayerEntity)livingEntity).isLocalPlayer();
                double d1 = 0.0D;
                if (flag) {
                    d1 = (double)livingEntity.yRot;
                } else if (entity instanceof ItemFrameEntity) {
                    d1 = this.getFrameRotation((ItemFrameEntity)entity);
                } else if (entity instanceof ItemEntity) {
                    d1 = (double)(180.0F - ((ItemEntity)entity).getSpin(0.5F) / ((float)Math.PI * 2F) * 360.0F);
                } else if (livingEntity != null) {
                    d1 = (double)livingEntity.yBodyRot;
                }

                d1 = MathHelper.positiveModulo(d1 / 360.0D, 1.0D);
                double d2 = this.getAngleTo(Vector3d.atCenterOf(blockpos), entity) / (double)((float)Math.PI * 2F);
                double d3;
                if (flag) {
                    if (this.wobble.shouldUpdate(gameTime)) {
                        this.wobble.update(gameTime, 0.5D - (d1 - 0.25D));
                    }

                    d3 = d2 + this.wobble.rotation;
                } else {
                    d3 = 0.5D - (d1 - 0.25D - d2);
                }

                return MathHelper.positiveModulo((float)d3, 1.0F);
            } else {
                if (this.wobbleRandom.shouldUpdate(gameTime)) {
                    this.wobbleRandom.update(gameTime, Math.random());
                }

                double d0 = this.wobbleRandom.rotation + (double)((float)stack.hashCode() / 2.14748365E9F);
                return MathHelper.positiveModulo((float)d0, 1.0F);
            }
        }
    }

    private int hash(int p_174670_) {
        return p_174670_ * 1327217883;
    }

    @Nullable
    private BlockPos getLastDeathPosition(World p_117916_, CompoundNBT p_117917_) {
        boolean flag = p_117917_.contains("LastDeathPos");
        boolean flag1 = p_117917_.contains("LastDeathDimension");
        if (flag && flag1) {
            Optional<RegistryKey<World>> optional = DeathCompassItem.getLastDeathDimension(p_117917_);
            if (optional.isPresent() && p_117916_.dimension() == optional.get()) {
                return NBTUtil.readBlockPos(p_117917_.getCompound("LastDeathPos"));
            }
        }
        return null;
    }

    private double getFrameRotation(ItemFrameEntity p_117914_) {
        Direction direction = p_117914_.getDirection();
        int i = direction.getAxis().isVertical() ? 90 * direction.getAxisDirection().getStep() : 0;
        return (double)MathHelper.wrapDegrees(180 + direction.get2DDataValue() * 90 + p_117914_.getRotation() * 45 + i);
    }

    private double getAngleTo(Vector3d p_117919_, Entity p_117920_) {
        return Math.atan2(p_117919_.z() - p_117920_.getZ(), p_117919_.x() - p_117920_.getX());
    }

    private static class CompassWobble {
        double rotation;
        private double deltaRotation;
        private long lastUpdateTick;

        public boolean shouldUpdate(long p_117934_) {
            return this.lastUpdateTick != p_117934_;
        }

        public void update(long p_117936_, double p_117937_) {
            this.lastUpdateTick = p_117936_;
            double d0 = p_117937_ - this.rotation;
            d0 = MathHelper.positiveModulo(d0 + 0.5D, 1.0D) - 0.5D;
            this.deltaRotation += d0 * 0.1D;
            this.deltaRotation *= 0.8D;
            this.rotation = MathHelper.positiveModulo(this.rotation + this.deltaRotation, 1.0D);
        }
    }
}
