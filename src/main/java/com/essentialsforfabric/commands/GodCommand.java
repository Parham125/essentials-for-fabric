package com.essentialsforfabric.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class GodCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("god")
            .requires(source -> source.hasPermissionLevel(2))
            .executes(context -> toggleGodSelf(context))
            .then(CommandManager.argument("player", EntityArgumentType.player())
                .executes(context -> toggleGodOther(context))));
    }

    private static int toggleGodSelf(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        boolean invulnerable = !player.getAbilities().invulnerable;
        toggleGod(player, invulnerable);
        String status = invulnerable ? "enabled" : "disabled";
        context.getSource().sendFeedback(() -> Text.literal("God mode " + status), false);
        return 1;
    }

    private static int toggleGodOther(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
        boolean invulnerable = !target.getAbilities().invulnerable;
        toggleGod(target, invulnerable);
        String status = invulnerable ? "enabled" : "disabled";
        context.getSource().sendFeedback(() -> Text.literal("God mode " + status + " for " + target.getGameProfile().getName()), true);
        target.sendMessage(Text.literal("God mode " + status));
        return 1;
    }

    private static void toggleGod(ServerPlayerEntity player, boolean invulnerable) {
        player.getAbilities().invulnerable = invulnerable;
        player.sendAbilitiesUpdate();
    }
}