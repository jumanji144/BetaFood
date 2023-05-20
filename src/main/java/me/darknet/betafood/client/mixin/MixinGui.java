package me.darknet.betafood.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.client.gui.GuiComponent.blit;

@Mixin(Gui.class)
public abstract class MixinGui {

	@Final
	@Shadow
	private Minecraft minecraft;

	@Shadow protected abstract Player getCameraPlayer();

	@Shadow protected abstract int getVisibleVehicleHeartRows(int i);

	@Shadow protected abstract LivingEntity getPlayerVehicleWithHealth();

	@Shadow protected abstract int getVehicleMaxHearts(LivingEntity livingEntity);

	@Shadow private int screenHeight;

	@Shadow private int screenWidth;

	@Inject(method = "renderPlayerHealth",
			at = @At(
					value="INVOKE_ASSIGN",
					target="Lnet/minecraft/client/gui/Gui;getPlayerVehicleWithHealth()Lnet/minecraft/world/entity/LivingEntity;"
			),
			cancellable = true)
	private void onRenderPlayerHealth(PoseStack poseStack, CallbackInfo ci) {
		// skip the render food bar routine,
		// but we need to renderAir as it comes after this, and is not in a method
		this.renderAir(poseStack);
		ci.cancel();
	}

	private void renderAir(PoseStack poseStack) {
		LivingEntity livingEntity = this.getPlayerVehicleWithHealth();
		int x = this.getVehicleMaxHearts(livingEntity);
		Player player = this.getCameraPlayer();
		this.minecraft.getProfiler().popPush("air");
		int n = this.screenWidth / 2 + 91;
		int o = this.screenHeight - 39;
		int t = o - 10;
		int y = player.getMaxAirSupply();
		int z = Math.min(player.getAirSupply(), y);
		if (player.isEyeInFluid(FluidTags.WATER) || z < y) {
			int aa = this.getVisibleVehicleHeartRows(x) - 1;
			t -= aa * 10;
			int ab = Mth.ceil((double)(z - 2) * 10.0 / (double)y);
			int ac = Mth.ceil((double)z * 10.0 / (double)y) - ab;

			for(int ad = 0; ad < ab + ac; ++ad) {
				if (ad < ab) {
					blit(poseStack, n - ad * 8 - 9, t, 16, 18, 9, 9);
				} else {
					blit(poseStack, n - ad * 8 - 9, t, 25, 18, 9, 9);
				}
			}
		}

		this.minecraft.getProfiler().pop();
	}

}
