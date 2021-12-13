package fuzs.deathcompass.registry;

import fuzs.deathcompass.world.item.DeathCompassItem;
import fuzs.puzzleslib.registry.v2.RegistryManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

public class ModRegistry {
    private static final RegistryManager REGISTRY = RegistryManager.INSTANCE;
    public static final RegistryObject<Item> DEATH_COMPASS_ITEM = REGISTRY.registerItem("death_compass", () -> new DeathCompassItem(new Item.Properties().tab(ItemGroup.TAB_TOOLS)));

    public static void touch() {

    }
}
