package me.darknet.betafood;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.prefs.Preferences;

public class BetaFoodConfig {

	private static final Path path = FabricLoader.getInstance().getConfigDir().resolve("betafood.preferences");

	private int stackSize = 4;
	private int useDuration = 1;
	private float effectiveness = 1F;

	public int getStackSize() {
		return stackSize;
	}

	public int getUseDuration() {
		return useDuration;
	}

	public float getEffectiveness() {
		return effectiveness;
	}

	public void writeToFile() {
		String content = "stackSize=" + stackSize + "\nuseDuration=" + useDuration + "\neffectiveness=" + effectiveness;
		try {
			Files.write(path, content.getBytes());
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
			}
		}
	}

}
