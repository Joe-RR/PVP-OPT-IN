package com.rerezd.pvpoptin.mixin;



import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.rerezd.pvpoptin.globals.globals;
import com.rerezd.pvpoptin.pvpoptin;

@Mixin(ServerPlayNetworkHandler.class)
public class ChatMixin
{

    @Shadow
    public ServerPlayerEntity player;

    @Redirect(method = "onGameMessage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;broadcastChatMessage(Lnet/minecraft/text/Text;Z)V"))
    private void onGameMessage(PlayerManager playerManager, Text text, boolean system, ChatMessageC2SPacket packet)
    {
        String prefix = "";
        String color = "White";


        if(pvpoptin.glbs.isPlayerAgro(player)){
            prefix = "[Aggressive]";
            color = "Yellow";
        }else{
            prefix = "[Passive]";
            color = "Aqua";
        }

        LiteralText name = new LiteralText(prefix + " ");
        name.append(player.getDisplayName());

        Text nmsg = new TranslatableText("chat.type.text",  pvpoptin.glbs.addStyle(name, color), packet.getChatMessage());

        playerManager.broadcastChatMessage(nmsg, system);
    }



}