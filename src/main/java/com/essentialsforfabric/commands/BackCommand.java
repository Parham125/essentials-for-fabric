package com.essentialsforfabric.commands;

import com.essentialsforfabric.data.PlayerDataManager;
import com.essentialsforfabric.util.PermissionUtil;
import com.mojang.brigadier.CommandDispatcher;
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

public class BackCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("back")
            .requires(source -> PermissionUtil.hasPermission(source, "essentials.back", 0))
            .executes(context -> back(context)));
    }

    private static int back(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        PlayerDataManager.LocationData lastLocation = PlayerDataManager.getLastLocation(player.getUuid());

        if (lastLocation == null) {
            context.getSource().sendError(Text.literal("No previous location found"));
            return 0;
        }

        PlayerDataManager.setLastLocation(player);

        RegistryKey<World> worldKey = RegistryKey.of(RegistryKeys.WORLD, new Identifier(lastLocation.world));
        ServerWorld world = context.getSource().getServer().getWorld(worldKey);

        if (world == null) {
            context.getSource().sendError(Text.literal("Previous world not found"));
            return 0;
        }

        player.teleport(world, lastLocation.x, lastLocation.y, lastLocation.z, lastLocation.yaw, lastLocation.pitch);
        context.getSource().sendFeedback(() -> Text.literal("Teleported to previous location"), false);

        return 1;
    }
}