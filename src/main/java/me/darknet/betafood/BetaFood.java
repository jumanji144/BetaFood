package me.darknet.betafood;

import net.fabricmc.api.ModInitializer;

public class BetaFood implements ModInitializer {

	private BetaFoodConfig config = new BetaFoodConfig();
	private static BetaFood instance;

	@Override
	public void onInitialize() {
		instance = this;
		System.out.println("BetaFood init");
		config.readFromFile();
	}

	public static BetaFood getInstance() {
		return instance;
	}

	public BetaFoodConfig getConfig() {
		return config;
	}

}
