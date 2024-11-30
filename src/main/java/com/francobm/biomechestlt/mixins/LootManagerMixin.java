package com.francobm.biomechestlt.mixins;

import com.francobm.biomechestlt.interfaces.ICustomLootManager;
import com.google.gson.Gson;
import net.minecraft.loot.LootManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LootManager.class)
public abstract class LootManagerMixin implements ICustomLootManager {

    @Shadow @Final private static Gson GSON;

    public Gson biomeChestLT$getGson() {
        return GSON;
    }
}
