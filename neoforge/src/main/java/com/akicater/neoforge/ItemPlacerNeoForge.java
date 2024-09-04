package com.akicater.neoforge;

import com.akicater.ItemPlacerCommon;
import com.akicater.neoforge.client.config.ClothConfigImpl;
import com.akicater.neoforge.client.config.ItemPlacerConfigNeoForge;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;

@Mod("itemplacer")
public final class ItemPlacerNeoForge {
    public ItemPlacerNeoForge() {
        ItemPlacerCommon.initializeServer();

        if (FMLLoader.getDist() == net.neoforged.api.distmarker.Dist.CLIENT) {
            ItemPlacerCommon.initializeClient();
            AutoConfig.register(ItemPlacerConfigNeoForge.class, Toml4jConfigSerializer::new);
            ClothConfigImpl.registerModsPage();
        }
    }
}
