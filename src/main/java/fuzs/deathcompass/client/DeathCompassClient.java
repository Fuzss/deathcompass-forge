package fuzs.deathcompass.client;

import fuzs.deathcompass.DeathCompass;
import fuzs.deathcompass.client.renderer.item.DeathCompassPropertyFunction;
import fuzs.deathcompass.registry.ModRegistry;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = DeathCompass.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class DeathCompassClient {
    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent evt) {
        ItemProperties.register(ModRegistry.DEATH_COMPASS_ITEM.get(), new ResourceLocation("angle"), new DeathCompassPropertyFunction());
    }
}
