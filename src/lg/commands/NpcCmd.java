package lg.commands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lg.LGPlugin;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import net.minecraft.server.v1_14_R1.MinecraftServer;
import net.minecraft.server.v1_14_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_14_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_14_R1.PacketPlayOutPosition;
import net.minecraft.server.v1_14_R1.PacketPlayOutPosition.EnumPlayerTeleportFlags;
import net.minecraft.server.v1_14_R1.PlayerConnection;
import net.minecraft.server.v1_14_R1.PlayerInteractManager;
import net.minecraft.server.v1_14_R1.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_14_R1.CraftServer;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;

public class NpcCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String msg, String[] args) {

		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			if(args.length > 0 && args.length < 2)
			{
				Location loc = p.getLocation();
				
				MinecraftServer server = ((CraftServer) LGPlugin.instance.getServer()).getServer();
				WorldServer world = ((CraftWorld) p.getWorld()).getHandle();
				EntityPlayer npc = new EntityPlayer(server, world, new GameProfile(UUID.randomUUID(), args[0]), new PlayerInteractManager(world));
				PacketPlayOutNamedEntitySpawn packetSpawn = new PacketPlayOutNamedEntitySpawn(npc);
				PacketPlayOutPlayerInfo packetInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc);
				
				Set<EnumPlayerTeleportFlags> flags = new HashSet<EnumPlayerTeleportFlags>(Arrays.asList(new EnumPlayerTeleportFlags[] {EnumPlayerTeleportFlags.X, EnumPlayerTeleportFlags.Y, EnumPlayerTeleportFlags.Z, EnumPlayerTeleportFlags.X_ROT, EnumPlayerTeleportFlags.Y_ROT}));
				PacketPlayOutPosition packetMove = new PacketPlayOutPosition(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), flags, 1);
				npc.playerConnection = new PlayerConnection(server, ((CraftPlayer) p).getHandle().playerConnection.networkManager, npc);
				npc.playerConnection.sendPacket(packetMove);
				
				Bukkit.getOnlinePlayers().forEach(player -> {
					EntityPlayer playerHandle = ((CraftPlayer) player).getHandle();
					playerHandle.playerConnection.sendPacket(packetInfo);
					playerHandle.playerConnection.sendPacket(packetSpawn);
				});
				
				p.sendMessage("§aLe joueur a été créée.");
			}
		}
		
		return true;
	}
	
}
