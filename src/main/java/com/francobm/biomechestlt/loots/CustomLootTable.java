package com.francobm.biomechestlt.loots;

import com.francobm.biomechestlt.BiomeChestLT;
import com.google.gson.annotations.SerializedName;
import net.minecraft.loot.LootTable;

import java.util.List;

public class CustomLootTable {
    @SerializedName("default")
    private boolean isDefault;

    private final List<String> biomes;

    @SerializedName("loot_tables")
    private final List<LootTable> lootTables;

    public CustomLootTable(boolean isDefault, List<String> biomes, List<LootTable> lootTables) {
        this.isDefault = isDefault;
        this.biomes = biomes;
        this.lootTables = lootTables;
    }

    //Obtener la primera loot table o una aleatoria
    public LootTable getLootTable() {
        if(lootTables.size() <= 1) {
            return lootTables.get(0);
        }
        int seed = BiomeChestLT.RANDOM.nextInt(0, lootTables.size());
        return lootTables.get(seed);
    }

    public List<LootTable> getLootTables() {
        return lootTables;
    }

    public List<String> getBiomes() {
        return biomes;
    }

    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public String toString() {
        return "CustomLootTable{" +
                "biomes=" + biomes +
                ", lootTables=" + lootTables +
                '}';
    }
}
