package com.essentialsforfabric.util;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class PermissionUtil {

    public static boolean hasPermission(ServerCommandSource source, String permission) {
        return hasPermission(source, permission, 2);
    }

    public static boolean hasPermission(ServerCommandSource source, String permission, int defaultLevel) {
        if (source.hasPermissionLevel(4)) {
            return true;
        }

        try {
            ServerPlayerEntity player = source.getPlayerOrThrow();

            switch (permission) {
                case "essentials.heal" -> {
                    return source.hasPermissionLevel(2);
                }
                case "essentials.heal.others" -> {
                    return source.hasPermissionLevel(2);
                }
                case "essentials.feed" -> {
                    return source.hasPermissionLevel(2);
                }
                case "essentials.feed.others" -> {
                    return source.hasPermissionLevel(2);
                }
                case "essentials.fly" -> {
                    return source.hasPermissionLevel(2);
                }
                case "essentials.fly.others" -> {
                    return source.hasPermissionLevel(2);
                }
                case "essentials.god" -> {
                    return source.hasPermissionLevel(2);
                }
                case "essentials.god.others" -> {
                    return source.hasPermissionLevel(2);
                }
                case "essentials.speed" -> {
                    return source.hasPermissionLevel(2);
                }
                case "essentials.speed.others" -> {
                    return source.hasPermissionLevel(2);
                }
                case "essentials.gamemode" -> {
                    return source.hasPermissionLevel(2);
                }
                case "essentials.gamemode.others" -> {
                    return source.hasPermissionLevel(2);
                }
                case "essentials.teleport" -> {
                    return source.hasPermissionLevel(2);
                }
                case "essentials.teleport.others" -> {
                    return source.hasPermissionLevel(2);
                }
                case "essentials.spawn" -> {
                    return source.hasPermissionLevel(1);
                }
                case "essentials.spawn.others" -> {
                    return source.hasPermissionLevel(2);
                }
                case "essentials.give" -> {
                    return source.hasPermissionLevel(2);
                }
                case "essentials.give.others" -> {
                    return source.hasPermissionLevel(2);
                }
                case "essentials.repair" -> {
                    return source.hasPermissionLevel(2);
                }
                case "essentials.repair.others" -> {
                    return source.hasPermissionLevel(2);
                }
                case "essentials.time" -> {
                    return source.hasPermissionLevel(2);
                }
                case "essentials.weather" -> {
                    return source.hasPermissionLevel(2);
                }
                case "essentials.home" -> {
                    return source.hasPermissionLevel(0);
                }
                case "essentials.sethome" -> {
                    return source.hasPermissionLevel(0);
                }
                case "essentials.back" -> {
                    return source.hasPermissionLevel(0);
                }
                case "essentials.warp" -> {
                    return source.hasPermissionLevel(0);
                }
                case "essentials.setwarp" -> {
                    return source.hasPermissionLevel(2);
                }
                case "essentials.delwarp" -> {
                    return source.hasPermissionLevel(2);
                }
                case "essentials.kick" -> {
                    return source.hasPermissionLevel(3);
                }
                case "essentials.ban" -> {
                    return source.hasPermissionLevel(3);
                }
                default -> {
                    return source.hasPermissionLevel(defaultLevel);
                }
            }
        } catch (Exception e) {
            return source.hasPermissionLevel(defaultLevel);
        }
    }
}