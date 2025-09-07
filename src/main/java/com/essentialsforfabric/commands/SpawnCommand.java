package com.essentialsforfabric.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class SpawnCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("spawn")
            .requires(source -> source.hasPermissionLevel(2))
            .executes(context -> teleportToSpawn(context))
            .then(CommandManager.argument("player", EntityArgumentType.player())
                .executes(context -> teleportOtherToSpawn(context))));
    }

    private static int teleportToSpawn(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        teleportPlayerToSpawn(player);
        context.getSource().sendFeedback(() -> Text.literal("Teleported to spawn"), false);
        return 1;
    }

    private static int teleportOtherToSpawn(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
        teleportPlayerToSpawn(target);
        context.getSource().sendFeedback(() -> Text.literal("Teleported " + target.getGameProfile().getName() + " to spawn"), true);
        target.sendMessage(Text.literal("You have been teleported to spawn"));
        return 1;
    }

    private static void teleportPlayerToSpawn(ServerPlayerEntity player) {
        ServerWorld overworld = player.getServer().getOverworld();
        BlockPos spawnPos = overworld.getSpawnPos();
        float spawnAngle = overworld.getSpawnAngle();

        player.teleport(overworld, spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, spawnAngle, 0.0f);
    }
}