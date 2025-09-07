package com.essentialsforfabric.commands;

import com.essentialsforfabric.util.PermissionUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.BannedPlayerEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Date;

public class KickBanCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("kick")
            .requires(source -> PermissionUtil.hasPermission(source, "essentials.kick", 3))
            .then(CommandManager.argument("player", EntityArgumentType.player())
                .executes(context -> kick(context, "Kicked by an operator"))
                .then(CommandManager.argument("reason", StringArgumentType.greedyString())
                    .executes(context -> kick(context, StringArgumentType.getString(context, "reason"))))));

        dispatcher.register(CommandManager.literal("ban")
            .requires(source -> PermissionUtil.hasPermission(source, "essentials.ban", 3))
            .then(CommandManager.argument("player", EntityArgumentType.player())
                .executes(context -> ban(context, "Banned by an operator"))
                .then(CommandManager.argument("reason", StringArgumentType.greedyString())
                    .executes(context -> ban(context, StringArgumentType.getString(context, "reason"))))));
    }

    private static int kick(CommandContext<ServerCommandSource> context, String reason) throws CommandSyntaxException {
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
        String kickerName = context.getSource().getName();

        target.networkHandler.disconnect(Text.literal("Kicked by " + kickerName + ": " + reason));

        context.getSource().sendFeedback(() -> Text.literal("Kicked " + target.getGameProfile().getName() + ": " + reason), true);
        context.getSource().getServer().getPlayerManager().broadcast(
            Text.literal(target.getGameProfile().getName() + " was kicked: " + reason), false);

        return 1;
    }

    private static int ban(CommandContext<ServerCommandSource> context, String reason) throws CommandSyntaxException {
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
        String bannerName = context.getSource().getName();

        BannedPlayerEntry banEntry = new BannedPlayerEntry(
            target.getGameProfile(),
            new Date(),
            bannerName,
            null,
            reason
        );

        context.getSource().getServer().getPlayerManager().getUserBanList().add(banEntry);
        target.networkHandler.disconnect(Text.literal("Banned by " + bannerName + ": " + reason));

        context.getSource().sendFeedback(() -> Text.literal("Banned " + target.getGameProfile().getName() + ": " + reason), true);
        context.getSource().getServer().getPlayerManager().broadcast(
            Text.literal(target.getGameProfile().getName() + " was banned: " + reason), false);

        return 1;
    }
}