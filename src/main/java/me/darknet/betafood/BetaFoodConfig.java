package me.darknet.betafood;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class BetaFoodConfig {

	private static final Path path = FabricLoader.getInstance().getConfigDir().resolve("betafood.preferences");

	private int stackSize = 4;
	private int useDuration = 1;
	private float effectiveness = 1F;
	private List<String> blacklisted = List.of("minecraft:rotten_flesh",
			"minecraft:spider_eye", "minecraft:poisonous_potato");

	public int getStackSize() {
		return stackSize;
	}

	public int getUseDuration() {
		return useDuration;
	}

	public float getEffectiveness() {
		return effectiveness;
	}

	public boolean isAllowed(String item) {
		return !blacklisted.contains(item);
	}

	public List<String> getBlacklisted() {
		return blacklisted;
	}

	public void writeToFile() {
		StringBuilder content = new StringBuilder();
		content.append("stackSize=").append(stackSize).append("\n");
		content.append("useDuration=").append(useDuration).append("\n");
		content.append("effectiveness=").append(effectiveness).append("\n");
		content.append("blacklisted=");
		for(String item : blacklisted) {
			content.append(item).append(",");
		}
		content.append("\n");
		try {
			Files.write(path, content.toString().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readFromFile() throws IOException {
		if(!Files.exists(path)) {
			writeToFile();
		}

		// Read from file
		List<String> lines = Files.readAllLines(path);
		for(String line : lines) {
			String[] split = line.split("=");
			if(split.length != 2) {
				continue;
			}
			String key = split[0];
			String value = split[1];
			switch (key) {
				case "stackSize" -> stackSize = Integer.parseInt(value);
				case "useDuration" -> useDuration = Integer.parseInt(value);
				case "effectiveness" -> effectiveness = Float.parseFloat(value);
				case "blacklisted" -> blacklisted = List.of(value.split(","));
			}
		}
	}

}
