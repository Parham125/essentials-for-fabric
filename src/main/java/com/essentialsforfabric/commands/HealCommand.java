package com.essentialsforfabric.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class HealCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("heal")
            .requires(source -> source.hasPermissionLevel(2))
            .executes(context -> healSelf(context))
            .then(CommandManager.argument("player", EntityArgumentType.player())
                .executes(context -> healOther(context))));
    }

    private static int healSelf(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        heal(player);
        context.getSource().sendFeedback(() -> Text.literal("You have been healed"), false);
        return 1;
    }

    private static int healOther(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
        heal(target);
        context.getSource().sendFeedback(() -> Text.literal("Healed " + target.getGameProfile().getName()), true);
        target.sendMessage(Text.literal("You have been healed"));
        return 1;
    }

    private static void heal(ServerPlayerEntity player) {
        player.setHealth(player.getMaxHealth());
        player.getHungerManager().setFoodLevel(20);
        player.getHungerManager().setSaturationLevel(20.0f);
    }
}