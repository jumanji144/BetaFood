package me.darknet.betafood.commands;

import com.mojang.brigadier.CommandDispatcher;
import me.darknet.betafood.BetaFood;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.io.IOException;

public class ReloadCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("betafood")
				.requires((commandSource) -> commandSource.hasPermission(2))
				.then(Commands.literal("reload")
						.executes((commandContext) -> {
							return reload();
						})
				)
		);
	}

	public static int reload() {
		try {
			BetaFood.getInstance().getConfig().readFromFile();
		} catch (IOException e) {
			return 0;
		}
		return 1;
	}

}
