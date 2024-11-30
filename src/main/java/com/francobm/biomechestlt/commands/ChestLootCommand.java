package com.francobm.biomechestlt.commands;

import com.francobm.biomechestlt.BiomeChestLT;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ChestLootCommand implements ICommand{
    private final String commandName;
    private final int permissionLevel;

    public ChestLootCommand(String commandName, int permissionLevel) {
        this.commandName = commandName;
        this.permissionLevel = permissionLevel;
    }

    public ChestLootCommand(String commandName) {
        this(commandName, 2);
    }

    public ChestLootCommand() {
        this("chestloot");
    }

    @Override
    public LiteralArgumentBuilder<ServerCommandSource> getCommand() {
        LiteralArgumentBuilder<ServerCommandSource> commandSource = CommandManager.literal(commandName);
        commandSource.requires(source -> source.hasPermissionLevel(permissionLevel))
                .then(CommandManager.argument("reload", StringArgumentType.string())
                        .suggests((context, builder) -> {
                            builder.suggest("reload");
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            //Reload Command here.
                            BiomeChestLT.getLootTableHandler().loadLootTables();
                            context.getSource().sendMessage(Text.literal("§aSe han recargado los datos con exito!"));
                            return 1;
                        }));
        return commandSource;
    }
}
