package com.rerezd.pvpoptin.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.rerezd.pvpoptin.globals.globals;
import net.minecraft.network.MessageType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import com.rerezd.pvpoptin.pvpoptin;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class CommandOpt {


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean b) {
        System.out.println("Hello Fabric world!!!!!!!!!!");
        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("opt").then(
                CommandManager.argument("playStyle", StringArgumentType.string()).suggests(suggestedStrings())
                        .executes(context -> execute(context)));


        dispatcher.register(literal);
    }
    //edited example suggestion code from the fabric wiki
    public static SuggestionProvider<ServerCommandSource> suggestedStrings() {
        List<String> sugma = Arrays.asList("passive","aggressive","in","out");
        return (ctx, builder) -> getSuggestionsBuilder(builder, sugma);
    }

    private static CompletableFuture<Suggestions> getSuggestionsBuilder(SuggestionsBuilder builder, List<String> list) {
        String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);

        if(list.isEmpty()) {
            return Suggestions.empty();
        }

        for (String str : list) {
            if (str.toLowerCase(Locale.ROOT).startsWith(remaining)) {
                builder.suggest(str);
            }
        }
        return builder.buildFuture();
    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        String playstyle = StringArgumentType.getString(context, "playStyle");
        System.out.println(playstyle);
        ServerPlayerEntity player = context.getSource().getPlayer();

        MutableText txt = new LiteralText("Please heal up to full health. You may not change PVP status until you are healed.");

        if(player.getHealth() != 20.0F){ //this is the requirement for being ful health. If you want to change the criteria this is where you do it
            player.sendMessage( pvpoptin.glbs.addStyle(txt, "Gold"), MessageType.CHAT);

            return 1;//dont finish command - player is most likely in combat
        }

        switch(playstyle){
            case "in": //not putting a break here cause "in" and "aggressive" are aliases
            case "aggressive":
                pvpoptin.glbs.addAgro(player);
                break;
            case "out": //not putting a break here cause "out" and "passive" are aliases
            case "passive":
                if(pvpoptin.glbs.isPlayerAgro(player)){
                    pvpoptin.glbs.removeAgro(player);
                }
                break;
        }

        return 1;
    }


}
