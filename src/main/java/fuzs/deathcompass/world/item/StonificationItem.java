package fuzs.deathcompass.world.item;

import fuzs.deathcompass.registry.ModRegistry;
import fuzs.deathcompass.world.block.entity.MobStatueBlockEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public class StonificationItem extends Item {
    public StonificationItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public boolean isFoil(ItemStack p_41453_) {
        return true;
    }

    public InteractionResult interactLivingEntity(ItemStack p_43055_, Player p_43056_, LivingEntity entity, InteractionHand p_43058_) {
        if (entity.isAlive() && !(entity instanceof WitherBoss) && !(entity instanceof EnderDragon)) {
            if (!p_43056_.level.isClientSide) {
                entity.level.setBlockAndUpdate(entity.blockPosition(), ModRegistry.MOB_STATUE_BLOCK.get().defaultBlockState());
                if (entity.level.getBlockEntity(entity.blockPosition()) instanceof MobStatueBlockEntity statueBlockEntity) {
                    statueBlockEntity.setEntity(entity);
                    entity.discard();
                }
                p_43055_.shrink(1);
            }
            return InteractionResult.sidedSuccess(p_43056_.level.isClientSide);
        }

        return InteractionResult.PASS;
    }
}
