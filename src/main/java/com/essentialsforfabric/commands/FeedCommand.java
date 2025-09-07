package com.essentialsforfabric.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class FeedCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("feed")
            .requires(source -> source.hasPermissionLevel(2))
            .executes(context -> feedSelf(context))
            .then(CommandManager.argument("player", EntityArgumentType.player())
                .executes(context -> feedOther(context))));
    }

    private static int feedSelf(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        feed(player);
        context.getSource().sendFeedback(() -> Text.literal("You have been fed"), false);
        return 1;
    }

    private static int feedOther(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
        feed(target);
        context.getSource().sendFeedback(() -> Text.literal("Fed " + target.getGameProfile().getName()), true);
        target.sendMessage(Text.literal("You have been fed"));
        return 1;
    }

    private static void feed(ServerPlayerEntity player) {
        player.getHungerManager().setFoodLevel(20);
        player.getHungerManager().setSaturationLevel(20.0f);
    }
}