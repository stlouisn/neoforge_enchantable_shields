package me.spzla.beehivetooltip.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.spzla.beehivetooltip.BeehiveTooltipClient;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> BeehiveTooltipClient.getConfig().makeScreen(parent);
    }
}
