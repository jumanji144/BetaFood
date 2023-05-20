package me.darknet.betafood.mixin;

import me.darknet.betafood.BetaFood;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class MixinItem {

	@Inject(method = "getUseDuration", at = @At("HEAD"), cancellable = true)
	private void onGetUseDuration(ItemStack stack, CallbackInfoReturnable<Integer> info) {
		if(stack.getItem().isEdible() && BetaFood.getConfig().isAllowed(
				BuiltInRegistries.ITEM.getKey(stack.getItem()).toString())) {
			info.setReturnValue(BetaFood.getConfig().getUseDuration());
		}
	}

	@Inject(method = "getMaxStackSize", at = @At("HEAD"), cancellable = true)
	private void onGetMaxStackSize(CallbackInfoReturnable<Integer> info) {
		Item item = (Item) (Object) this;
		if(item.isEdible() && BetaFood.getConfig().isAllowed(
				BuiltInRegistries.ITEM.getKey(item).toString())) {
			info.setReturnValue(BetaFood.getConfig().getStackSize());
		}
	}

}
