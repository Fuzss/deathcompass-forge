package fuzs.deathcompass;

import fuzs.deathcompass.config.ServerConfig;
import fuzs.deathcompass.handler.DeathCompassHandler;
import fuzs.deathcompass.registry.ModRegistry;
import fuzs.puzzleslib.config.v2.AbstractConfig;
import fuzs.puzzleslib.config.v2.ConfigHolder;
import fuzs.puzzleslib.config.v2.ConfigHolderImpl;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(DeathCompass.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DeathCompass {
    public static final String MOD_ID = "deathcompass";
    public static final String MOD_NAME = "Death Compass";
    public static final Logger LOGGER = LogManager.getLogger(DeathCompass.MOD_NAME);

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
