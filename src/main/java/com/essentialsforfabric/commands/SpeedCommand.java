package com.essentialsforfabric.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SpeedCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("speed")
            .requires(source -> source.hasPermissionLevel(2))
            .then(CommandManager.argument("amount", FloatArgumentType.floatArg(0.0f, 10.0f))
                .executes(context -> setSpeedSelf(context))
                .then(CommandManager.argument("player", EntityArgumentType.player())
                    .executes(context -> setSpeedOther(context)))));
    }

    private static int setSpeedSelf(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        float speed = FloatArgumentType.getFloat(context, "amount");
        setSpeed(player, speed);
        context.getSource().sendFeedback(() -> Text.literal("Speed set to " + speed), false);
        return 1;
    }

    private static int setSpeedOther(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
        float speed = FloatArgumentType.getFloat(context, "amount");
        setSpeed(target, speed);
        context.getSource().sendFeedback(() -> Text.literal("Speed set to " + speed + " for " + target.getGameProfile().getName()), true);
        target.sendMessage(Text.literal("Speed set to " + speed));
        return 1;
    }

    private static void setSpeed(ServerPlayerEntity player, float speed) {
        float walkSpeed = speed * 0.1f;
        float flySpeed = speed * 0.05f;
        player.getAbilities().setWalkSpeed(walkSpeed);
        player.getAbilities().setFlySpeed(flySpeed);
        player.sendAbilitiesUpdate();
    }
}