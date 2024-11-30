package com.francobm.biomechestlt.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;

public interface ICommand {

    LiteralArgumentBuilder<ServerCommandSource> getCommand();
}
