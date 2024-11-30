package com.francobm.biomechestlt;

import com.francobm.biomechestlt.handlers.LootTableHandler;
import com.francobm.biomechestlt.handlers.commands.CommandsHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;

import java.util.Random;

public class BiomeChestLT implements ModInitializer {
    public static String MOD_ID = "biomechestlt";
    public static String NBT_TAG = MOD_ID + ".used";
    public static Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MOD_ID);

    private static MinecraftServer serverInstance;

    private CommandsHandler commandsHandler;
    private static LootTableHandler lootTableHandler;

    public static Random RANDOM = new Random();

    @Override
    public void onInitialize() {
        commandsHandler = new CommandsHandler();
        commandsHandler.registerCommands();
        lootTableHandler = new LootTableHandler();
        loadServerInstance();
    }

    private void loadServerInstance() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            serverInstance = server;
            lootTableHandler.loadLootTables(); //Cargar las loot tables cuando serverInstance tenga un valor
        });
    }

    public CommandsHandler getCommandsHandler() {
        return commandsHandler;
    }

    public static MinecraftServer getServerInstance() {
        return serverInstance;
    }

    public static LootTableHandler getLootTableHandler() {
        return lootTableHandler;
    }
}

