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
import java.util.function.Consumer;

public class AggroPlayerList extends ServerConfigList<GameProfile, AggroPlayerEntry> {
	private static final File configDir = FabricLoader.getInstance().getConfigDirectory();
	private static final File modConfigDir = new File(configDir, "PvpOptIn");
	private static final File aggroFile = new File(modConfigDir, "aggros.json");
	public static final AggroPlayerList INSTANCE = new AggroPlayerList(aggroFile);

	private AggroPlayerList(File file) {
		super(file);

		//noinspection ResultOfMethodCallIgnored
		file.getParentFile().mkdirs();
	}

	@Override
	protected ServerConfigEntry<GameProfile> fromJson(JsonObject json) {
		return new AggroPlayerEntry(json);
	}

	public boolean isAggro(GameProfile profile) {
		AggroPlayerEntry entry = get(profile);
		return entry != null && entry.isAggro;
	}

	public boolean bypasses(GameProfile profile) {
		AggroPlayerEntry entry = get(profile);
		return entry != null && entry.bypasses;
	}

	public void modifyEntry(GameProfile profile, Consumer<AggroPlayerEntry> consumer) {
		AggroPlayerEntry entry = get(profile);

		if (entry == null) {
			entry = new AggroPlayerEntry(profile);
			consumer.accept(entry);

			if (entry.isValid()) add(entry);
		} else {
			consumer.accept(entry);

			if (!entry.isValid()) remove(entry);
		}
	}

	public void setAggro(GameProfile profile, boolean aggro) {
		modifyEntry(profile, entry -> entry.isAggro = aggro);

		PlayerManager playerManager = PvpOptIn.server.getPlayerManager();
		ServerPlayerEntity playerEntity = playerManager.getPlayer(profile.getId());

		if (playerEntity != null) {
			playerManager.sendToAll(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME, playerEntity));
		}
	}

	public void setBypasses(GameProfile profile, boolean bypasses) {
		modifyEntry(profile, entry -> entry.bypasses = bypasses);
	}

	public void setAggro(GameProfile profile) {
		setAggro(profile, true);
	}

	private final Text AGGRO_PREFIX = PvpOptIn.translatableText("prefix.aggressive").formatted(Formatting.YELLOW);
	private final Text PASSIVE_PREFIX = PvpOptIn.translatableText("prefix.passive").formatted(Formatting.AQUA);

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
