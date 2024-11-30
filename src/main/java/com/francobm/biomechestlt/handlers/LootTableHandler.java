package com.francobm.biomechestlt.handlers;

import com.francobm.biomechestlt.BiomeChestLT;
import com.francobm.biomechestlt.interfaces.ICustomLootManager;
import com.francobm.biomechestlt.loots.CustomLootTable;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class LootTableHandler {
    private File lootTableFile;
    private final Set<CustomLootTable> lootTables;
    private CustomLootTable defaultLootTable;

    public LootTableHandler() {
        createDefaultFile();
        lootTables = new HashSet<>();
    }

    public void loadLootTables() {
        lootTables.clear();
        ICustomLootManager customLootManager = (ICustomLootManager) BiomeChestLT.getServerInstance().getLootManager();
        Gson gson = customLootManager.biomeChestLT$getGson();
        try(FileReader reader = new FileReader(lootTableFile)) {
            CustomLootTable[] customLootTables = gson.fromJson(reader, CustomLootTable[].class);

            for(CustomLootTable customLootTable : customLootTables){
                if(customLootTable.isDefault()) {
                    defaultLootTable = customLootTable;
                    continue;
                }
                lootTables.add(customLootTable);
            }

            BiomeChestLT.LOGGER.info("Registrando {} loot_tables de la config", customLootTables.length);
        } catch (IOException e) {
            BiomeChestLT.LOGGER.error("Error al leer el archivo: {}", e.getMessage());
        }
    }

    //crear el archivo de loot tables
    private void createDefaultFile() {
        String path = "config/BiomeChestLT/loot_tables.json";
        lootTableFile = new File(path);
        if(lootTableFile.exists()) return;
        lootTableFile.getParentFile().mkdirs();
        try {
            lootTableFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        writingDefaultFile();
    }

    //Escribir el contenido por defecto del archivo de loot tables
    private void writingDefaultFile() {
        JsonArray parentJsonArray = new JsonArray();

        JsonObject defaultParentJson = new JsonObject();

        JsonArray lootTablesArray = new JsonArray();

        JsonObject defaultLootTableJson = new JsonObject();

        defaultLootTableJson.addProperty("type", "minecraft:empty");
        defaultLootTableJson.add("pools", new JsonArray());
        lootTablesArray.add(defaultLootTableJson);

        defaultParentJson.addProperty("default", true);
        defaultParentJson.add("biomes", new JsonArray());
        defaultParentJson.add("loot_tables", lootTablesArray);

        parentJsonArray.add(defaultParentJson);
        try(FileWriter writer = new FileWriter(lootTableFile)){
            Gson gson = new Gson();
            gson.toJson(parentJsonArray, writer);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Obtener la loot table por el bioma o el default
    public CustomLootTable getLootTableByBiomes(String biome) {
        Optional<CustomLootTable> customLootTable = lootTables.stream().filter(lootTable -> lootTable.getBiomes().contains(biome)).findFirst();
        return customLootTable.orElse(defaultLootTable);
    }
}
