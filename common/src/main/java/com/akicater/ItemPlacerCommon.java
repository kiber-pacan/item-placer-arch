package com.akicater;

import com.akicater.blocks.layingItem;
import com.akicater.blocks.layingItemBlockEntity;
import com.google.common.base.Suppliers;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ItemPlacerCommon {
    public static final Logger LOGGER = LoggerFactory.getLogger("item-placer");
	public static String MODID = "item-placer";

	public static Supplier<RegistrarManager> MANAGER;

	public static RegistrySupplier<Block> LAYING_ITEM;
	public static RegistrySupplier<BlockEntityType<layingItemBlockEntity>> LAYING_ITEM_BLOCK_ENTITY;

	public static Identifier ITEMPLACE;
	public static Identifier ITEMROTATE;


	public static KeyBinding PLACE_KEY;
	public static KeyBinding STOP_SCROLLING_KEY;

	public static void initializeServer() {
		if (Platform.isForge()) MODID = "itemplacer";

		MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MODID));
		Registrar<Block> blocks = MANAGER.get().get(Registries.BLOCK);
		Registrar<BlockEntityType<?>> blockEntityTypes = MANAGER.get().get(Registries.BLOCK_ENTITY_TYPE);

		LAYING_ITEM = blocks.register(new Identifier(MODID, "laying_item"), () -> new layingItem(Block.Settings.create().breakInstantly().nonOpaque().noBlockBreakParticles().pistonBehavior(PistonBehavior.DESTROY)));
		LAYING_ITEM_BLOCK_ENTITY = blockEntityTypes.register(
				new Identifier(MODID, "laying_item_block_entity"),
				() -> BlockEntityType.Builder.create(layingItemBlockEntity::new, LAYING_ITEM.get()).build(null)
		);

		ITEMPLACE = new Identifier(MODID, "itemplace");
		ITEMROTATE = new Identifier(MODID, "itemrotate");

		NetworkManager.registerReceiver(NetworkManager.Side.C2S, ITEMPLACE, (buf, context) -> {
			ItemStack stack = context.getPlayer().getMainHandStack();
			World world = context.getPlayer().getWorld();
			BlockPos pos = buf.readBlockPos();
			BlockHitResult hitResult = buf.readBlockHitResult();
			if (world.getBlockState(pos).getBlock() == Blocks.AIR || world.getBlockState(pos).getBlock() == Blocks.WATER) {
				context.getPlayer().setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
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
					world.emitGameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, pos);
					blockEntity.markDirty();
				}
			} else if (world.getBlockState(pos).getBlock() == ItemPlacerCommon.LAYING_ITEM.get()) {
				Direction dir = hitResult.getSide().getOpposite();
				layingItemBlockEntity blockEntity = (layingItemBlockEntity)world.getChunk(pos).getBlockEntity(pos);
				if (blockEntity != null) {
					int i = ItemPlacerCommon.dirToInt(dir);
					if(blockEntity.inventory.get(i).isEmpty()) {
						context.getPlayer().setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
						blockEntity.inventory.set(i, stack);
						world.emitGameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, pos);
						blockEntity.markDirty();
					}
				}
			}
		});

		NetworkManager.registerReceiver(NetworkManager.Side.C2S, ITEMROTATE, (buf, context) -> {
			BlockPos pos = buf.readBlockPos();
			World world = context.getPlayer().getEntityWorld();
			BlockEntity blockEntity = world.getChunk(pos).getBlockEntity(pos);
			if (blockEntity instanceof layingItemBlockEntity) {
				((layingItemBlockEntity) blockEntity).rotate(buf.readFloat(), getDirection(buf.readBlockHitResult()));
			}
		});

	}

	public static void initializeClient() {
		if (Platform.isForge()) {
			MODID = "itemplacer";

			NetworkManager.registerReceiver(NetworkManager.Side.C2S, ITEMPLACE, (buf, context) -> {
				ItemStack stack = context.getPlayer().getMainHandStack();
				World world = context.getPlayer().getWorld();
				BlockPos pos = buf.readBlockPos();
				BlockHitResult hitResult = buf.readBlockHitResult();
				if (world.getBlockState(pos).getBlock() == Blocks.AIR || world.getBlockState(pos).getBlock() == Blocks.WATER) {
					context.getPlayer().setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
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
						world.emitGameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, pos);
						blockEntity.markDirty();
					}
				} else if (world.getBlockState(pos).getBlock() == ItemPlacerCommon.LAYING_ITEM.get()) {
					Direction dir = hitResult.getSide().getOpposite();
					layingItemBlockEntity blockEntity = (layingItemBlockEntity)world.getChunk(pos).getBlockEntity(pos);
					if (blockEntity != null) {
						int i = ItemPlacerCommon.dirToInt(dir);
						if(blockEntity.inventory.get(i).isEmpty()) {
							context.getPlayer().setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
							blockEntity.inventory.set(i, stack);
							world.emitGameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, pos);
							blockEntity.markDirty();
						}
					}
				}
			});

			NetworkManager.registerReceiver(NetworkManager.Side.C2S, ITEMROTATE, (buf, context) -> {
				BlockPos pos = buf.readBlockPos();
				World world = context.getPlayer().getEntityWorld();
				BlockEntity blockEntity = world.getChunk(pos).getBlockEntity(pos);
				if (blockEntity instanceof layingItemBlockEntity) {
					((layingItemBlockEntity) blockEntity).rotate(buf.readFloat(), getDirection(buf.readBlockHitResult()));
				}
			});
		}

		ITEMPLACE = new Identifier(MODID, "itemplace");
		ITEMROTATE = new Identifier(MODID, "itemrotate");

		PLACE_KEY = new KeyBinding(
				"Place item",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_V,
				"item-placer"
		);

		STOP_SCROLLING_KEY = new KeyBinding(
				"Rotate item",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_LEFT_ALT,
				"item-placer"
		);

		KeyMappingRegistry.register(PLACE_KEY);
		KeyMappingRegistry.register(STOP_SCROLLING_KEY);

		ClientTickEvent.CLIENT_POST.register(client -> {
			if (PLACE_KEY.wasPressed()) {
				if (client.crosshairTarget instanceof BlockHitResult && client.player.getStackInHand(Hand.MAIN_HAND) != ItemStack.EMPTY && MinecraftClient.getInstance().world.getBlockState(((BlockHitResult) client.crosshairTarget).getBlockPos()).getBlock() != Blocks.AIR) {
					PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
					Direction side = ((BlockHitResult) client.crosshairTarget).getSide();
					BlockPos pos = ((BlockHitResult) client.crosshairTarget).getBlockPos();
					buf.writeBlockPos(pos.offset(side, 1));
					buf.writeBlockHitResult((BlockHitResult) client.crosshairTarget);
					NetworkManager.sendToServer(ITEMPLACE, buf);
				}
			}
		});
	}

	public static int dirToInt(Direction dir) {
		return switch (dir) {
			case SOUTH -> 0;
			case NORTH -> 1;
			case EAST -> 2;
			case WEST -> 3;
			case UP -> 4;
			case DOWN -> 5;
		};
	}

	public static Direction intToDir(int dir) {
		return switch (dir) {
			case 0 -> Direction.SOUTH;
			case 1 -> Direction.NORTH;
			case 2 -> Direction.EAST;
			case 3 -> Direction.WEST;
			case 4 -> Direction.UP;
			case 5 -> Direction.DOWN;
			default -> throw new IllegalStateException("Unexpected value: " + dir);
		};
	}

	static boolean contains(Vec3d vec, Box box) {
		return vec.x >= box.minX
				&& vec.x <= box.maxX
				&& vec.y >= box.minY
				&& vec.y <= box.maxY
				&& vec.z >= box.minZ
				&& vec.z <= box.maxZ;
	}

	static List<Box> boxes = new ArrayList<>(
			List.of(
					new Box(0.125f, 0.125f, 0.875f, 0.875f, 0.875f, 1.0f),
					new Box(0.125f, 0.125f, 0.0f, 0.875f, 0.875f, 0.125f),
					new Box(0.875f, 0.125f, 0.125f, 1.0f, 0.875f, 0.875f),
					new Box(0.0f, 0.125f, 0.125f, 0.125f, 0.875f, 0.875f),
					new Box(0.125f, 0.875f, 0.125f, 0.875f, 1.0f, 0.875f),
					new Box(0.125f, 0.0f, 0.125f, 0.875f, 0.125f, 0.875f)
			)
	);

	public static int getDirection(BlockHitResult hit) {
		double xT = hit.getPos().x;
		double yT = hit.getPos().y;
		double zT = hit.getPos().z;

		BlockPos blockPos = hit.getBlockPos();

		double x = Math.abs(xT - blockPos.getX());
		double y = Math.abs(yT - blockPos.getY());
		double z = Math.abs(zT - blockPos.getZ());

		Vec3d pos = new Vec3d(x,y,z);

		for (int i = 0; i < boxes.size(); i++) {
			if (contains(pos, boxes.get(i))) {
				return i;
			}
		}
		LOGGER.warn("Somehow you got warning? damn... Maybe my mod is fucking garbage? (item-placer)");
		return 0;
	}
}