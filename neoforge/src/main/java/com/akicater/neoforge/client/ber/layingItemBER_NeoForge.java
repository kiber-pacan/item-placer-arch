package com.akicater.neoforge.client.ber;

import com.akicater.ber.layingItemBER;
import com.akicater.blocks.layingItemBlockEntity;
import com.akicater.neoforge.client.config.ItemPlacerConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.joml.Math;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;

public class layingItemBER_NeoForge extends layingItemBER {

    ItemPlacerConfig config = AutoConfig.getConfigHolder(ItemPlacerConfig.class).getConfig();

    public layingItemBER_NeoForge(BlockEntityRendererFactory.Context ctx) {
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