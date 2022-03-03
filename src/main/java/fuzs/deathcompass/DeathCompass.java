package fuzs.deathcompass;

import fuzs.deathcompass.config.ServerConfig;
import fuzs.deathcompass.handler.DeathCompassHandler;
import fuzs.deathcompass.registry.ModRegistry;
import fuzs.puzzleslib.config.AbstractConfig;
import fuzs.puzzleslib.config.ConfigHolder;
import fuzs.puzzleslib.config.ConfigHolderImpl;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(DeathCompass.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DeathCompass {
    public static final String MOD_ID = "deathcompass";
    public static final String MOD_NAME = "Death Compass";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @SuppressWarnings("Convert2MethodRef")
    public static final ConfigHolder<AbstractConfig, ServerConfig> CONFIG = ConfigHolder.server(() -> new ServerConfig());

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ((ConfigHolderImpl<?, ?>) CONFIG).addConfigs(MOD_ID);
        ModRegistry.touch();
        registerHandlers();
    }

    private static void registerHandlers() {
        final DeathCompassHandler handler = new DeathCompassHandler();
        MinecraftForge.EVENT_BUS.addListener(handler::onLivingDrops);
        MinecraftForge.EVENT_BUS.addListener(handler::onPlayerClone);
    }
}
