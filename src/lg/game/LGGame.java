package lg.game;

import java.util.ArrayList;
import java.util.List;

import lg.LGPlugin;
import lg.countdowns.GiveRoleCountdown;
import lg.countdowns.NightCountdown;
import lg.roles.Role;
import lg.roles.RoleManager;
import lg.utils.Utils;
import net.minecraft.server.v1_14_R1.PacketPlayOutTitle.EnumTitleAction;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LGGame {

	public static List<Player> deadPlayers = new ArrayList<Player>();
	public static List<Player> toKill = new ArrayList<Player>();
	
	private static World inGameWorld;
	private static int nightCount = 0;
	
	public static void night()
	{
		LGPlugin.getStateManager().currentState = State.NIGHT;
		inGameWorld.setTime(18000);
		
		nightCount++;
		Bukkit.getOnlinePlayers().forEach(player -> {
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999999, 255, false, false));
			player.stopSound(Sound.MUSIC_DISC_STAL, SoundCategory.RECORDS);
			player.playSound(player.getLocation(), Sound.MUSIC_DISC_WARD, SoundCategory.RECORDS, 1f, 1f);
			Utils.hideOtherPlayers(player);
			
			Utils.sendTitle(player, EnumTitleAction.TITLE, "{\"text\":\"Le village s'endort\",\"color\":\"yellow\"}");
			Utils.sendTitle(player, EnumTitleAction.ACTIONBAR, "[{\"text\":\"Nuit \",\"color\":\"gold\"},{\"text\":\"" + nightCount + "\",\"color\":\"red\"}]");
		});
		
		List<Role> nightRoles = new ArrayList<Role>();
		for(Role role : LGPlugin.getRoleManager().getRoles())
		{
			if(role.isActivatedDuringNight() && !role.isDead())
			{
				nightRoles.add(role);
			}
		}
		if(!nightRoles.isEmpty())
		{
			new NightCountdown(nightRoles);
		}
		
	}
	
	public static void day() 
	{
		RoleManager rm = LGPlugin.getRoleManager();
		
		LGPlugin.getStateManager().currentState = State.DAY;
		inGameWorld.setTime(6000);
		
		if(toKill.isEmpty())
		{
			Bukkit.broadcastMessage(LGPlugin.PREFIX + "§eLe village se réveille et... Personne n'est mort cette nuit.");
		} else
		{
			Bukkit.broadcastMessage(LGPlugin.PREFIX + "§eLe village se réveille et...");
		}
		
		//kills everyone
		for(int i = 0 ; i < toKill.size() ; i++)
		{
			rm.kill(toKill.get(i));
		}
		toKill.clear();
		
		//Applying day setup
		Bukkit.getOnlinePlayers().forEach(player -> {
			player.removePotionEffect(PotionEffectType.BLINDNESS);
			player.stopSound(Sound.MUSIC_DISC_WARD, SoundCategory.RECORDS);
			player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1f, 1f);
			player.playSound(player.getLocation(), Sound.MUSIC_DISC_STAL, SoundCategory.RECORDS, 1f, 1f);
			
			Utils.showOtherPlayers(player);
			Utils.sendTitle(player, EnumTitleAction.TITLE, "{\"text\":\"Le village se réveille.\",\"color\":\"yellow\"}");
		});
	}
	
	public static void start()
	{	
		LGPlugin.getStateManager().currentState = State.NIGHT;
		inGameWorld = Bukkit.getWorld(LGPlugin.instance.getConfig().getString("setting.spawn1.world"));
		
		teleportEveryone();
		LGPlugin.getRoleManager().giveRoles();
		
		Bukkit.getOnlinePlayers().forEach(player -> {
			player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
			player.getInventory().clear();
			player.setGameMode(GameMode.SURVIVAL);
		});
		
		//Will automatically call night to start the first night
		new GiveRoleCountdown().runTaskTimer(LGPlugin.instance, 0, 20);
	}

	private static void teleportEveryone() {
		FileConfiguration config = LGPlugin.instance.getConfig();
		int gameSize = config.getInt("setting.gamesize");
		Player[] player = Bukkit.getOnlinePlayers().toArray(new Player[gameSize]);
		for(int i = 0 ; i < gameSize ; i++)
		{
			if(player[i] != null)
				player[i].teleport(Utils.getConfigLoc("setting.spawn" + (i+1)));
		}
	}
	
}
