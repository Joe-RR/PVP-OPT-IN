package com.rerezd.pvpoptin.mixin;

import com.rerezd.pvpoptin.PvpOptIn;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftDedicatedServer.class)
public class MixinMinecraftDedicatedServer {
	@Inject(
		at = @At("HEAD"),
		method = "setupServer"
	)
	private void onSetupServer(CallbackInfoReturnable<Boolean> cir) {
		PvpOptIn.server = (MinecraftDedicatedServer) (Object) this;
	}
}
