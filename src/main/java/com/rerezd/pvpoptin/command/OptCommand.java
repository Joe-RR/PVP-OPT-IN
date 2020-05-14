package com.rerezd.pvpoptin.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.rerezd.pvpoptin.PvpOptIn;
import com.rerezd.pvpoptin.command.arguments.EnumArgumentType;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class OptCommand {
	@SuppressWarnings("unused")
	enum Playstyles {
		OUT(false),
		PASSIVE(false),
		IN(true),
		AGGRESSIVE(true),
		AGGRO(true);

		final boolean isAggro;

		Playstyles(boolean isAggro) {
			this.isAggro = isAggro;
		}
	}

	@SuppressWarnings("unused")
	enum Bypasses {
		ON(true),
		ENABLE(true),
		TRUE(true),
		OFF(false),
		DISABLE(false),
		FALSE(false);

		final boolean bypasses;

		Bypasses(boolean bypasses) {
			this.bypasses = bypasses;
		}
	}

	@SuppressWarnings("unused")
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicatedServer) {
		LiteralArgumentBuilder<ServerCommandSource> rootNode = CommandManager.literal("opt");
		RequiredArgumentBuilder<ServerCommandSource, Playstyles> playstyle =
			CommandManager.argument("playstyle", EnumArgumentType.forEnum(Playstyles.class));

		LiteralArgumentBuilder<ServerCommandSource> bypass = CommandManager.literal("bypass");
		LiteralArgumentBuilder<ServerCommandSource> bypassStatus = CommandManager.literal("status");
		RequiredArgumentBuilder<ServerCommandSource, EntitySelector> bypassPlayer =
			CommandManager.argument("player", EntityArgumentType.player());

		RequiredArgumentBuilder<ServerCommandSource, Bypasses> whichBypass =
			CommandManager.argument("which", EnumArgumentType.forEnum(Bypasses.class));
		RequiredArgumentBuilder<ServerCommandSource, EntitySelector> whichBypassPlayer =
			CommandManager.argument("player", EntityArgumentType.player());

		rootNode.executes(OptCommand::playstyleStatus);
		playstyle.executes(OptCommand::playstyleChange);
		bypass.requires(src -> src.hasPermissionLevel(3));
		bypass.executes(OptCommand::bypassStatus);
		bypassStatus.executes(OptCommand::bypassStatus);
		bypassPlayer.executes(OptCommand::bypassStatusOther);
		whichBypass.executes(OptCommand::bypassChange);
		whichBypassPlayer.executes(OptCommand::bypassChangeOther);

		dispatcher.register(rootNode
			.then(playstyle)
			.then(bypass
				.then(bypassStatus
					.then(bypassPlayer))
				.then(whichBypass
					.then(whichBypassPlayer)))
		);
	}

	@SuppressWarnings("SameReturnValue")
	private static int playstyleStatus(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
		ctx.getSource().sendFeedback(PvpOptIn.translatableText(
			"command.opt.playstyle.status",
			PvpOptIn.aggros.getAggroPrefix(ctx.getSource().getPlayer().getGameProfile())
		), false);

		return 1;
	}

	private static int playstyleChange(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
		Playstyles playstyle = EnumArgumentType.getEnum(ctx, "playstyle", Playstyles.class);
		ServerPlayerEntity player = ctx.getSource().getPlayer();

		if (player.getHealth() < player.getMaximumHealth()) {
			ctx.getSource().sendError(PvpOptIn.translatableText("command.opt.playstyle.not-full"));
			return 0;
		}

		PvpOptIn.aggros.setAggro(player.getGameProfile(), playstyle.isAggro);

		ctx.getSource().sendFeedback(PvpOptIn.translatableText(
			"command.opt.playstyle.changed",
			PvpOptIn.aggros.getAggroPrefix(player.getGameProfile())
		), false);

		return 1;
	}

	@SuppressWarnings("SameReturnValue")
	private static int bypassStatus(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
		ServerPlayerEntity player = ctx.getSource().getPlayer();
		boolean bypasses = PvpOptIn.aggros.bypasses(player.getGameProfile());

		ctx.getSource().sendFeedback(PvpOptIn.translatableText(
			"command.opt.bypass.status." + (bypasses ? "enabled" : "disabled")
		), false);

		return 1;
	}

	@SuppressWarnings("SameReturnValue")
	private static int bypassStatusOther(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgumentType.getPlayer(ctx, "player");
		boolean bypasses = PvpOptIn.aggros.bypasses(player.getGameProfile());

		ctx.getSource().sendFeedback(PvpOptIn.translatableText(
			"command.opt.bypass.status.other." + (bypasses ? "enabled" : "disabled"),
			player.getDisplayName()
		), false);

		return 1;
	}

	@SuppressWarnings("SameReturnValue")
	private static int bypassChange(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
		ServerPlayerEntity player = ctx.getSource().getPlayer();
		Bypasses which = EnumArgumentType.getEnum(ctx, "which", Bypasses.class);
		boolean bypass = which.bypasses;

		PvpOptIn.aggros.setBypasses(player.getGameProfile(), bypass);

		ctx.getSource().sendFeedback(PvpOptIn.translatableText(
			"command.opt.bypass." + (bypass ? "enabled" : "disabled")
		), true);

		return 1;
	}

	@SuppressWarnings("SameReturnValue")
	private static int bypassChangeOther(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgumentType.getPlayer(ctx, "player");
		Bypasses which = EnumArgumentType.getEnum(ctx, "which", Bypasses.class);
		boolean bypass = which.bypasses;

		PvpOptIn.aggros.setBypasses(player.getGameProfile(), bypass);

		ctx.getSource().sendFeedback(PvpOptIn.translatableText(
			"command.opt.bypass.other." + (bypass ? "enabled" : "disabled"),
			player.getDisplayName()
		), true);

		return 1;
	}
}
