package com.akicater.fabric.client;

import com.akicater.ItemPlacerCommon;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public final class ItemPlacerFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemPlacerCommon.initializeClient();

        AutoConfig.register(ItemPlacerConfig.class, Toml4jConfigSerializer::new);
        BlockEntityRendererFactories.register(ItemPlacerCommon.LAYING_ITEM_BLOCK_ENTITY.get(), layingItemBER_Fabric::new);
    }
}
