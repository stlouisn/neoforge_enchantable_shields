package com.spzla.beehivetooltip.fabric;

import com.spzla.beehivetooltip.BeehiveTooltipCommon;
import net.fabricmc.api.ModInitializer;

public final class BeehiveTooltip implements ModInitializer {

  @Override public void onInitialize() {
    BeehiveTooltipCommon.commonInit();
  }
}