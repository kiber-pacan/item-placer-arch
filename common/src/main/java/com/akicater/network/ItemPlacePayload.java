
package com.akicater.network;

#if MC_VER >= V1_21
import com.akicater.ItemPlacerCommon;
import com.akicater.blocks.layingItemBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import static com.akicater.ItemPlacerCommon.MODID;

public record ItemPlacePayload(BlockPos pos, BlockHitResult hitResult) implements CustomPayload {
    public static final Id<ItemPlacePayload> ID = new Id<ItemPlacePayload>(Identifier.of(MODID, "item_place"));
    public static final PacketCodec<PacketByteBuf, ItemPlacePayload> CODEC = PacketCodec.of((value, buf) -> buf.writeBlockPos(value.pos).writeBlockHitResult(value.hitResult), buf -> new ItemPlacePayload(buf.readBlockPos(), buf.readBlockHitResult()));

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
    public static void receive(PlayerEntity player, BlockPos pos, BlockHitResult hitResult) {
        ItemStack stack = player.getMainHandStack();
        World world = player.getWorld();
        if (world.getBlockState(pos).getBlock() == Blocks.AIR || world.getBlockState(pos).getBlock() == Blocks.WATER) {
            player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
            Direction dir = hitResult.getSide().getOpposite();
            BlockState state = ItemPlacerCommon.LAYING_ITEM.get().getDefaultState();
            if (world.getBlockState(pos).getBlock() == Blocks.WATER) {
                state = state.with(Properties.WATERLOGGED, true);
            }
            world.setBlockState(pos, state);
            state.initShapeCache();
            layingItemBlockEntity blockEntity = (layingItemBlockEntity)world.getChunk(pos).getBlockEntity(pos);
            if (blockEntity != null) {
                int i = ItemPlacerCommon.dirToInt(dir);
                blockEntity.inventory.set(i, stack);
                world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                blockEntity.markDirty();
            }
        } else if (world.getBlockState(pos).getBlock() == ItemPlacerCommon.LAYING_ITEM.get()) {
            Direction dir = hitResult.getSide().getOpposite();
            layingItemBlockEntity blockEntity = (layingItemBlockEntity)world.getChunk(pos).getBlockEntity(pos);
            if (blockEntity != null) {
                int i = ItemPlacerCommon.dirToInt(dir);
                if(blockEntity.inventory.get(i).isEmpty()) {
                    player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                    blockEntity.inventory.set(i, stack);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                    blockEntity.markDirty();
                }
            }
        }
    }
}
#endif