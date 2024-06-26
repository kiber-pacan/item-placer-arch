package com.akicater.blocks;

import com.akicater.ItemPlacer;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.StairShape;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static com.akicater.ItemPlacer.getDirection;

public class layingItem extends Block implements BlockEntityProvider {

    public layingItem(Settings settings) {
        super(settings);

    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new layingItemBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        layingItemBlockEntity blockEntity = (layingItemBlockEntity)world.getChunk(pos).getBlockEntity(pos);
        if (blockEntity != null) {
            blockEntity.dropItem(getDirection(hit));
            if (isInventoryClear(blockEntity.inventory)) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
            }
            world.playSound((double)pos.getX(),(double)pos.getY(),(double)pos.getZ(), SoundEvents.ENTITY_PAINTING_PLACE, SoundCategory.BLOCKS,1f,2f,true);
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    Boolean isInventoryClear(DefaultedList<ItemStack> inventory) {
        for (ItemStack itemStack : inventory) {
            if (!ItemStack.EMPTY.equals(itemStack)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            if (world.getBlockEntity(pos) instanceof layingItemBlockEntity entity) {
                for (int i = 0; i < 6; i++) {

                    ItemStack itemStack = entity.getStack(i);

                    ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);

                    world.updateComparators(pos, this);
                }
                entity.clear();
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView blockView, BlockPos pos, ShapeContext context) {
        layingItemBlockEntity entity = (layingItemBlockEntity) blockView.getBlockEntity(pos);
        List<VoxelShape> tempShape = new ArrayList<>();
        if (entity != null) {
            if (entity.directions.list.get(0)) {
                tempShape.add(VoxelShapes.cuboid(0.125f, 0.125f, 0.875f, 0.875f, 0.875f, 1.0f));
            }
            if (entity.directions.list.get(1)) {
                tempShape.add(VoxelShapes.cuboid(0.125f, 0.125f, 0.0f, 0.875f, 0.875f, 0.125f));
            }
            if (entity.directions.list.get(2)) {
                tempShape.add(VoxelShapes.cuboid(0.875f, 0.125f, 0.125f, 1.0f, 0.875f, 0.875f));
            }
            if (entity.directions.list.get(3)) {
                tempShape.add(VoxelShapes.cuboid(0.0f, 0.125f, 0.125f, 0.125f, 0.875f, 0.875f));
            }
            if (entity.directions.list.get(4)) {
                tempShape.add(VoxelShapes.cuboid(0.125f, 0.875f, 0.125f, 0.875f, 1.0f, 0.875f));
            }
            if (entity.directions.list.get(5)) {
                tempShape.add(VoxelShapes.cuboid(0.125f, 0.0f, 0.125f, 0.875f, 0.125f, 0.875f));
            }
        }
        Optional<VoxelShape> shape = tempShape.stream().reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR));
        if (shape.isPresent()) return shape.get();
        else return VoxelShapes.cuboid(0.125f, 0.0f, 0.125f, 0.875f, 0.125f, 0.875f);
    }

}