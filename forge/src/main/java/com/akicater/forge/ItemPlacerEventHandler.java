package com.akicater.forge;

import com.akicater.ItemPlacerCommon;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "itemplacer", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ItemPlacerEventHandler {
    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ItemPlacerCommon.LAYING_ITEM_BLOCK_ENTITY.get(), layingItemBER::new);
    }
}