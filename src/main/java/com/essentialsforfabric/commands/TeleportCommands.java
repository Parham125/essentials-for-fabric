package com.essentialsforfabric.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TeleportCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("tp")
            .requires(source -> source.hasPermissionLevel(2))
            .then(CommandManager.argument("target", EntityArgumentType.player())
                .executes(context -> teleportToPlayer(context))));

        dispatcher.register(CommandManager.literal("tphere")
            .requires(source -> source.hasPermissionLevel(2))
            .then(CommandManager.argument("player", EntityArgumentType.player())
                .executes(context -> teleportPlayerHere(context))));
    }

    private static int teleportToPlayer(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");

        if (player == target) {
            context.getSource().sendError(Text.literal("Cannot teleport to yourself"));
            return 0;
        }

        player.teleport(target.getServerWorld(), target.getX(), target.getY(), target.getZ(), 
                       target.getYaw(), target.getPitch());
        context.getSource().sendFeedback(() -> Text.literal("Teleported to " + target.getGameProfile().getName()), false);
        return 1;
    }

    private static int teleportPlayerHere(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");

        if (player == target) {
            context.getSource().sendError(Text.literal("Cannot teleport yourself to yourself"));
            return 0;
        }

        target.teleport(player.getServerWorld(), player.getX(), player.getY(), player.getZ(), 
                       target.getYaw(), target.getPitch());
        context.getSource().sendFeedback(() -> Text.literal("Teleported " + target.getGameProfile().getName() + " to you"), true);
        target.sendMessage(Text.literal("You have been teleported to " + player.getGameProfile().getName()));
        return 1;
    }
}