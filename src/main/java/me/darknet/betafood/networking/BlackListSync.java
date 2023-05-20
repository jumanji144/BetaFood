package me.darknet.betafood.networking;

import me.darknet.betafood.BetaFood;
import me.darknet.betafood.client.BetaFoodClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class BlackListSync {

	public static final ResourceLocation BLACKLIST_ID =
			new ResourceLocation("betafood", "item_blacklist");
	public static final ResourceLocation BLACKLIST_REQUEST_ID =
			new ResourceLocation("betafood", "item_blacklist_request");

	public static void registerServer() {
		ServerPlayNetworking.registerGlobalReceiver(BLACKLIST_REQUEST_ID, (server, player, handler, buf, responseSender)
				-> server.execute(() -> {
					ServerPlayNetworking.send(player, BLACKLIST_ID, createBlackListPacket());
				}));
	}

	public static void registerClient() {
		// server login event
		ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
			// request a blacklist sync
			ClientPlayNetworking.send(BLACKLIST_REQUEST_ID, PacketByteBufs.empty());
		}));
		ClientPlayNetworking.registerGlobalReceiver(BLACKLIST_ID, ((client, handler, buf, responseSender) -> {
			List<String> blacklisted = BetaFoodClient.getBlacklisted();
			blacklisted.clear();
			int size = buf.readVarInt();
			for(int i = 0; i < size; i++) {
				blacklisted.add(buf.readUtf());
			}
		}));
	}

	private static FriendlyByteBuf createBlackListPacket() {
		FriendlyByteBuf buf = new FriendlyByteBuf(PacketByteBufs.create());
		List<String> blacklisted = BetaFood.getConfig().getBlacklisted();
		buf.writeVarInt(blacklisted.size());
		for(String item : blacklisted) {
			buf.writeUtf(item);
		}
		return buf;
	}

}
