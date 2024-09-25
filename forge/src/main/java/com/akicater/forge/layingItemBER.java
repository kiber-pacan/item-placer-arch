package com.akicater.forge;

import com.akicater.blocks.layingItemBlockEntity;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.*;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.util.math.MathHelper.*;

public class layingItemBER implements BlockEntityRenderer<layingItemBlockEntity> {

    ItemPlacerConfig config = AutoConfig.getConfigHolder(ItemPlacerConfig.class).getConfig();

    public static List<Quaternion> list = new ArrayList<>(
        List.of(
            Vec3f.POSITIVE_X.getDegreesQuaternion(0),   //SOUTH
            Vec3f.POSITIVE_Y.getDegreesQuaternion(180),   //NORTH
            Vec3f.POSITIVE_Y.getDegreesQuaternion(90),    //EAST
            Vec3f.NEGATIVE_Y.getDegreesQuaternion(90),    //WEST
            Vec3f.NEGATIVE_X.getDegreesQuaternion(90),    //UP
            Vec3f.POSITIVE_X.getDegreesQuaternion(90)    //DOWN
        )
    );

    public layingItemBER(BlockEntityRendererFactory.Context ctx) {
    }

    static int getLight(World world, BlockPos pos) {
        return LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, pos), world.getLightLevel(LightType.SKY, pos));
    }

    public Quaternion rotateZ(float angle, Quaternion dest) {
        float sin = MathHelper.sin(angle * 0.5f);
        float cos = MathHelper.sin(angle * 0.5f + 1.5707963267948966f);
        return new Quaternion(dest.getX() * cos + dest.getY() * sin,
            dest.getY() * cos - dest.getX() * sin,
            dest.getW() * sin + dest.getZ() * cos,
            dest.getW() * cos - dest.getZ() * sin);
    }

    public Quaternion rotateX(float angle, Quaternion dest) {
        float sin = MathHelper.sin(angle * 0.5f);
        float cos = MathHelper.sin(angle * 0.5f + 1.5707963267948966f);
        return new Quaternion(dest.getW() * sin + dest.getX() * cos,
            dest.getY() * cos + dest.getZ() * sin,
            dest.getZ() * cos - dest.getY() * sin,
            dest.getW() * cos - dest.getX() * sin);
    }

    @Override
    public void render(layingItemBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        float absoluteSize = config.absoluteSize;
        boolean oldRendering = config.oldRendering;

        float itemSize = config.tempItemSize * absoluteSize;
        float blockSize = config.tempBlockSize * absoluteSize;

        int x = getLight(entity.getWorld(), entity.getPos());

        for (int i = 0; i < 6; i++) {
            if (!entity.inventory.get(i).isEmpty()) {
                ItemStack item = entity.inventory.get(i);

                matrices.push();

                matrices.translate(entity.positions.get(i).x, entity.positions.get(i).y, entity.positions.get(i).z);

                // Differentiate item and block rendering
                if (item.getItem() instanceof BlockItem) {
                    // Differentiate new and old block rendering
                    if (!oldRendering) {
                        matrices.multiply(rotateX((float) Math.toRadians(-90), rotateZ((float) Math.toRadians(entity.rotation.list.get(i)), list.get(i))));
                        matrices.translate(0, 0.25 * blockSize - 0.025, 0);
                    } else {
                        matrices.multiply(rotateZ((float) Math.toRadians(entity.rotation.list.get(i)), list.get(i)));
                    }
                    matrices.scale(blockSize, blockSize, blockSize);
                } else {
                    matrices.scale(itemSize, itemSize, itemSize);
                    matrices.multiply(rotateZ((float) Math.toRadians(entity.rotation.list.get(i)), list.get(i)));
                }

                itemRenderer.renderItem(item, ModelTransformation.Mode.FIXED, x, overlay, matrices, vertexConsumers, 1);

                matrices.pop();
            }
        }
    }
}