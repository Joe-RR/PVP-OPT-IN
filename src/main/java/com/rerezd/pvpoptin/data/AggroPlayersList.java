package com.rerezd.pvpoptin.data;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.rerezd.pvpoptin.PvpOptIn;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.ServerConfigEntry;
import net.minecraft.server.ServerConfigList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.File;

public class AggroPlayersList extends ServerConfigList<GameProfile, AggroPlayerEntry> {
	private static File configDir = FabricLoader.getInstance().getConfigDirectory();
	private static File modConfigDir = new File(configDir, "PvpOptIn");
	private static File aggroFile = new File(modConfigDir, "aggros.json");
	public static AggroPlayersList INSTANCE = new AggroPlayersList(aggroFile);

	private AggroPlayersList(File file) {
		super(file);

		//noinspection ResultOfMethodCallIgnored
		file.getParentFile().mkdirs();
	}

	@Override
	protected ServerConfigEntry<GameProfile> fromJson(JsonObject json) {
		return new AggroPlayerEntry(json);
	}

	public boolean isAggro(GameProfile profile) {
		return get(profile) != null;
	}

	public void setAggro(GameProfile profile, boolean aggro) {
		if (isAggro(profile) == aggro) return;

		if (aggro) {
			add(new AggroPlayerEntry(profile));
		} else {
			remove(profile);
		}

		PlayerManager playerManager = PvpOptIn.server.getPlayerManager();
		ServerPlayerEntity playerEntity = playerManager.getPlayer(profile.getId());

		if (playerEntity != null) {
			playerManager.sendToAll(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME, playerEntity));
		}
	}

	@SuppressWarnings("WeakerAccess")
	public void setAggro(GameProfile profile) {
		setAggro(profile, true);
	}

	private Text AGGRO_PREFIX = PvpOptIn.translatableText("prefix.aggressive").formatted(Formatting.YELLOW);
	private Text PASSIVE_PREFIX = PvpOptIn.translatableText("prefix.passive").formatted(Formatting.AQUA);

	public Text getAggroPrefix(GameProfile profile) {
		if (isAggro(profile)) {
			return AGGRO_PREFIX;
		} else {
			return PASSIVE_PREFIX;
		}
	}

	public Text addAggroPrefix(GameProfile profile, Text displayName) {
		return new LiteralText("")
			.append(PvpOptIn.aggros.getAggroPrefix(profile))
			.append(" ")
			.append(displayName);
	}
}
