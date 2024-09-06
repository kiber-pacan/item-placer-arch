package com.akicater.forge;

import com.akicater.ItemPlacerCommon;
import com.akicater.forge.client.config.ClothConfigImpl;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("itemplacer")
public final class ItemPlacerForge {

    public ItemPlacerForge() {
        EventBuses.registerModEventBus("itemplacer", FMLJavaModLoadingContext.get().getModEventBus());

        // Initialization
        ItemPlacerCommon.initializeServer();

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClothConfigImpl::registerModsPage);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ItemPlacerCommon::initializeClient);
    }
}
