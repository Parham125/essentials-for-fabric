package com.essentialsforfabric.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;

public class GamemodeCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        registerGamemodeCommand(dispatcher, "gmc", GameMode.CREATIVE);
        registerGamemodeCommand(dispatcher, "gms", GameMode.SURVIVAL);
        registerGamemodeCommand(dispatcher, "gma", GameMode.ADVENTURE);
        registerGamemodeCommand(dispatcher, "gmsp", GameMode.SPECTATOR);
    }

    private static void registerGamemodeCommand(CommandDispatcher<ServerCommandSource> dispatcher, String command, GameMode gameMode) {
        dispatcher.register(CommandManager.literal(command)
            .requires(source -> source.hasPermissionLevel(2))
            .executes(context -> setGamemodeSelf(context, gameMode))
            .then(CommandManager.argument("player", EntityArgumentType.player())
                .executes(context -> setGamemodeOther(context, gameMode))));
    }

    private static int setGamemodeSelf(CommandContext<ServerCommandSource> context, GameMode gameMode) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        player.changeGameMode(gameMode);
        context.getSource().sendFeedback(() -> Text.literal("Gamemode set to " + gameMode.getName()), false);
        return 1;
    }

    private static int setGamemodeOther(CommandContext<ServerCommandSource> context, GameMode gameMode) throws CommandSyntaxException {
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
        target.changeGameMode(gameMode);
        context.getSource().sendFeedback(() -> Text.literal("Gamemode set to " + gameMode.getName() + " for " + target.getGameProfile().getName()), true);
        target.sendMessage(Text.literal("Gamemode set to " + gameMode.getName()));
        return 1;
    }
}