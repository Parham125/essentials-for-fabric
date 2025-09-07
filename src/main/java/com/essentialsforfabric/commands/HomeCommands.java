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

public class HomeCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("home")
            .requires(source -> PermissionUtil.hasPermission(source, "essentials.home", 0))
            .executes(context -> home(context, "home"))
            .then(CommandManager.argument("name", StringArgumentType.string())
                .executes(context -> home(context, StringArgumentType.getString(context, "name")))));

        dispatcher.register(CommandManager.literal("sethome")
            .requires(source -> PermissionUtil.hasPermission(source, "essentials.sethome", 0))
            .executes(context -> setHome(context, "home"))
            .then(CommandManager.argument("name", StringArgumentType.string())
                .executes(context -> setHome(context, StringArgumentType.getString(context, "name")))));

        dispatcher.register(CommandManager.literal("delhome")
            .requires(source -> PermissionUtil.hasPermission(source, "essentials.sethome", 0))
            .then(CommandManager.argument("name", StringArgumentType.string())
                .executes(context -> deleteHome(context, StringArgumentType.getString(context, "name")))));
    }

    private static int home(CommandContext<ServerCommandSource> context, String homeName) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        PlayerDataManager.LocationData home = PlayerDataManager.getHome(player.getUuid(), homeName);

        if (home == null) {
            context.getSource().sendError(Text.literal("Home '" + homeName + "' not found"));
            return 0;
        }

        PlayerDataManager.setLastLocation(player);

        RegistryKey<World> worldKey = RegistryKey.of(RegistryKeys.WORLD, new Identifier(home.world));
        ServerWorld world = context.getSource().getServer().getWorld(worldKey);

        if (world == null) {
            context.getSource().sendError(Text.literal("World not found"));
            return 0;
        }

        player.teleport(world, home.x, home.y, home.z, home.yaw, home.pitch);
        context.getSource().sendFeedback(() -> Text.literal("Teleported to home: " + homeName), false);

        return 1;
    }

    private static int setHome(CommandContext<ServerCommandSource> context, String homeName) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

        PlayerDataManager.setHome(player, homeName);
        context.getSource().sendFeedback(() -> Text.literal("Home '" + homeName + "' set"), false);

        return 1;
    }

    private static int deleteHome(CommandContext<ServerCommandSource> context, String homeName) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        PlayerDataManager.PlayerData data = PlayerDataManager.getPlayerData(player.getUuid());

        if (data.homes.remove(homeName.toLowerCase()) != null) {
            context.getSource().sendFeedback(() -> Text.literal("Home '" + homeName + "' deleted"), false);
            return 1;
        } else {
            context.getSource().sendError(Text.literal("Home '" + homeName + "' not found"));
            return 0;
        }
    }
}