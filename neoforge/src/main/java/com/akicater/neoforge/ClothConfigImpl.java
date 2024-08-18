package com.akicater.neoforge;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class ClothConfigImpl {
    public static void registerModsPage() {
        AutoConfig.register(ItemPlacerConfig.class, Toml4jConfigSerializer::new);
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () ->
                (client, parent) -> {
                        return AutoConfig.getConfigScreen(ItemPlacerConfig.class, parent).get();
                });
    }
}
