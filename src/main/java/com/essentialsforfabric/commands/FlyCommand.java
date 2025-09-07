package com.essentialsforfabric.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class FlyCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("fly")
            .requires(source -> source.hasPermissionLevel(2))
            .executes(context -> toggleFlySelf(context))
            .then(CommandManager.argument("player", EntityArgumentType.player())
                .executes(context -> toggleFlyOther(context))));
    }

    private static int toggleFlySelf(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        boolean canFly = !player.getAbilities().allowFlying;
        toggleFly(player, canFly);
        String status = canFly ? "enabled" : "disabled";
        context.getSource().sendFeedback(() -> Text.literal("Flight " + status), false);
        return 1;
    }

    private static int toggleFlyOther(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
        boolean canFly = !target.getAbilities().allowFlying;
        toggleFly(target, canFly);
        String status = canFly ? "enabled" : "disabled";
        context.getSource().sendFeedback(() -> Text.literal("Flight " + status + " for " + target.getGameProfile().getName()), true);
        target.sendMessage(Text.literal("Flight " + status));
        return 1;
    }

    private static void toggleFly(ServerPlayerEntity player, boolean canFly) {
        player.getAbilities().allowFlying = canFly;
        if (!canFly) {
            player.getAbilities().flying = false;
        }
        player.sendAbilitiesUpdate();
    }
}