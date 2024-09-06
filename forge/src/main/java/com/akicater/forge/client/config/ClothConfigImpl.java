package com.akicater.forge.client.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import me.shedaniel.clothconfig2.ClothConfigDemo;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

public class ClothConfigImpl {
    public static void registerModsPage() {
        #if MC_VER != V1_20_4
        AutoConfig.register(ItemPlacerConfigForge.class, Toml4jConfigSerializer::new);
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> {
            return AutoConfig.getConfigScreen(ItemPlacerConfigForge.class, parent).get();
        }));
        #endif
    }
}
