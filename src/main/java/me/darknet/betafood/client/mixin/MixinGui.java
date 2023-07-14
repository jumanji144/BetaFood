package me.darknet.betafood.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

	@Shadow private int displayHealth;

	@Shadow protected abstract void renderHearts(GuiGraphics poseStack, Player player, int i, int j, int k, int l, float f, int m, int n, int o, boolean bl);

	@Shadow private int tickCount;

	@Shadow private long healthBlinkTime;

	@Shadow private final static ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");

	@Inject(method = "renderPlayerHealth",
			at = @At(
					value="INVOKE",
					target="Lnet/minecraft/util/profiling/ProfilerFiller;push(Ljava/lang/String;)V"
			),
			cancellable = true)
	private void onRenderPlayerHealth(GuiGraphics poseStack, CallbackInfo ci) {
		// rewrite the render player health
		ci.cancel();
		renderBars(poseStack);
	}

	/**
	 * Extracted and rewritten from net.minecraft.client.gui.Gui#renderPlayerHealth(PoseStack)
	 * @param poseStack
	 */
	public void renderBars(GuiGraphics poseStack) {
		Player player = this.getCameraPlayer();
		int i = Mth.ceil(player.getHealth());
		int j = this.displayHealth;
		boolean bl = this.healthBlinkTime > (long)this.tickCount && (this.healthBlinkTime - (long)this.tickCount) / 3L % 2L == 1L;
		int m = this.screenWidth / 2 - 91;
		int n = this.screenWidth / 2 + 91;
		int o = this.screenHeight - 39;
		float f = Math.max((float)player.getAttributeValue(Attributes.MAX_HEALTH), (float)Math.max(j, i));
		int p = Mth.ceil(player.getAbsorptionAmount());
		int q = Mth.ceil((f + (float)p) / 2.0F / 10.0F);
		int r = Math.max(10 - (q - 2), 3);
		int s = o - (q - 1) * r - 10;
		int u = player.getArmorValue();
		int v = -1;
		if (player.hasEffect(MobEffects.REGENERATION)) {
			v = this.tickCount % Mth.ceil(f + 5.0F);
		}

		LivingEntity livingEntity = this.getPlayerVehicleWithHealth();
		int vehicleMaxHearts = this.getVehicleMaxHearts(livingEntity);


		this.minecraft.getProfiler().push("armor");

		if (u > 0) {
			int armorY = vehicleMaxHearts != 0 ? s : o;
			int xBase = vehicleMaxHearts == 0 ? (91 + 10) : 0;
			for (int w = 0; w < 10; ++w) {
				// if we have vehicle hearts render above the player hearts, if not render
				// where the food bar used to be
				int armorX = xBase + m + w * 8; // this is above the player hearts
				if (w * 2 + 1 < u) {
					poseStack.blit(GUI_ICONS_LOCATION, armorX, armorY, 34, 9, 9, 9);
				}

				if (w * 2 + 1 == u) {
					poseStack.blit(GUI_ICONS_LOCATION, armorX, armorY, 25, 9, 9, 9);
				}

				if (w * 2 + 1 > u) {
					poseStack.blit(GUI_ICONS_LOCATION, armorX, armorY, 16, 9, 9, 9);
				}
			}
		}

		this.minecraft.getProfiler().popPush("health");
		this.renderHearts(poseStack, player, m, o, r, v, f, i, j, p, bl);

		this.minecraft.getProfiler().popPush("air");
		int maxAirSupply = player.getMaxAirSupply();
		int airSupply = Math.min(player.getAirSupply(), maxAirSupply);
		if(player.isEyeInFluid(FluidTags.WATER) || airSupply < maxAirSupply) {
			int vehicleHeartRows = this.getVisibleVehicleHeartRows(vehicleMaxHearts) - 1;
			int depth = o - (vehicleHeartRows * 10) - 10;
			if(vehicleMaxHearts == 0 && u > 0) {
				depth -= 10;
			}
			double ab = Mth.ceil((double)(airSupply - 2) * 10.0 / (double)maxAirSupply);
			double ac = Mth.ceil((double)airSupply * 10.0 / (double)maxAirSupply) - ab;

			for(int ad = 0; ad < ab + ac; ++ad) {
				if (ad < ab) {
					poseStack.blit(GUI_ICONS_LOCATION, n - ad * 8 - 9, depth, 16, 18, 9, 9);
				} else {
					poseStack.blit(GUI_ICONS_LOCATION, n - ad * 8 - 9, depth, 25, 18, 9, 9);
				}
			}
		}
	}

}
