package com.francobm.biomechestlt.handlers.commands;

import com.francobm.biomechestlt.commands.ChestLootCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class CommandsHandler {

    public void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(new ChestLootCommand().getCommand());
        });
    }
}
