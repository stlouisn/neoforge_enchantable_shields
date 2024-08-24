package com.spzla.beehivetooltip.fabric;

import net.fabricmc.api.ModInitializer;

import com.spzla.beehivetooltip.BeehiveTooltipCommon;

public final class BeehiveTooltip implements ModInitializer {

    @Override
    public void onInitialize() {
        BeehiveTooltipCommon.commonInit();
    }

}