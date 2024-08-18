package com.akicater.neoforge;

import com.akicater.ItemPlacerCommon;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;

@Mod("itemplacer")
public final class ItemPlacerForge {
    public ItemPlacerForge() {
        // Initialization
        ItemPlacerCommon.initializeServer();

        if (FMLLoader.getDist() == net.neoforged.api.distmarker.Dist.CLIENT) {
            ClothConfigImpl.registerModsPage();
            ItemPlacerCommon.initializeClient();
        }
    }
}
