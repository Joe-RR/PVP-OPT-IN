package com.rerezd.pvpoptin.mixin;

import com.mojang.authlib.GameProfile;
import com.rerezd.pvpoptin.PvpOptIn;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerListS2CPacket.Entry.class)
public class MixinPlayerListS2CPacketEntry {
	@SuppressWarnings("unused")
	@Shadow
	@Final
	@Mutable
	private Text displayName;

	@Shadow
	@Final
	private GameProfile profile;

	@SuppressWarnings("InvalidInjectorMethodSignature")
	@Inject(
		at = @At("RETURN"),
		method = "<init>(Lcom/mojang/authlib/GameProfile;ILnet/minecraft/world/GameMode;Lnet/minecraft/text/Text;)V"
	)
	private void onInit(PlayerListS2CPacket packet, GameProfile gameProfile, int i, GameMode gameMode, Text text, CallbackInfo ci) {
		Text displayName = text;

		if (displayName == null) {
			ServerPlayerEntity player = PvpOptIn.server.getPlayerManager().getPlayer(profile.getId());

			if (player != null) {
				this.displayName = player.getDisplayName();

				return;
			} else {
				displayName = new LiteralText(gameProfile.getName());
			}
		}

		this.displayName = PvpOptIn.aggros.addAggroPrefix(gameProfile, displayName);
	}
}
