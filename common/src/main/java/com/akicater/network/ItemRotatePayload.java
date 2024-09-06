
package com.akicater.network;

#if MC_VER >= V1_21
import com.akicater.blocks.layingItemBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.akicater.ItemPlacerCommon.MODID;
import static com.akicater.ItemPlacerCommon.getDirection;

public record ItemRotatePayload(BlockPos pos, float degrees, BlockHitResult hitResult) implements CustomPayload {
    public static final Id<ItemRotatePayload> ID = new Id<ItemRotatePayload>(Identifier.of(MODID, "item_rotate"));
    public static final PacketCodec<PacketByteBuf, ItemRotatePayload> CODEC = PacketCodec.of((value, buf) -> buf.writeBlockPos(value.pos).writeFloat(value.degrees).writeBlockHitResult(value.hitResult), buf -> new ItemRotatePayload(buf.readBlockPos(), buf.readFloat(), buf.readBlockHitResult()));

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
    public static void receive(PlayerEntity player, BlockPos pos, float degrees, BlockHitResult hitResult) {
        World world = player.getWorld();
        layingItemBlockEntity blockEntity;
        if ((blockEntity = (layingItemBlockEntity) world.getChunk(pos).getBlockEntity(pos)) != null) {
            blockEntity.rotate(degrees, getDirection(hitResult));
        }
    }
}
#endif
