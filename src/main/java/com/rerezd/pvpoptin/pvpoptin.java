package com.rerezd.pvpoptin;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.rerezd.pvpoptin.commands.CommandOpt;
import com.rerezd.pvpoptin.commands.CommandOpt;
import com.rerezd.pvpoptin.globals.globals;
import jdk.nashorn.internal.runtime.StoredScript;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import com.rerezd.pvpoptin.globals.globals;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;


public class pvpoptin implements ModInitializer {
	public static final String MOD_ID = "pvpoptin";
	public static final String MOD_NAME = "Pvp Opt In";
	public static globals glbs = new globals();

	@Override
	public void onInitialize() {

		glbs.onLoad();

		CommandRegistrationCallback.EVENT.register(CommandOpt::register);

	}

}