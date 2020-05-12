package net.logandark.languagehack.pvpoptin.ducks;

import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.text.Style;

public interface TextSerializerDuck {
	@SuppressWarnings({"EmptyMethod", "unused"})
	void callAddStyle(Style style, JsonObject json, JsonSerializationContext context);
}
