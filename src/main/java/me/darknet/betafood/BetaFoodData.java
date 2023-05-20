package me.darknet.betafood;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BetaFoodData extends FoodData {

	private float currentHealth;
	private float maxHealth;
	private float toHeal;

	public BetaFoodData() {
		System.out.println("BetaFoodData constructor");
	}

	@Override
	public void tick(Player player) {
		currentHealth = player.getHealth();
		maxHealth = player.getMaxHealth();

		if(toHeal != 0) {
			player.heal(toHeal);
			toHeal = 0;
		}
	}

	@Override
	public boolean needsFood() {
		return currentHealth < maxHealth;
	}

	@Override
	public void setSaturation(float f) {
		// stub
	}

	@Override
	public float getSaturationLevel() {
		return 20F;
	}

	@Override
	public void setFoodLevel(int i) {
		// stub
	}

	@Override
	public int getFoodLevel() {
		return 20;
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compoundTag) {
		// stub
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compoundTag) {
		// stub
	}

	@Override
	public void eat(int hunger, float saturation) {
		toHeal = hunger * BetaFood.getConfig().getEffectiveness();
	}

	@Override
	public void eat(Item item, ItemStack itemStack) {
		ResourceLocation location = BuiltInRegistries.ITEM.getKey(item);
		if(item.isEdible() && BetaFood.getConfig().isAllowed(location.toString())) {
			FoodProperties foodProperties = item.getFoodProperties();
			if(foodProperties != null) {
				this.eat(foodProperties.getNutrition(), foodProperties.getSaturationModifier());
			}
		}
	}
}
