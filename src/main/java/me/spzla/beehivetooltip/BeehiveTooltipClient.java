package me.spzla.beehivetooltip;

import me.spzla.beehivetooltip.config.BeehiveTooltipConfig;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeehiveTooltipClient implements ClientModInitializer {
    public static final String MOD_ID = "beehivetooltip";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        getConfig().load();
    }

    public static BeehiveTooltipConfig getConfig() {
        return BeehiveTooltipConfig.INSTANCE;
    }
}
