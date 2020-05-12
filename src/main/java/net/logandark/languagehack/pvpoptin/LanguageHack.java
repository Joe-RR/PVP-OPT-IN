package net.logandark.languagehack.pvpoptin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rerezd.pvpoptin.PvpOptIn;
import net.logandark.languagehack.pvpoptin.mixin.MixinLanguage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Language;
import org.apache.logging.log4j.core.util.Closer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Allows dedicated servers to load the language files from this mod.
 */
public class LanguageHack {
	private static final String MODID = PvpOptIn.MODID;

	public static void activate() throws IOException {
		MixinLanguage language = (MixinLanguage) Language.getInstance();
		InputStream inputStream = null;

		try {
			//noinspection OptionalGetWithoutIsPresent
			inputStream = Files.newInputStream(
				FabricLoader.getInstance()
					.getModContainer(MODID).get()
					.getPath("assets/" + MODID + "/lang/en_us.json")
			);

			JsonObject jsonObject = new Gson().fromJson(
				new InputStreamReader(inputStream, StandardCharsets.UTF_8),
				JsonObject.class
			);

			jsonObject.entrySet().forEach(entry -> {
				String formatString = language.getTokenPattern()
					.matcher(JsonHelper.asString(entry.getValue(), entry.getKey()))
					.replaceAll("%$1s");
				language.getTranslations().put(entry.getKey(), formatString);
			});
		} finally {
			Closer.closeSilently(inputStream);
		}
	}
}
