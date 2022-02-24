package fuzs.deathcompass.handler;

import fuzs.deathcompass.DeathCompass;
import fuzs.deathcompass.capability.DeathTrackerCapability;
import fuzs.deathcompass.registry.ModRegistry;
import fuzs.deathcompass.world.item.DeathCompassItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

public class DeathCompassHandler {
    @SubscribeEvent
    public void onLivingDrops(final LivingDropsEvent evt) {
        if (evt.getEntityLiving() instanceof ServerPlayer player) {
            LazyOptional<DeathTrackerCapability> optional = player.getCapability(ModRegistry.DEATH_TRACKER_CAPABILITY);
            if (optional.isPresent()) {
                if (!evt.getDrops().isEmpty() || !DeathCompass.CONFIG.server().onlyOnItemsLost) {
                    DeathTrackerCapability.saveLastDeathData(optional.orElseThrow(IllegalStateException::new), player.blockPosition(), player.level.dimension());
                } else {
                    DeathTrackerCapability.clearLastDeathData(optional.orElseThrow(IllegalStateException::new));
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerClone(final PlayerEvent.Clone evt) {
        if (!evt.isWasDeath()) return;
        if (evt.getOriginal() instanceof ServerPlayer player) {
            player.reviveCaps();
            if (player.getCapability(ModRegistry.DEATH_TRACKER_CAPABILITY).map(DeathTrackerCapability::hasLastDeathData).orElse(false)) {
                ServerLevel world = player.getLevel();
                if (DeathCompass.CONFIG.server().ignoreKeepInventory || !world.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                    if (!DeathCompass.CONFIG.server().survivalPlayersOnly || !player.isCreative() && !player.isSpectator()) {
                        final Optional<ItemStack> deathCompass = DeathCompassItem.createDeathCompass(player);
                        deathCompass.ifPresent(itemStack -> evt.getPlayer().getInventory().add(itemStack));
                    }
                }
            }
            player.invalidateCaps();
        }
    }
}
