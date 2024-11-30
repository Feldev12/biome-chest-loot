package com.francobm.biomechestlt.mixins;

import com.francobm.biomechestlt.BiomeChestLT;
import com.francobm.biomechestlt.interfaces.ICustomChestEntity;
import com.francobm.biomechestlt.loots.CustomLootTable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntity.class)
public abstract class ChestBlockEntityMixin extends LootableContainerBlockEntity implements ICustomChestEntity {
    @Unique
    private boolean used = true;

    protected ChestBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    private void biomeChestLT$writeNbt(NbtCompound nbt, CallbackInfo ci) {
        if(used) {
            nbt.putBoolean(BiomeChestLT.NBT_TAG, true);
        }
    }

    @Inject(method = "readNbt", at = @At("TAIL"))
    private void biomeChestLT$readNbt(NbtCompound nbt, CallbackInfo ci) {
        if(nbt.contains(BiomeChestLT.NBT_TAG)) {
            used = nbt.getBoolean(BiomeChestLT.NBT_TAG);
        }
        //BiomeChestLT.LOGGER.info("ReadNBT Used: {}", used);
    }

    @Override
    public void checkLootInteraction(@Nullable PlayerEntity player) {
        if(used || player == null) {
            super.checkLootInteraction(player);
            return;
        }
        if (this.world.getServer() == null) return;
        LootContext.Builder builder = (new LootContext.Builder((ServerWorld)this.world)).parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(this.pos));
        player.getWorld().getBiome(player.getBlockPos()).getKey().ifPresent(key -> {
            CustomLootTable customLootTable = BiomeChestLT.getLootTableHandler().getLootTableByBiomes(key.getValue().toString());
            if(customLootTable == null) {
                BiomeChestLT.LOGGER.warn("No se ha encontrado un Custom Loot Table para este bioma: {}", key.getValue());
                return;
            }
            if(customLootTable.isDefault()) {
                BiomeChestLT.LOGGER.warn("No se encontro un Custom Loot Table para este bioma, Usando el predeterminado..");
            }
            LootTable lootTable = customLootTable.getLootTable();
            //BiomeChestLT.LOGGER.info("Biome: {} - {}", key.getValue(), key.getRegistry());
            if(lootTable == null) {
                //BiomeChestLT.LOGGER.info("No se ha encontrado un Loot Table");
                return;
            }

            lootTable.supplyInventory(this, builder.build(LootContextTypes.CHEST));
            used = true;
            //BiomeChestLT.LOGGER.info("Aplicando la Custom Loot Table al cofre");
            markDirty();
        });
    }

    @Unique
    public boolean biomeChestLT$isUsed() {
        return used;
    }

    @Unique
    public void biomeChestLT$setUsed(boolean used) {
        this.used = used;
    }
}
