package fuzs.deathcompass.world.entity.player;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface PlayerDeathTracker {
    default boolean hasLastDeathData() {
        return this.getLastDeathPosition() != BlockPos.ZERO;
    }

    BlockPos getLastDeathPosition();

    RegistryKey<World> getLastDeathDimension();

    long getLastDeathDate();

    void setLastDeathPosition(BlockPos lastDeathPosition);

    void setLastDeathDimension(RegistryKey<World> lastDeathDimension);

    void setLastDeathDate(long lastDeathDate);

    static void saveLastDeathData(PlayerDeathTracker player, BlockPos pos, RegistryKey<World> dimension) {
        player.setLastDeathPosition(pos);
        player.setLastDeathDimension(dimension);
        player.setLastDeathDate(System.currentTimeMillis());
    }

    static void clearLastDeathData(PlayerDeathTracker player) {
        player.setLastDeathPosition(BlockPos.ZERO);
    }
}
