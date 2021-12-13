package fuzs.deathcompass.config;

import fuzs.puzzleslib.config.AbstractConfig;
import fuzs.puzzleslib.config.annotation.Config;

public class ServerConfig extends AbstractConfig {
    @Config(description = "Give death compass even when gamerule \"keepInventory\" is enabled.")
    public boolean ignoreKeepInventory = true;
    @Config(description = "Give death compass only to players that dropped items from their inventory upon dying.")
    public boolean onlyOnItemsLost = false;
    @Config(description = "Give death compass only to survival players that died.")
    public boolean survivalPlayersOnly = true;

    public ServerConfig() {
        super("");
    }
}
