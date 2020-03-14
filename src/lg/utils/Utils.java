package lg.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import lg.LGPlugin;
import lg.game.LGGame;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import net.minecraft.server.v1_14_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_14_R1.Packet;
import net.minecraft.server.v1_14_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_14_R1.PacketPlayOutTitle.EnumTitleAction;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Utils {

	public static Entity getNearestEntityInSight(Player player, int range) {
        ArrayList<Entity> entities = (ArrayList<Entity>) player.getNearbyEntities(range, range, range);
        ArrayList<Block> sightBlock = (ArrayList<Block>) player.getLineOfSight( (Set<Material>) null, range);
        ArrayList<Location> sight = new ArrayList<Location>();
        for (int i = 0;i<sightBlock.size();i++)
            sight.add(sightBlock.get(i).getLocation());
        for (int i = 0;i<sight.size();i++) {
            for (int k = 0;k<entities.size();k++) {
                if (Math.abs(entities.get(k).getLocation().getX()-sight.get(i).getX())<1.3) {
                    if (Math.abs(entities.get(k).getLocation().getY()-sight.get(i).getY())<1.5) {
                        if (Math.abs(entities.get(k).getLocation().getZ()-sight.get(i).getZ())<1.3) {
                            return entities.get(k);
                        }
                    }
                }
            }
        }
        return null;
    }
	
	public static void setConfigLoc(String path, Location loc)
	{
		FileConfiguration config = LGPlugin.instance.getConfig();
		config.set(path + ".world", loc.getWorld().getName());
		config.set(path + ".posX", loc.getX());
		config.set(path + ".posY", loc.getY());
		config.set(path + ".posZ", loc.getZ());
		config.set(path + ".pitch", loc.getPitch());
		config.set(path + ".yaw", loc.getYaw());
		LGPlugin.instance.saveConfig();
	}
	
	public static Location getConfigLoc(String path) {
		FileConfiguration config = LGPlugin.instance.getConfig();
		World world = Bukkit.getWorld(config.getString(path + ".world"));
		double posX = config.getDouble(path + ".posX");
		double posY = config.getDouble(path + ".posY");
		double posZ = config.getDouble(path + ".posZ");
		float pitch = (float) config.getDouble(path + ".pitch");
		float yaw = (float) config.getDouble(path + ".yaw");
		Location loc = new Location(world, posX, posY, posZ, yaw, pitch);
		return loc;
	}
	
	public static void sendTitle(Player p, EnumTitleAction type, String json)
	{
		PacketPlayOutTitle packetTitle = new PacketPlayOutTitle(type, ChatSerializer.a(json));
		EntityPlayer player = ((CraftPlayer) p).getHandle();
		player.playerConnection.sendPacket(packetTitle);
	}
	
	@SuppressWarnings("deprecation")
	public static void hideOtherPlayers(Player p)
	{
		Bukkit.getOnlinePlayers().forEach(player -> {
			if(!player.getName().equalsIgnoreCase(p.getName())) p.hidePlayer(player);
		});
	}
	
	@SuppressWarnings("deprecation")
	public static void showOtherPlayers(Player p)
	{
		Bukkit.getOnlinePlayers().forEach(player -> {
			if(!player.getName().equalsIgnoreCase(p.getName())
					&& !LGGame.deadPlayers.contains(player)) p.showPlayer(player);
		});
	}

	public static void sendPacketToAllPlayer(@SuppressWarnings("rawtypes") Packet infoP) {
		Bukkit.getOnlinePlayers().forEach(player -> {
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(infoP);
		});
	}
	
	public static Entity create(EntityType entityType, Location location) {
        try {
            Class<?> craftWorldClass = getNMSClass("org.bukkit.craftbukkit.", "CraftWorld");

            Object craftWorldObject = craftWorldClass.cast(location.getWorld());

            Method createEntityMethod = craftWorldObject.getClass().getMethod("createEntity", Location.class, Class.class);
            Object entity = createEntityMethod.invoke(craftWorldObject, location, entityType.getEntityClass());

            return (Entity) entity.getClass().getMethod("getBukkitEntity").invoke(entity);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
        
        return null;
    }
	
	public static void wait(int timeSeconds)
	{
		try {
			TimeUnit.SECONDS.wait(timeSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static Class<?> getNMSClass(String prefix, String nmsClassString) throws ClassNotFoundException {
       String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";

        String name = prefix + version + nmsClassString;
        return Class.forName(name);
    }

}
