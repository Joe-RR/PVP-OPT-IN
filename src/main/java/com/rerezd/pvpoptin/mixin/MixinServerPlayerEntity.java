package com.rerezd.pvpoptin.mixin;

import com.mojang.authlib.GameProfile;
import com.rerezd.pvpoptin.PvpOptIn;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity {
	@Redirect(
		method = "damage",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/network/ServerPlayerEntity;shouldDamagePlayer(Lnet/minecraft/entity/player/PlayerEntity;)Z"
		)
	)
	private boolean onShouldDamagePlayer(ServerPlayerEntity attacker, PlayerEntity attacked) {
		GameProfile attackerProfile = attacker.getGameProfile();
		GameProfile attackedProfile = attacked.getGameProfile();

		if (!PvpOptIn.aggros.bypasses(attackerProfile)) {
			if (!PvpOptIn.aggros.isAggro(attackerProfile) || !PvpOptIn.aggros.isAggro(attackedProfile)) {
				return false;
			}
		}

		return attacker.shouldDamagePlayer(attacked);
	}
}
