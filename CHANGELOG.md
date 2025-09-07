# Changelog

## Version 0.1.0-beta - Initial Beta Release

### Features Added
- **22 Essential Commands** covering all major server administration needs
- **EssentialsX-style Permission System** with proper operator level defaults
- **Persistent Data Storage** for homes and warps using JSON files
- **Player Home System** - Set, teleport to, and manage multiple homes
- **Server Warp System** - Create global warps accessible to all players
- **Back Command** - Return to previous location after teleporting
- **Comprehensive Moderation Tools** - Kick and ban with reasons

### Command Categories

#### Basic Utility (12 commands)
- `/heal` - Restore health and hunger
- `/feed` - Restore hunger only  
- `/fly` - Toggle flight ability
- `/god` - Toggle invincibility mode
- `/speed <amount>` - Set movement speed
- `/gmc/gms/gma/gmsp` - Gamemode shortcuts
- `/repair` - Repair held items
- `/give <item> [count]` - Give items with proper grammar
- `/time set/add` - Control world time
- `/weather <type>` - Control weather

#### Teleportation (5 commands)
- `/tp <player>` - Teleport to player
- `/tphere <player>` - Teleport player to you
- `/spawn` - Teleport to spawn
- `/back` - Return to previous location
- `/home/sethome/delhome` - Home management

#### Server Management (5 commands)
- `/warp/setwarp/delwarp` - Warp system
- `/kick <player> [reason]` - Kick players
- `/ban <player> [reason]` - Ban players

### Technical Features
- **Permission System**: EssentialsX-compatible permissions with appropriate defaults
- **Data Persistence**: JSON storage for homes and warps
- **Multi-world Support**: Works across dimensions
- **Error Handling**: Comprehensive error messages and validation
- **Performance Optimized**: Lightweight with minimal server impact

### Bug Fixes
- Fixed `/give` command showing "Air" instead of proper item names
- Fixed grammar in command feedback messages ("Gave" instead of "Given")
- Fixed command registration requiring proper CommandRegistryAccess

### Server Requirements
- Minecraft 1.20.1 (1.21.1 support planned)
- Fabric Loader 0.14.21+
- Fabric API 0.83.0+
- Java 17+

### Permissions Overview
- **Level 0** (All Players): Home/warp usage, back command
- **Level 2** (Operators): Utility commands, item/world management  
- **Level 3** (Admins): Moderation commands
- **Level 4** (Owners): All permissions