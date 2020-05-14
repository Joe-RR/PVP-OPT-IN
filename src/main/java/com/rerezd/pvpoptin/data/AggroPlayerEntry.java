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

	final GameProfile profile;
	public boolean isAggro;
	public boolean bypasses;

	AggroPlayerEntry(GameProfile profile) {
		super(profile);
		this.profile = profile;
		this.isAggro = false;
		this.bypasses = false;
	}

	AggroPlayerEntry(JsonObject json) {
		super(gameProfileFromJSON(json));
		profile = gameProfileFromJSON(json);

		if (json.has("isAggro")) {
			isAggro = json.get("isAggro").getAsBoolean();
			bypasses = json.get("bypass").getAsBoolean();
		} else {
			isAggro = true;
			bypasses = false;
		}
	}

	@Override
	protected void fromJson(JsonObject json) {
		gameProfileToJSON(profile, json);
		json.addProperty("isAggro", isAggro);
		json.addProperty("bypass", bypasses);
	}

	public boolean isValid() {
		return isAggro || bypasses;
	}
}
