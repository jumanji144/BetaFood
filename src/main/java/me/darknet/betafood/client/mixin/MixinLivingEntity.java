package me.darknet.betafood.client.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

	// redirect the spawnItemParticles call in triggerItemUseEffects to a stub
	@Redirect(method = "triggerItemUseEffects",
			at = @At(
					value="INVOKE",
					target="Lnet/minecraft/world/entity/LivingEntity;spawnItemParticles(Lnet/minecraft/world/item/ItemStack;I)V"
			))
	private void onTriggerItemUseEffects(LivingEntity livingEntity, ItemStack itemStack, int i) {
		if(!(livingEntity instanceof Player)) { // only do it on players
			livingEntity.spawnItemParticles(itemStack, i);
		}
		// stub
	}

}
