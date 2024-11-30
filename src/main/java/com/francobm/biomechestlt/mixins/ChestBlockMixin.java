package com.francobm.biomechestlt.mixins;

import com.francobm.biomechestlt.interfaces.ICustomChestEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlock.class)
public class ChestBlockMixin {

    @Inject(method = "onPlaced", at = @At("TAIL"))
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(!(placer instanceof ServerPlayerEntity player)) return;
        if(player.interactionManager.getGameMode().equals(GameMode.SURVIVAL)) return;
        if (!(blockEntity instanceof ICustomChestEntity customChestEntity)) return;
        customChestEntity.biomeChestLT$setUsed(false);
    }
}
