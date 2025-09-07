package com.essentialsforfabric.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class WeatherCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("weather")
            .requires(source -> source.hasPermissionLevel(2))
            .then(CommandManager.argument("type", StringArgumentType.string())
                .executes(context -> setWeather(context))));
    }

    private static int setWeather(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String weatherType = StringArgumentType.getString(context, "type").toLowerCase();
        ServerWorld overworld = context.getSource().getServer().getOverworld();

        switch (weatherType) {
            case "clear", "sun", "sunny" -> {
                overworld.setWeather(6000, 0, false, false);
                context.getSource().sendFeedback(() -> Text.literal("Weather set to clear"), true);
                return 1;
            }
            case "rain", "rainy" -> {
                overworld.setWeather(0, 6000, true, false);
                context.getSource().sendFeedback(() -> Text.literal("Weather set to rain"), true);
                return 1;
            }
            case "thunder", "storm", "thunderstorm" -> {
                overworld.setWeather(0, 6000, true, true);
                context.getSource().sendFeedback(() -> Text.literal("Weather set to thunderstorm"), true);
                return 1;
            }
            default -> {
                context.getSource().sendError(Text.literal("Invalid weather type. Use: clear, rain, or thunder"));
                return 0;
            }
        }
    }
}