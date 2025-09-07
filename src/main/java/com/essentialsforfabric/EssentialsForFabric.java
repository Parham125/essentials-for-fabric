package com.essentialsforfabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import com.essentialsforfabric.commands.*;
import com.essentialsforfabric.data.PlayerDataManager;

public class EssentialsForFabric implements ModInitializer {
    public static final String MOD_ID = "essentials-for-fabric";

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            HealCommand.register(dispatcher);
            FeedCommand.register(dispatcher);
            FlyCommand.register(dispatcher);
            GodCommand.register(dispatcher);
            SpeedCommand.register(dispatcher);
            GamemodeCommands.register(dispatcher);
            TeleportCommands.register(dispatcher);
            GiveCommand.register(dispatcher, registryAccess);
            RepairCommand.register(dispatcher);
            TimeCommand.register(dispatcher);
            WeatherCommand.register(dispatcher);
            SpawnCommand.register(dispatcher);
            HomeCommands.register(dispatcher);
            BackCommand.register(dispatcher);
            WarpCommands.register(dispatcher);
            KickBanCommands.register(dispatcher);
        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            PlayerDataManager.loadWarps(server);
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            PlayerDataManager.loadPlayerData(server, handler.player.getUuid());
        });
    }
}