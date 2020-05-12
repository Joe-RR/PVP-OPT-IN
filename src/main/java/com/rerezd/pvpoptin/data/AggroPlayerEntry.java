package com.rerezd.pvpoptin.data;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.ServerConfigEntry;

import java.util.UUID;

public class AggroPlayerEntry extends ServerConfigEntry<GameProfile> {
	private static GameProfile gameProfileFromJSON(JsonObject json) {
		UUID uuid = UUID.fromString(json.get("uuid").getAsString());
		String name = json.get("name").getAsString();

		return new GameProfile(uuid, name);
	}

	private static void gameProfileToJSON(GameProfile profile, JsonObject json) {
		json.addProperty("uuid", profile.getId().toString());
		json.addProperty("name", profile.getName());
	}

	@SuppressWarnings("WeakerAccess")
	GameProfile profile;

	AggroPlayerEntry(GameProfile profile) {
		super(profile);
		this.profile = profile;
	}

	AggroPlayerEntry(JsonObject json) {
		super(gameProfileFromJSON(json));
		profile = gameProfileFromJSON(json);
	}

	@Override
	protected void fromJson(JsonObject json) {
		gameProfileToJSON(profile, json);
	}
}
