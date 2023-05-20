package me.darknet.betafood.client;

import me.darknet.betafood.networking.BlackListSync;
import net.fabricmc.api.ClientModInitializer;

import java.util.ArrayList;
import java.util.List;

public class BetaFoodClient implements ClientModInitializer {

	private static List<String> blacklisted = new ArrayList<>();

	@Override
	public void onInitializeClient() {
		BlackListSync.registerClient();
	}

	public static boolean isAllowed(String item) {
		return !blacklisted.contains(item);
	}

	public static List<String> getBlacklisted() {
		return blacklisted;
	}
}
