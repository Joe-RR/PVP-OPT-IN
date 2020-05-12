package com.rerezd.pvpoptin.mixin;

import com.mojang.authlib.GameProfile;
import com.rerezd.pvpoptin.PvpOptIn;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity {
	@Shadow
	public abstract GameProfile getGameProfile();

	@Inject(
		at = @At("RETURN"),
		method = "getDisplayName",
		cancellable = true
	)
	private void onGetDisplayName(CallbackInfoReturnable<Text> cir) {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER || PvpOptIn.server instanceof IntegratedServer) {
			cir.setReturnValue(PvpOptIn.aggros.addAggroPrefix(getGameProfile(), cir.getReturnValue()));
		}
	}
}