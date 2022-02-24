package fuzs.deathcompass.registry;

import fuzs.deathcompass.DeathCompass;
import fuzs.deathcompass.capability.DeathTrackerCapability;
import fuzs.deathcompass.capability.DeathTrackerCapabilityImpl;
import fuzs.deathcompass.world.item.DeathCompassItem;
import fuzs.puzzleslib.capability.CapabilityController;
import fuzs.puzzleslib.capability.data.PlayerRespawnStrategy;
import fuzs.puzzleslib.registry.RegistryManager;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.registries.RegistryObject;

public class ModRegistry {
    private static final RegistryManager REGISTRY = RegistryManager.of(DeathCompass.MOD_ID);
    public static final RegistryObject<Item> DEATH_COMPASS_ITEM = REGISTRY.registerItem("death_compass", () -> new DeathCompassItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));

    private static final CapabilityController CAPABILITIES = CapabilityController.of(DeathCompass.MOD_ID);
    public static final Capability<DeathTrackerCapability> DEATH_TRACKER_CAPABILITY = CAPABILITIES.registerPlayerCapability("death_tracker", DeathTrackerCapability.class, player -> new DeathTrackerCapabilityImpl(), PlayerRespawnStrategy.NEVER, new CapabilityToken<DeathTrackerCapability>() {});

    public static void touch() {

    }
}
