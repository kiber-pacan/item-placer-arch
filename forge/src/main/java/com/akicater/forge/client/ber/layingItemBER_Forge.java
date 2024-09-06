package com.akicater.forge.client.ber;

import com.akicater.ber.layingItemBER;
import com.akicater.blocks.layingItemBlockEntity;
import com.akicater.forge.client.config.ItemPlacerConfigForge;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

public class layingItemBER_Forge extends layingItemBER {

    ItemPlacerConfigForge config = AutoConfig.getConfigHolder(ItemPlacerConfigForge.class).getConfig();

    public layingItemBER_Forge(BlockEntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(layingItemBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        float absoluteSize = config.absoluteSize;
        float itemSize = config.tempItemSize * absoluteSize;
        float blockSize = config.tempBlockSize * absoluteSize;

        boolean oldRendering = config.oldRendering;

        render(entity, tickDelta, matrices, vertexConsumers, light, overlay, absoluteSize, blockSize, itemSize, oldRendering);
    }
}