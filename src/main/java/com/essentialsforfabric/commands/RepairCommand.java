package com.essentialsforfabric.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class RepairCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("repair")
            .requires(source -> source.hasPermissionLevel(2))
            .executes(context -> repairSelf(context))
            .then(CommandManager.argument("player", EntityArgumentType.player())
                .executes(context -> repairOther(context))));
    }

    private static int repairSelf(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        ItemStack heldItem = player.getMainHandStack();

        if (heldItem.isEmpty()) {
            context.getSource().sendError(Text.literal("You must be holding an item to repair"));
            return 0;
        }

        if (!heldItem.isDamageable()) {
            context.getSource().sendError(Text.literal("This item cannot be repaired"));
            return 0;
        }

        heldItem.setDamage(0);
        context.getSource().sendFeedback(() -> Text.literal("Item repaired"), false);
        return 1;
    }

    private static int repairOther(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
        ItemStack heldItem = target.getMainHandStack();

        if (heldItem.isEmpty()) {
            context.getSource().sendError(Text.literal(target.getGameProfile().getName() + " is not holding an item"));
            return 0;
        }

        if (!heldItem.isDamageable()) {
            context.getSource().sendError(Text.literal("That item cannot be repaired"));
            return 0;
        }

        heldItem.setDamage(0);
        context.getSource().sendFeedback(() -> Text.literal("Repaired item for " + target.getGameProfile().getName()), true);
        target.sendMessage(Text.literal("Your item has been repaired"));
        return 1;
    }
}