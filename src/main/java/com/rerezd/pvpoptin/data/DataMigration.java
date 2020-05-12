package com.rerezd.pvpoptin.data;

import com.mojang.authlib.GameProfile;
import com.rerezd.pvpoptin.PvpOptIn;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Stream;

public class DataMigration {
	private static final File configDir = FabricLoader.getInstance().getConfigDirectory();
	private static final File oldModConfigDir = new File(configDir, "PvpOptIn");
	private static final File oldAggroFile = new File(oldModConfigDir.getPath(), "AgroPlayers.txt");

	public static boolean shouldMigrateData() {
		return oldAggroFile.exists() && oldAggroFile.isFile();
	}

	private static Stream<UUID> getUuids(Path path) {
		try {
			return Files.lines(path).map(UUID::fromString);
		} catch (IOException e) {
			PvpOptIn.LOGGER.error("Failed to read lines from old config file", e);

			return Stream.empty();
		}
	}

	private static GameProfile uuidToProfile(UUID uuid) {
		return new GameProfile(uuid, null);
	}

	public static void migrateData() {
		if (!shouldMigrateData()) {
			PvpOptIn.LOGGER.warn("migrateData() called when data should not be migrated");
			return;
		}

		getUuids(oldAggroFile.toPath())
			.map(DataMigration::uuidToProfile)
			.forEach(PvpOptIn.aggros::setAggro);

		PvpOptIn.LOGGER.info("Successfully migrated old data");

		if (!oldAggroFile.delete()) {
			PvpOptIn.LOGGER.error("Data migration could not delete the old config file");
		}
	}
}
