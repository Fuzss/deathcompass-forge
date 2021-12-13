package fuzs.deathcompass.handler;

import fuzs.deathcompass.DeathCompass;
import fuzs.deathcompass.world.entity.player.PlayerDeathTracker;
import fuzs.deathcompass.world.item.DeathCompassItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.Optional;

public class DeathCompassHandler {
    public void onLivingDrops(final LivingDropsEvent evt) {
        if (evt.getEntityLiving() instanceof ServerPlayer player) {
            if (!evt.getDrops().isEmpty() || !DeathCompass.CONFIG.server().onlyOnItemsLost) {
                PlayerDeathTracker.saveLastDeathData((PlayerDeathTracker) player, player.blockPosition(), player.level.dimension());
            } else {
                PlayerDeathTracker.clearLastDeathData((PlayerDeathTracker) player);
            }
        }
    }

    public void onPlayerClone(final PlayerEvent.Clone evt) {
        if (!evt.isWasDeath()) return;
        if (evt.getOriginal() instanceof ServerPlayer player && ((PlayerDeathTracker) player).hasLastDeathData()) {
            ServerLevel world = player.getLevel();
            if (DeathCompass.CONFIG.server().ignoreKeepInventory || !world.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                if (!DeathCompass.CONFIG.server().survivalPlayersOnly || !player.isCreative() && !player.isSpectator()) {
                    final Optional<ItemStack> deathCompass = DeathCompassItem.createDeathCompass(player);
                    deathCompass.ifPresent(itemStack -> evt.getPlayer().getInventory().add(itemStack));
                }
            }
        }
    }
}
