package com.rerezd.pvpoptin.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.rerezd.pvpoptin.PvpOptIn;
import net.minecraft.server.command.ServerCommandSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class EnumArgumentType<T extends Enum> implements ArgumentType<T> {
	private static final SimpleCommandExceptionType INVALID_VALUE_EXCEPTION = new SimpleCommandExceptionType(PvpOptIn.translatableText("exceptions.enum-argument-type.invalid-value"));

	public final Class<T> enumClass;

	private EnumArgumentType(Class<T> enumClass) {
		this.enumClass = enumClass;
	}

	private static <T extends Enum> T findConstantByName(Class<T> enumClass, String needle) {
		for (T constant : enumClass.getEnumConstants()) {
			String name = constant.name();

			if (name.equalsIgnoreCase(needle)) {
				return constant;
			}
		}

		return null;
	}

	private T findConstantByName(String needle) {
		return findConstantByName(enumClass, needle);
	}

	public static <T extends Enum> EnumArgumentType<T> forEnum(Class<T> enumClass) {
		return new EnumArgumentType<>(enumClass);
	}

	public static <T extends Enum> T getEnum(CommandContext<ServerCommandSource> ctx, String name, Class<T> enumClass) {
		return ctx.getArgument(name, enumClass);
	}

	@Override
	public T parse(StringReader reader) throws CommandSyntaxException {
		T constant = findConstantByName(reader.readUnquotedString());

		if (constant == null)
			throw INVALID_VALUE_EXCEPTION.createWithContext(reader);

		return constant;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);

		for (T val : enumClass.getEnumConstants()) {
			String name = val.name();

			if (name.toLowerCase(Locale.ROOT).startsWith(remaining)) {
				builder.suggest(name.toLowerCase(Locale.ROOT));
			}
		}

		return builder.buildFuture();
	}

	@Override
	public Collection<String> getExamples() {
		T[] constants = enumClass.getEnumConstants();
		ArrayList<String> examples = new ArrayList<>(constants.length);

		for (T constant : constants) {
			examples.add(constant.name().toLowerCase(Locale.ROOT));
		}

		return examples;
	}
}
