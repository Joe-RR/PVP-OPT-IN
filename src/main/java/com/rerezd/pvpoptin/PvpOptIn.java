package com.rerezd.pvpoptin;

import com.rerezd.pvpoptin.command.OptCommand;
import com.rerezd.pvpoptin.data.AggroPlayersList;
import com.rerezd.pvpoptin.data.DataMigration;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.logandark.languagehack.pvpoptin.LanguageHack;
import net.logandark.languagehack.pvpoptin.SSTranslatableText;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.TranslatableText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@SuppressWarnings("WeakerAccess")
public class PvpOptIn implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger();

	public static final String MODID = "pvp-opt-in";
	public static final AggroPlayersList aggros = AggroPlayersList.INSTANCE;

	public static MinecraftServer server;

	@Override
	public void onInitialize() {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
			try {
				LanguageHack.activate();
			} catch (Throwable e) {
				LOGGER.error("Could not activate LanguageHack", e);

				throw new RuntimeException(e);
			}
		}

		try {
			aggros.load();
		} catch (Throwable e) {
			LOGGER.error("Couldn't load aggros file", e);

			throw new RuntimeException(e);
		}

		if (DataMigration.shouldMigrateData()) {
			DataMigration.migrateData();
		}

		CommandRegistrationCallback.EVENT.register(OptCommand::register);
	}

	public static String translationKey(String path) {
		return MODID + "." + path;
	}

	public static TranslatableText translatableText(String path, Object... args) {
		return new SSTranslatableText(translationKey(path), args);
	}
}