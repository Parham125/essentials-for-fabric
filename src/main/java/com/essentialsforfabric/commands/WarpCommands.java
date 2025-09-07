package com.essentialsforfabric.commands;

import com.essentialsforfabric.data.PlayerDataManager;
import com.essentialsforfabric.util.PermissionUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Map;

public class WarpCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("warp")
            .requires(source -> PermissionUtil.hasPermission(source, "essentials.warp", 0))
            .executes(context -> listWarps(context))
            .then(CommandManager.argument("name", StringArgumentType.string())
                .executes(context -> warp(context, StringArgumentType.getString(context, "name")))));

        dispatcher.register(CommandManager.literal("setwarp")
            .requires(source -> PermissionUtil.hasPermission(source, "essentials.setwarp", 2))
            .then(CommandManager.argument("name", StringArgumentType.string())
                .executes(context -> setWarp(context, StringArgumentType.getString(context, "name")))));

        dispatcher.register(CommandManager.literal("delwarp")
            .requires(source -> PermissionUtil.hasPermission(source, "essentials.delwarp", 2))
            .then(CommandManager.argument("name", StringArgumentType.string())
                .executes(context -> deleteWarp(context, StringArgumentType.getString(context, "name")))));
    }

    private static int warp(CommandContext<ServerCommandSource> context, String warpName) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        PlayerDataManager.WarpData warp = PlayerDataManager.getWarp(warpName);

        if (warp == null) {
            context.getSource().sendError(Text.literal("Warp '" + warpName + "' not found"));
            return 0;
        }

        PlayerDataManager.setLastLocation(player);

        RegistryKey<World> worldKey = RegistryKey.of(RegistryKeys.WORLD, new Identifier(warp.world));
        ServerWorld world = context.getSource().getServer().getWorld(worldKey);

        if (world == null) {
            context.getSource().sendError(Text.literal("Warp world not found"));
            return 0;
        }

        player.teleport(world, warp.x, warp.y, warp.z, warp.yaw, warp.pitch);
        context.getSource().sendFeedback(() -> Text.literal("Teleported to warp: " + warpName), false);

        return 1;
    }

    private static int setWarp(CommandContext<ServerCommandSource> context, String warpName) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

        PlayerDataManager.setWarp(warpName, player);
        context.getSource().sendFeedback(() -> Text.literal("Warp '" + warpName + "' set"), true);

        return 1;
    }

    private static int deleteWarp(CommandContext<ServerCommandSource> context, String warpName) {
        PlayerDataManager.WarpData warp = PlayerDataManager.getWarp(warpName);

        if (warp == null) {
            context.getSource().sendError(Text.literal("Warp '" + warpName + "' not found"));
            return 0;
        }

        PlayerDataManager.deleteWarp(warpName);
        context.getSource().sendFeedback(() -> Text.literal("Warp '" + warpName + "' deleted"), true);

        return 1;
    }

    private static int listWarps(CommandContext<ServerCommandSource> context) {
        Map<String, PlayerDataManager.WarpData> warps = PlayerDataManager.getAllWarps();

        if (warps.isEmpty()) {
            context.getSource().sendFeedback(() -> Text.literal("No warps available"), false);
            return 0;
        }

        StringBuilder warpList = new StringBuilder("Available warps: ");
        warpList.append(String.join(", ", warps.keySet()));

        context.getSource().sendFeedback(() -> Text.literal(warpList.toString()), false);

        return warps.size();
    }
}