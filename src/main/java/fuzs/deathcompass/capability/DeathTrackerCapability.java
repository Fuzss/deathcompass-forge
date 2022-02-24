package fuzs.deathcompass.capability;

import fuzs.puzzleslib.capability.data.CapabilityComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public interface DeathTrackerCapability extends CapabilityComponent {
    default boolean hasLastDeathData() {
        return this.getLastDeathPosition() != BlockPos.ZERO;
    }

    BlockPos getLastDeathPosition();

    ResourceKey<Level> getLastDeathDimension();

    long getLastDeathDate();

    void setLastDeathPosition(BlockPos lastDeathPosition);

    void setLastDeathDimension(ResourceKey<Level> lastDeathDimension);

    void setLastDeathDate(long lastDeathDate);

    static void saveLastDeathData(DeathTrackerCapability capability, BlockPos pos, ResourceKey<Level> dimension) {
        capability.setLastDeathPosition(pos);
        capability.setLastDeathDimension(dimension);
        capability.setLastDeathDate(System.currentTimeMillis());
    }

    static void clearLastDeathData(DeathTrackerCapability capability) {
        capability.setLastDeathPosition(BlockPos.ZERO);
    }
}
