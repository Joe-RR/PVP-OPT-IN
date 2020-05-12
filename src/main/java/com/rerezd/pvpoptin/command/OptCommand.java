package com.rerezd.pvpoptin.command;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.rerezd.pvpoptin.PvpOptIn;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class OptCommand {
	private static final Set<String> PASSIVE = ImmutableSet.of("out", "passive");
	private static final Set<String> AGGRESSIVE = ImmutableSet.of("in", "aggressive", "aggro");
	private static final Set<String> PLAYSTYLES = Sets.union(PASSIVE, AGGRESSIVE).immutableCopy();

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicatedServer) {
		LiteralArgumentBuilder<ServerCommandSource> rootNode = CommandManager.literal("opt");
		RequiredArgumentBuilder<ServerCommandSource, String> playstyle =
			CommandManager.argument("playstyle", StringArgumentType.word())
				.suggests(suggestedStrings());

		rootNode.executes(ctx -> {
			ctx.getSource().sendFeedback(PvpOptIn.translatableText(
				"command.opt.status",
				PvpOptIn.aggros.getAggroPrefix(ctx.getSource().getPlayer().getGameProfile())
			), false);

			return 1;
		});

		playstyle.executes(OptCommand::execute);

		dispatcher.register(rootNode.then(playstyle));
	}

	private static SuggestionProvider<ServerCommandSource> suggestedStrings() {
		return (ctx, builder) -> getSuggestionsBuilder(builder);
	}

	private static CompletableFuture<Suggestions> getSuggestionsBuilder(SuggestionsBuilder builder) {
		String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);

		for (String playstyle : PLAYSTYLES) {
			if (playstyle.toLowerCase(Locale.ROOT).startsWith(remaining)) {
				builder.suggest(playstyle);
			}
		}

		return builder.buildFuture();
	}

	private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		String playstyle = StringArgumentType.getString(context, "playstyle");

		if (!PLAYSTYLES.contains(playstyle)) {
			context.getSource().sendError(PvpOptIn.translatableText("command.opt.invalid-playstyle", playstyle));
			return 0;
		}

		ServerPlayerEntity player = context.getSource().getPlayer();

		if (player.getHealth() < player.getMaximumHealth()) {
			context.getSource().sendError(PvpOptIn.translatableText("command.opt.not-full-health"));
			return 0;
		}

		PvpOptIn.aggros.setAggro(
			player.getGameProfile(),
			AGGRESSIVE.contains(playstyle)
		);

		context.getSource().sendFeedback(PvpOptIn.translatableText(
			"command.opt.status-changed",
			PvpOptIn.aggros.getAggroPrefix(player.getGameProfile())
		), false);

		return 1;
	}
}
