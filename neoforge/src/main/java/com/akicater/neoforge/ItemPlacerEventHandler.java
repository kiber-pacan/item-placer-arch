package com.akicater.neoforge;

import com.akicater.ItemPlacerCommon;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = "itemplacer", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ItemPlacerEventHandler {
    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ItemPlacerCommon.LAYING_ITEM_BLOCK_ENTITY.get(), layingItemBER::new);
    }
}