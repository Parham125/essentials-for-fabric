package com.essentialsforfabric.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.WorldSavePath;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<UUID, PlayerData> playerDataCache = new HashMap<>();
    private static final Map<String, WarpData> warps = new HashMap<>();

    public static class PlayerData {
        public Map<String, LocationData> homes = new HashMap<>();
        public LocationData lastLocation;

        public PlayerData() {
            this.homes = new HashMap<>();
        }
    }

    public static class LocationData {
        public String world;
        public double x, y, z;
        public float yaw, pitch;

        public LocationData(String world, double x, double y, double z, float yaw, float pitch) {
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
        }
    }

    public static class WarpData extends LocationData {
        public WarpData(String world, double x, double y, double z, float yaw, float pitch) {
            super(world, x, y, z, yaw, pitch);
        }
    }

    public static PlayerData getPlayerData(UUID playerId) {
        return playerDataCache.computeIfAbsent(playerId, k -> new PlayerData());
    }

    public static void setHome(ServerPlayerEntity player, String homeName) {
        PlayerData data = getPlayerData(player.getUuid());
        LocationData location = new LocationData(
            player.getWorld().getRegistryKey().getValue().toString(),
            player.getX(), player.getY(), player.getZ(),
            player.getYaw(), player.getPitch()
        );
        data.homes.put(homeName.toLowerCase(), location);
        savePlayerData(player.getServer(), player.getUuid());
    }

    public static LocationData getHome(UUID playerId, String homeName) {
        PlayerData data = getPlayerData(playerId);
        return data.homes.get(homeName.toLowerCase());
    }

    public static void setLastLocation(ServerPlayerEntity player) {
        PlayerData data = getPlayerData(player.getUuid());
        data.lastLocation = new LocationData(
            player.getWorld().getRegistryKey().getValue().toString(),
            player.getX(), player.getY(), player.getZ(),
            player.getYaw(), player.getPitch()
        );
        savePlayerData(player.getServer(), player.getUuid());
    }

    public static LocationData getLastLocation(UUID playerId) {
        PlayerData data = getPlayerData(playerId);
        return data.lastLocation;
    }

    public static void setWarp(String name, ServerPlayerEntity player) {
        WarpData warp = new WarpData(
            player.getWorld().getRegistryKey().getValue().toString(),
            player.getX(), player.getY(), player.getZ(),
            player.getYaw(), player.getPitch()
        );
        warps.put(name.toLowerCase(), warp);
        saveWarps(player.getServer());
    }

    public static WarpData getWarp(String name) {
        return warps.get(name.toLowerCase());
    }

    public static Map<String, WarpData> getAllWarps() {
        return new HashMap<>(warps);
    }

    public static void deleteWarp(String name) {
        warps.remove(name.toLowerCase());
    }

    private static void savePlayerData(MinecraftServer server, UUID playerId) {
        try {
            Path dataDir = server.getSavePath(WorldSavePath.ROOT).resolve("essentials").resolve("playerdata");
            Files.createDirectories(dataDir);

            Path playerFile = dataDir.resolve(playerId + ".json");
            PlayerData data = playerDataCache.get(playerId);

            try (FileWriter writer = new FileWriter(playerFile.toFile())) {
                GSON.toJson(data, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveWarps(MinecraftServer server) {
        try {
            Path dataDir = server.getSavePath(WorldSavePath.ROOT).resolve("essentials");
            Files.createDirectories(dataDir);

            Path warpsFile = dataDir.resolve("warps.json");

            try (FileWriter writer = new FileWriter(warpsFile.toFile())) {
                GSON.toJson(warps, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadPlayerData(MinecraftServer server, UUID playerId) {
        try {
            Path playerFile = server.getSavePath(WorldSavePath.ROOT).resolve("essentials").resolve("playerdata").resolve(playerId + ".json");

            if (Files.exists(playerFile)) {
                try (FileReader reader = new FileReader(playerFile.toFile())) {
                    PlayerData data = GSON.fromJson(reader, PlayerData.class);
                    if (data != null) {
                        playerDataCache.put(playerId, data);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadWarps(MinecraftServer server) {
        try {
            Path warpsFile = server.getSavePath(WorldSavePath.ROOT).resolve("essentials").resolve("warps.json");

            if (Files.exists(warpsFile)) {
                try (FileReader reader = new FileReader(warpsFile.toFile())) {
                    Type type = new TypeToken<Map<String, WarpData>>(){}.getType();
                    Map<String, WarpData> loadedWarps = GSON.fromJson(reader, type);
                    if (loadedWarps != null) {
                        warps.putAll(loadedWarps);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}