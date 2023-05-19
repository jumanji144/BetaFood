package me.darknet.betafood.mixin;

import me.darknet.betafood.BetaFoodData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class MixinPlayer {

	@Shadow
	protected FoodData foodData;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onPlayerInit(CallbackInfo info) {
		foodData = new BetaFoodData();
	}

}
