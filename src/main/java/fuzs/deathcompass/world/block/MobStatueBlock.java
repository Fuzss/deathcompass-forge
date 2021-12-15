package fuzs.deathcompass.world.block;

import fuzs.deathcompass.DeathCompass;
import fuzs.deathcompass.world.block.entity.MobStatueBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class MobStatueBlock extends BaseEntityBlock {
    public MobStatueBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new MobStatueBlockEntity(p_153215_, p_153216_);
    }

    public InteractionResult use(BlockState p_49722_, Level level, BlockPos pos, Player p_49725_, InteractionHand p_49726_, BlockHitResult p_49727_) {
        if (level.getBlockEntity(pos) instanceof MobStatueBlockEntity statueBlockEntity) {
            DeathCompass.LOGGER.info(statueBlockEntity.getEntity() != null ? statueBlockEntity.getEntity().getType() : "null entity");
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
}
