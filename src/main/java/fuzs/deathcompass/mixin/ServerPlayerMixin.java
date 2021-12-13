package fuzs.deathcompass.mixin;

import com.mojang.authlib.GameProfile;
import fuzs.deathcompass.world.entity.player.PlayerDeathTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerMixin extends PlayerEntity implements PlayerDeathTracker {
    @Unique
    private BlockPos lastDeathPosition = BlockPos.ZERO;
    @Unique
    private RegistryKey<World> lastDeathDimension = World.OVERWORLD;
    @Unique
    private long lastDeathDate;

    public ServerPlayerMixin(World p_36114_, BlockPos p_36115_, float p_36116_, GameProfile p_36117_) {
        super(p_36114_, p_36115_, p_36116_, p_36117_);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readAdditionalSaveData(CompoundNBT p_36215_, CallbackInfo callbackInfo) {
        if (p_36215_.contains("LastDeathX", 99) && p_36215_.contains("LastDeathY", 99) && p_36215_.contains("LastDeathZ", 99)) {
            this.lastDeathPosition = new BlockPos(p_36215_.getInt("LastDeathX"), p_36215_.getInt("LastDeathY"), p_36215_.getInt("LastDeathZ"));
            if (p_36215_.contains("LastDeathDimension")) {
                this.lastDeathDimension = World.RESOURCE_KEY_CODEC.parse(NBTDynamicOps.INSTANCE, p_36215_.get("LastDeathDimension")).resultOrPartial(LOGGER::error).orElse(World.OVERWORLD);
            }
            if (p_36215_.contains("LastDeathDate")) {
                this.lastDeathDate = p_36215_.getLong("LastDeathDate");
            }
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addAdditionalSaveData(CompoundNBT p_36265_, CallbackInfo callbackInfo) {
        if (this.lastDeathPosition != BlockPos.ZERO) {
            p_36265_.putInt("LastDeathX", this.lastDeathPosition.getX());
            p_36265_.putInt("LastDeathY", this.lastDeathPosition.getY());
            p_36265_.putInt("LastDeathZ", this.lastDeathPosition.getZ());
            ResourceLocation.CODEC.encodeStart(NBTDynamicOps.INSTANCE, this.lastDeathDimension.location()).resultOrPartial(LOGGER::error).ifPresent((p_9134_) -> {
                p_36265_.put("LastDeathDimension", p_9134_);
            });
            p_36265_.putLong("LastDeathDate", this.lastDeathDate);
        }
    }

    @Override
    public BlockPos getLastDeathPosition() {
        return this.lastDeathPosition;
    }

    @Override
    public RegistryKey<World> getLastDeathDimension() {
        return this.lastDeathDimension;
    }

    @Override
    public long getLastDeathDate() {
        return this.lastDeathDate;
    }

    @Override
    public void setLastDeathPosition(BlockPos lastDeathPosition) {
        this.lastDeathPosition = lastDeathPosition;
    }

    @Override
    public void setLastDeathDimension(RegistryKey<World> lastDeathDimension) {
        this.lastDeathDimension = lastDeathDimension;
    }

    @Override
    public void setLastDeathDate(long lastDeathDate) {
        this.lastDeathDate = lastDeathDate;
    }
}