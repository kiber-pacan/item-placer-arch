package com.akicater.forge.client.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import static com.akicater.ItemPlacerCommon.MODID;

@Config(name = "itemplacer")
public class ItemPlacerConfigForge implements ConfigData {
    public float absoluteSize = 0.75f;
    public float tempItemSize = 1.0f;
    public float tempBlockSize = 1.0f;
    public boolean oldRendering = false;
}