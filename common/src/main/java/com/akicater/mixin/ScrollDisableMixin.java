package com.akicater.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.akicater.ItemPlacerCommon.LAYING_ITEM;
import static com.akicater.ItemPlacerCommon.STOP_SCROLLING_KEY;

@Mixin(PlayerInventory.class)
public class ScrollDisableMixin {

	@Inject(at = @At("HEAD"), method = "Lnet/minecraft/entity/player/PlayerInventory;scrollInHotbar(D)V", cancellable = true)
	private void disableScrolling(double scrollAmount, CallbackInfo callbackInfo) {
		if (STOP_SCROLLING_KEY != null && STOP_SCROLLING_KEY.isPressed() && MinecraftClient.getInstance().crosshairTarget instanceof BlockHitResult && MinecraftClient.getInstance().world.getBlockState(((BlockHitResult)MinecraftClient.getInstance().crosshairTarget).getBlockPos()).getBlock() == LAYING_ITEM.get()) {
			MinecraftClient.getInstance().player.playSound(SoundEvents.BLOCK_DISPENSER_FAIL, 0.35f,1.5f);
			callbackInfo.cancel();
		}
	}
}