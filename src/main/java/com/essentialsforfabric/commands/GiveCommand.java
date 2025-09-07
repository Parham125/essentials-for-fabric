package com.essentialsforfabric.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class GiveCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, net.minecraft.command.CommandRegistryAccess registryAccess) {
        dispatcher.register(CommandManager.literal("give")
            .requires(source -> source.hasPermissionLevel(2))
            .then(CommandManager.argument("item", ItemStackArgumentType.itemStack(registryAccess))
                .executes(context -> giveSelf(context, 1))
                .then(CommandManager.argument("count", IntegerArgumentType.integer(1, 64))
                    .executes(context -> giveSelf(context, IntegerArgumentType.getInteger(context, "count")))
                    .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes(context -> giveOther(context, IntegerArgumentType.getInteger(context, "count")))))));
    }

    private static int giveSelf(CommandContext<ServerCommandSource> context, int count) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        ItemStack itemStack = ItemStackArgumentType.getItemStackArgument(context, "item").createStack(count, false);

        if (!player.getInventory().insertStack(itemStack)) {
            player.dropItem(itemStack, false);
        }

        String itemName = itemStack.getItem().getName().getString();
        String message = count == 1 ? "Gave " + itemName : "Gave " + count + " " + itemName;
        context.getSource().sendFeedback(() -> Text.literal(message), false);
        return count;
    }

    private static int giveOther(CommandContext<ServerCommandSource> context, int count) throws CommandSyntaxException {
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
        ItemStack itemStack = ItemStackArgumentType.getItemStackArgument(context, "item").createStack(count, false);

        if (!target.getInventory().insertStack(itemStack)) {
            target.dropItem(itemStack, false);
        }

        String itemName = itemStack.getItem().getName().getString();
        String giveMessage = count == 1 ? "Gave " + itemName + " to " + target.getGameProfile().getName() : "Gave " + count + " " + itemName + " to " + target.getGameProfile().getName();
        String receiveMessage = count == 1 ? "You received " + itemName : "You received " + count + " " + itemName;
        context.getSource().sendFeedback(() -> Text.literal(giveMessage), true);
        target.sendMessage(Text.literal(receiveMessage));
        return count;
    }
}