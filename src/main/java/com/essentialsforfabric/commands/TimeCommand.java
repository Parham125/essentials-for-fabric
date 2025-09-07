package com.essentialsforfabric.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class TimeCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("time")
            .requires(source -> source.hasPermissionLevel(2))
            .then(CommandManager.literal("set")
                .then(CommandManager.argument("time", StringArgumentType.string())
                    .executes(context -> setTime(context))))
            .then(CommandManager.literal("add")
                .then(CommandManager.argument("time", IntegerArgumentType.integer())
                    .executes(context -> addTime(context)))));
    }

    private static int setTime(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String timeArg = StringArgumentType.getString(context, "time");
        long time = parseTime(timeArg);

        if (time == -1) {
            context.getSource().sendError(Text.literal("Invalid time format. Use day, night, noon, midnight, or a number"));
            return 0;
        }

        for (ServerWorld world : context.getSource().getServer().getWorlds()) {
            world.setTimeOfDay(time);
        }

        context.getSource().sendFeedback(() -> Text.literal("Time set to " + time), true);
        return 1;
    }

    private static int addTime(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int timeToAdd = IntegerArgumentType.getInteger(context, "time");

        for (ServerWorld world : context.getSource().getServer().getWorlds()) {
            world.setTimeOfDay(world.getTimeOfDay() + timeToAdd);
        }

        context.getSource().sendFeedback(() -> Text.literal("Added " + timeToAdd + " to the current time"), true);
        return 1;
    }

    private static long parseTime(String timeArg) {
        return switch (timeArg.toLowerCase()) {
            case "day" -> 1000;
            case "noon" -> 6000;
            case "night" -> 13000;
            case "midnight" -> 18000;
            default -> {
                try {
                    yield Long.parseLong(timeArg);
                } catch (NumberFormatException e) {
                    yield -1;
                }
            }
        };
    }
}