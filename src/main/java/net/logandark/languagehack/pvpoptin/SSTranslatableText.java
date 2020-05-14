package net.logandark.languagehack.pvpoptin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.logandark.languagehack.pvpoptin.ducks.TextSerializerDuck;
import net.logandark.languagehack.pvpoptin.mixin.MixinTranslatableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

public class SSTranslatableText extends TranslatableText {
	@SuppressWarnings("unused")
	public SSTranslatableText(String key) {
		super(key);
	}

	public SSTranslatableText(String key, Object... args) {
		super(key, args);
	}

	public JsonObject serialize(Text.Serializer serializer, JsonSerializationContext ctx) {
		((MixinTranslatableText) this).callUpdateTranslations();

		JsonObject jsonObject = new JsonObject();
		TextSerializerDuck betterSerializer = (TextSerializerDuck) serializer;

		jsonObject.addProperty("text", "");

		if (!getStyle().isEmpty()) {
			betterSerializer.callAddStyle(getStyle(), jsonObject, ctx);
		}

		JsonArray extra = new JsonArray();

		for (Text translation : ((MixinTranslatableText) this).getTranslations()) {
			extra.add(serializer.serialize(translation, translation.getClass(), ctx));
		}

		for (Text sibling : getSiblings()) {
			extra.add(serializer.serialize(sibling, sibling.getClass(), ctx));
		}

		if (extra.size() > 0) {
			jsonObject.add("extra", extra);
		}

		return jsonObject;
	}

	@Override
	public TranslatableText copy() {
		ArrayList<Object> argCopies = new ArrayList<>(Arrays.asList(getArgs()));
		ListIterator<Object> iter = argCopies.listIterator();

		while (iter.hasNext()) {
			Object arg = iter.next();

			if (arg instanceof Text) {
				iter.remove();
				iter.add(((Text) arg).copy());
			}
		}

		return new SSTranslatableText(getKey(), argCopies.toArray());
	}
}
