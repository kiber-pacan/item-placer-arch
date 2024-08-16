package com.akicater.fabric;

import com.akicater.ItemPlacerCommon;
import net.fabricmc.api.ModInitializer;

public final class ItemPlacerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ItemPlacerCommon.initializeServer();
    }
}
