package com.rerezd.pvpoptin.mixin;

import com.rerezd.pvpoptin.PvpOptIn;
import net.minecraft.server.integrated.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IntegratedServer.class)
public class MixinIntegratedServer {
	@Inject(
		at = @At("HEAD"),
		method = "setupServer"
	)
	private void onSetupServer(CallbackInfoReturnable<Boolean> cir) {
		PvpOptIn.server = (IntegratedServer) (Object) this;
	}
}
