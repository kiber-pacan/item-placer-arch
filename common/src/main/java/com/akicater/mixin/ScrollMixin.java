package com.akicater.mixin;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

import static com.akicater.ItemPlacerCommon.ITEMROTATE;
import static com.akicater.ItemPlacerCommon.STOP_SCROLLING_KEY;

@Mixin(Mouse.class)
public class ScrollMixin {
	@Unique
	public final Random random = new Random();

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;getDiscreteMouseScroll()Lnet/minecraft/client/option/SimpleOption;"), method = "onMouseScroll")
	private void disableScrolling(long window, double horizontal, double vertical, CallbackInfo callbackInfo) {
		if (STOP_SCROLLING_KEY.isPressed()) {
			int x = (int) Math.signum(vertical);
			if (MinecraftClient.getInstance().crosshairTarget instanceof BlockHitResult) {
				PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
				buf.writeBlockPos(((BlockHitResult) MinecraftClient.getInstance().crosshairTarget).getBlockPos());
				buf.writeFloat(x * random.nextFloat(1.0f, 3.0f));
				buf.writeBlockHitResult((BlockHitResult) MinecraftClient.getInstance().crosshairTarget);
				NetworkManager.sendToServer(ITEMROTATE, buf);
			}
		}
	}
}