package com.rerezd.pvpoptin.mixin;

import com.rerezd.pvpoptin.pvpoptin;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ServerPlayerEntity.class)
public class DamageMixin {


    @Redirect(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;shouldDamagePlayer(Lnet/minecraft/entity/player/PlayerEntity;)Z"))
    private boolean noplamage(ServerPlayerEntity playerEntity, PlayerEntity player) {
        //playerEntity == player who is being hit
        //player == player who is the source of damage
        if(pvpoptin.glbs.isPlayerAgro((ServerPlayerEntity) player) && pvpoptin.glbs.isPlayerAgro((ServerPlayerEntity) playerEntity)){
            return true;//player is agro - allow pvp
        }else if(!pvpoptin.glbs.isPlayerAgro((ServerPlayerEntity) player) || !pvpoptin.glbs.isPlayerAgro((ServerPlayerEntity) playerEntity)){
            return false; //player is passive - disallow pvp
        }

        return true;
    }
}
