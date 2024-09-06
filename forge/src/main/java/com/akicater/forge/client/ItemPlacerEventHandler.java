package com.akicater.forge.client;

import com.akicater.ItemPlacerCommon;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import com.akicater.forge.client.ber.layingItemBER_Forge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "itemplacer", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ItemPlacerEventHandler {
    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ItemPlacerCommon.LAYING_ITEM_BLOCK_ENTITY.get(), layingItemBER_Forge::new);
    }
}