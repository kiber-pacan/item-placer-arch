package com.akicater.fabric.client;

import com.akicater.ber.layingItemBER;
import com.akicater.blocks.layingItemBlockEntity;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

public class layingItemBER_Fabric extends layingItemBER {

    ItemPlacerConfig config = AutoConfig.getConfigHolder(ItemPlacerConfig.class).getConfig();

    public layingItemBER_Fabric(BlockEntityRendererFactory.Context ctx) {
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