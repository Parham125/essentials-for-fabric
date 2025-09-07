# Essentials for Fabric

A Minecraft Fabric mod that provides essential server commands similar to EssentialsX for Spigot/Paper servers.

## Requirements

- Java 17 or higher (Java 21+ for 1.21.1 support)
- Gradle (for building)
- Minecraft 1.20.1 (1.21.1 support coming soon)
- Fabric Loader 0.14.21+
- Fabric API 0.83.0+

## Installation & Setup

### 1. Install Java 17 and Gradle

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install openjdk-17-jdk gradle
```

**Windows:**
- Download and install Java 17 from [Adoptium](https://adoptium.net/)
- Download and install Gradle from [gradle.org](https://gradle.org/install/)

**macOS:**
```bash
brew install openjdk@17 gradle
```

### 2. Building the Mod

```bash
# Clone or download the project
cd essentialsforfabric

# Make gradlew executable (Linux/macOS)
chmod +x gradlew

# Build the mod
./gradlew build

# The built .jar file will be in build/libs/
```

### 3. Installing the Mod

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 1.20.1
2. Download [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api) for 1.20.1
3. Copy both the Fabric API and `essentials-for-fabric-0.1.0-beta-mc1.20.1.jar` to your `mods/` folder
4. Start your Minecraft server

## Available Commands

### Basic Utility Commands
- `/heal [player]` - Restore health and hunger (Permission: `essentials.heal`)
- `/feed [player]` - Restore hunger only (Permission: `essentials.feed`)
- `/fly [player]` - Toggle flight ability (Permission: `essentials.fly`)
- `/god [player]` - Toggle invincibility mode (Permission: `essentials.god`)
- `/speed <amount> [player]` - Set movement speed (Permission: `essentials.speed`)

### Gamemode Commands
- `/gmc [player]` - Set Creative mode (Permission: `essentials.gamemode`)
- `/gms [player]` - Set Survival mode (Permission: `essentials.gamemode`)
- `/gma [player]` - Set Adventure mode (Permission: `essentials.gamemode`)
- `/gmsp [player]` - Set Spectator mode (Permission: `essentials.gamemode`)

### Teleportation Commands
- `/tp <player>` - Teleport to player (Permission: `essentials.teleport`)
- `/tphere <player>` - Teleport player to you (Permission: `essentials.teleport.others`)
- `/spawn [player]` - Teleport to spawn (Permission: `essentials.spawn`)
- `/back` - Return to previous location (Permission: `essentials.back`)

### Home System
- `/home [name]` - Teleport to home (Permission: `essentials.home`)
- `/sethome [name]` - Set a home location (Permission: `essentials.sethome`)
- `/delhome <name>` - Delete a home (Permission: `essentials.sethome`)

### Warp System
- `/warp [name]` - List warps or teleport to warp (Permission: `essentials.warp`)
- `/setwarp <name>` - Create a warp (Permission: `essentials.setwarp`)
- `/delwarp <name>` - Delete a warp (Permission: `essentials.delwarp`)

### Item & World Commands
- `/give <item> [count] [player]` - Give items (Permission: `essentials.give`)
- `/repair [player]` - Repair held item (Permission: `essentials.repair`)
- `/time set <day|night|noon|midnight|number>` - Set time (Permission: `essentials.time`)
- `/time add <number>` - Add time (Permission: `essentials.time`)
- `/weather <clear|rain|thunder>` - Control weather (Permission: `essentials.weather`)

### Moderation Commands
- `/kick <player> [reason]` - Kick a player (Permission: `essentials.kick`)
- `/ban <player> [reason]` - Ban a player (Permission: `essentials.ban`)

## Permission System

The mod uses an EssentialsX-style permission system with default operator levels:

- **Level 0** (All Players): `/home`, `/sethome`, `/back`, `/spawn`, `/warp`
- **Level 2** (Operators): Most utility commands (`/heal`, `/fly`, `/give`, etc.)
- **Level 3** (Admins): Moderation commands (`/kick`, `/ban`)
- **Level 4** (Server Owners): All permissions automatically granted

## Data Storage

Player data (homes, last locations) and server warps are automatically saved to:
- `world/essentials/playerdata/<uuid>.json` - Individual player data
- `world/essentials/warps.json` - Server-wide warps

## Development

To modify or extend the mod:

1. Import the project in your IDE (IntelliJ IDEA recommended)
2. Use `./gradlew genSources` to generate Minecraft source code
3. Make your changes
4. Test with `./gradlew runServer` (for dedicated server testing)
5. Build with `./gradlew build`

## License

This project is licensed under the MIT License.