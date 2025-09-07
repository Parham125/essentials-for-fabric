# Essentials for Fabric

**The definitive server management mod for Fabric - bringing EssentialsX functionality to Minecraft 1.20.1!**

Transform your Fabric server with 22+ essential commands, home/warp systems, and comprehensive moderation tools. Perfect for server administrators who want powerful, reliable server management without the complexity.

## 🌟 Key Features

### 🏠 **Player Home System**
- **Multiple Homes**: Players can set multiple named homes (`/sethome kitchen`, `/sethome base`)
- **Easy Navigation**: Quick teleportation with `/home [name]`
- **Management Tools**: Delete unwanted homes with `/delhome <name>`

### 🌐 **Server Warp System** 
- **Global Warps**: Admins create server-wide teleport points (`/setwarp spawn`)
- **Player Access**: All players can use `/warp <name>` to teleport
- **Warp Management**: List all warps with `/warp`, delete with `/delwarp <name>`

### 🔄 **Smart Back System**
- **Previous Location**: Never lose your way with `/back`
- **Auto-tracking**: Automatically saves location before teleports
- **Multi-dimensional**: Works across Nether, End, and Overworld

### ⚡ **Essential Utility Commands**
- **Health & Hunger**: `/heal` and `/feed` for instant restoration
- **Flight Control**: `/fly` toggle for creative-style flight
- **God Mode**: `/god` for invincibility during builds
- **Speed Control**: `/speed <1-10>` for faster movement
- **Item Repair**: `/repair` to fix damaged tools instantly

### 🎮 **Gamemode Shortcuts**
- **Quick Switching**: `/gmc`, `/gms`, `/gma`, `/gmsp`
- **Player Targeting**: Change other players' gamemodes
- **Instant Response**: No more typing `/gamemode creative`

### 🛠️ **Server Management**
- **Item Distribution**: `/give <item> [count] [player]` with proper grammar
- **World Control**: `/time` and `/weather` commands
- **Spawn Management**: `/spawn` and `/tphere` for player positioning

### 👮 **Moderation Tools**
- **Player Kicks**: `/kick <player> [reason]` with broadcast
- **Player Bans**: `/ban <player> [reason]` with permanent enforcement
- **Reason Support**: Optional custom reasons for all mod actions

## 🔐 Permission System

**EssentialsX-Compatible Permissions**

| Permission Level | Commands Available | Player Type |
|------------------|-------------------|-------------|
| **Level 0** | `/home`, `/sethome`, `/back`, `/spawn`, `/warp` | All Players |
| **Level 2** | Utility commands, `/give`, `/repair`, `/time`, `/weather` | Operators |
| **Level 3** | `/kick`, `/ban` | Administrators |
| **Level 4** | All permissions | Server Owners |

**Specific Permissions:**
- `essentials.home` - Home system access
- `essentials.warp` - Warp system access  
- `essentials.back` - Back command access
- `essentials.fly` - Flight toggle ability
- `essentials.god` - God mode access
- `essentials.give` - Item giving permissions
- `essentials.kick` - Player kick ability
- `essentials.ban` - Player ban ability
- And many more...

## 💾 Data Persistence

**Automatic Save System**
- **Player Data**: Homes and last locations saved to `world/essentials/playerdata/<uuid>.json`
- **Server Warps**: Global warps saved to `world/essentials/warps.json`
- **Auto-Loading**: Data loads automatically on server start and player join
- **JSON Format**: Human-readable, easy to backup and restore

## 🚀 Installation

1. **Download Requirements**:
   - [Fabric Loader](https://fabricmc.net/use/) for Minecraft 1.20.1
   - [Fabric API](https://modrinth.com/mod/fabric-api) 0.83.0+

2. **Install the Mod**:
   - Place `essentials-for-fabric-0.1.0-beta.jar` in your `mods/` folder
   - Include Fabric API in the same folder
   - Start your server

3. **First Setup**:
   - All commands work immediately with default permissions
   - Players can start using `/home` and `/warp` right away
   - Operators get access to all utility commands

## 📋 Complete Command List

### **Player Commands (Level 0)**
```
/home [name] - Teleport to home
/sethome [name] - Set home location  
/delhome <name> - Delete home
/back - Return to previous location
/spawn - Teleport to spawn
/warp [name] - List warps or teleport to warp
```

### **Operator Commands (Level 2)**
```
/heal [player] - Restore health and hunger
/feed [player] - Restore hunger only
/fly [player] - Toggle flight ability
/god [player] - Toggle invincibility
/speed <amount> [player] - Set movement speed (0.1-10.0)
/gmc/gms/gma/gmsp [player] - Gamemode shortcuts
/give <item> [count] [player] - Give items
/repair [player] - Repair held item
/time set/add <value> - Control world time
/weather <clear/rain/thunder> - Control weather
/tp <player> - Teleport to player
/tphere <player> - Teleport player to you
/setwarp <name> - Create server warp
/delwarp <name> - Delete server warp
```

### **Admin Commands (Level 3+)**
```
/kick <player> [reason] - Kick player with reason
/ban <player> [reason] - Ban player with reason
```

## 🆚 Why Choose Essentials for Fabric?

### **vs Other Server Mods:**
- ✅ **Lightweight**: Minimal performance impact
- ✅ **Complete**: 22+ commands covering all needs
- ✅ **Reliable**: Comprehensive error handling
- ✅ **Familiar**: EssentialsX-style commands and permissions

### **vs Bukkit/Spigot EssentialsX:**
- ✅ **Native Fabric**: No server software change needed
- ✅ **Modern**: Built for Minecraft 1.20.1 (1.21.1 support coming soon)
- ✅ **Integrated**: Works seamlessly with other Fabric mods
- ✅ **Performance**: Fabric's optimized command system

## 🐛 Beta Notice

This is a **beta release** - please report any issues on [GitHub](https://github.com/essentialsforfabric/essentials-for-fabric/issues). 

**Known Limitations:**
- Economy system not yet implemented
- Kit system planned for future release
- Some advanced EssentialsX features coming soon

## 🤝 Support & Community

- **Bug Reports**: [GitHub Issues](https://github.com/essentialsforfabric/essentials-for-fabric/issues)
- **Feature Requests**: GitHub Discussions
- **Documentation**: Full README on GitHub
- **Source Code**: Available under MIT License

---

*Transform your Fabric server today with the power of essential commands!*