package fuzs.deathcompass.world.block.entity;

import fuzs.deathcompass.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import org.jetbrains.annotations.Nullable;

public class MobStatueBlockEntity extends BlockEntity {
    private EntityType<?> type;
    private LivingEntity entity;

    public MobStatueBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModRegistry.MOB_STATUE_BLOCK_ENTITY_TYPE.get(), p_155229_, p_155230_);
    }

    @Override
    public void load(CompoundTag p_155245_) {
        super.load(p_155245_);
        this.type = ((ForgeRegistry<EntityType<?>>) ForgeRegistries.ENTITIES).getValue(p_155245_.getInt("MobType"));
        EntityType.create(p_155245_.getCompound("MobData"), this.getLevel()).ifPresent(e -> this.entity = (LivingEntity) e);
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_) {
        super.saveAdditional(p_187471_);
        p_187471_.putInt("MobType", ((ForgeRegistry<EntityType<?>>) ForgeRegistries.ENTITIES).getID(this.type));
        p_187471_.put("MobData", this.entity.saveWithoutId(new CompoundTag()));
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public void setEntity(LivingEntity entity) {
        this.entity = entity;
        if (this.type == null) {
            this.type = this.entity.getType();
        }
    }
}
