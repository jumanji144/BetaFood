package me.darknet.betafood;

import me.darknet.betafood.commands.ReloadCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import java.io.IOException;

public class BetaFood implements ModInitializer {

	private BetaFoodConfig config = new BetaFoodConfig();
	private static BetaFood instance;

	@Override
	public void onInitialize() {
		instance = this;
		System.out.println("BetaFood init");
		try {
			config.readFromFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, env) -> {
			ReloadCommand.register(dispatcher);
		});
	}

	public static BetaFood getInstance() {
		return instance;
	}

	public static BetaFoodConfig getConfig() {
		return instance.config;
	}

}
