package com.akicater.neoforge;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "itemplacer")
public class ItemPlacerConfig implements ConfigData {
    public float absoluteSize = 0.75f;
    public float tempItemSize = 1.0f;
    public float tempBlockSize = 1.0f;
    public boolean oldRendering = false;
}