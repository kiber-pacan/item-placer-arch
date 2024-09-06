package com.akicater.neoforge.client;

import com.akicater.ItemPlacerCommon;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import com.akicater.neoforge.client.ber.layingItemBER_NeoForge;
#if MC_VER >= V1_21
    import net.neoforged.fml.common.EventBusSubscriber;
#else
    import net.neoforged.fml.common.Mod;
#endif

import net.neoforged.neoforge.client.event.EntityRenderersEvent;


#if MC_VER >= V1_21
    @EventBusSubscriber(modid = "itemplacer", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
#else
    @Mod.EventBusSubscriber(modid = "itemplacer", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
#endif
public class ItemPlacerEventHandler {
    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ItemPlacerCommon.LAYING_ITEM_BLOCK_ENTITY.get(), layingItemBER_NeoForge::new);
    }
}