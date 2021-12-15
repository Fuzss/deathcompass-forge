package fuzs.deathcompass.registry;

import fuzs.deathcompass.DeathCompass;
import fuzs.deathcompass.world.block.MobStatueBlock;
import fuzs.deathcompass.world.block.entity.MobStatueBlockEntity;
import fuzs.deathcompass.world.item.DeathCompassItem;
import fuzs.deathcompass.world.item.StonificationItem;
import fuzs.puzzleslib.registry.RegistryManager;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.RegistryObject;

public class ModRegistry {
    private static final RegistryManager REGISTRY = RegistryManager.of(DeathCompass.MOD_ID);
    public static final RegistryObject<Item> DEATH_COMPASS_ITEM = REGISTRY.registerItem("death_compass", () -> new DeathCompassItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> STONIFICATION_ITEM = REGISTRY.registerItem("stonifier", () -> new StonificationItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Block> MOB_STATUE_BLOCK = REGISTRY.registerBlockWithItem("mob_statue", () -> new MobStatueBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)), CreativeModeTab.TAB_DECORATIONS);
    public static final RegistryObject<BlockEntityType<MobStatueBlockEntity>> MOB_STATUE_BLOCK_ENTITY_TYPE = REGISTRY.registerRawBlockEntityType("mob_statue", () -> BlockEntityType.Builder.of(MobStatueBlockEntity::new, MOB_STATUE_BLOCK.get()));

    public static void touch() {

    }
}
