package lg.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import lg.LGPlugin;
import lg.countdowns.GiveRoleCountdown;
import lg.countdowns.NightCountdown;
import lg.countdowns.VillageVoteCountdown;
import lg.roles.Role;
import lg.roles.RoleManager;
import lg.roles.Roles;
import lg.roles.Type;
import lg.roles.roles.LGBT;
import lg.roles.roles.Werewolf;
import lg.utils.Utils;
import net.minecraft.server.v1_14_R1.EntityLiving;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_14_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_14_R1.PacketPlayOutTitle.EnumTitleAction;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LGGame {

	//IMPORTANT LISTS
	public static List<Player> deadPlayers = new ArrayList<Player>();
	public static List<Player> toKill = new ArrayList<Player>();
	
	//Village vote
	public static List<Vote> votes = new ArrayList<Vote>();
	public static HashMap<Player, ArmorStand> voteDisplays = new HashMap<Player, ArmorStand>();
	public static List<Player> alreadyVoted = new ArrayList<Player>();
	
	//Informations or game stuff
	private static World inGameWorld;
	private static int nightCount = 0;
	private static int dayCount = 0;
	
	public static boolean villageVote = false;
	public static ItemStack cancelItem;
	
	public static void night()
	{
		LGPlugin.getStateManager().currentState = State.NIGHT;
		inGameWorld.setTime(18000);
		
		nightCount++;
		
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("§7========================§bNuit " + nightCount + "§7========================");
		Bukkit.broadcastMessage("");
		
		Bukkit.getOnlinePlayers().forEach(player -> {
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999999, 255, false, false));
			player.stopSound(Sound.MUSIC_DISC_STAL, SoundCategory.RECORDS);
			player.playSound(player.getLocation(), Sound.MUSIC_DISC_WARD, SoundCategory.RECORDS, 1f, 1f);
			player.getInventory().clear();
			Utils.hideOtherPlayers(player);
			
			Utils.sendTitle(player, EnumTitleAction.TITLE, "{\"text\":\"Le village s'endort\",\"color\":\"yellow\"}");
		});
		
		if(testForWinners())
			return;
		
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
			Collections.sort(nightRoles); // Apply right night role passing order
			new NightCountdown(nightRoles);
		}
	}
	
	public static void day() 
	{	
		RoleManager rm = LGPlugin.getRoleManager();

		dayCount++;
		
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("§7========================§eJour " + dayCount + "§7========================");
		Bukkit.broadcastMessage("");
		
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
		
		if(testForWinners())
			return;
		
		villageVote = true;
		
		//LGBT Power
		LGBT lgbt = (LGBT) LGPlugin.getRoleManager().getRole(Roles.LGBT);
		if(lgbt.target != null)
		{
			Vote v = new Vote(lgbt.target, 2);
			votes.add(v);
			updateDisplays(v);
			
			Bukkit.broadcastMessage(LGPlugin.PREFIX + "§c" + lgbt.target.getName() + " §ea été identifié comme un harceleur sexuel !");
			Bukkit.broadcastMessage(LGPlugin.PREFIX + "§eIl reçoit donc 2 votes par défaut.");
		}
		
		//Start village vote countdown
		new VillageVoteCountdown().runTaskTimer(LGPlugin.instance, 0, 20);
	}
	
	public static void start()
	{	
		cancelItem = new ItemStack(Material.BARRIER, 1);
		ItemMeta meta = cancelItem.getItemMeta();
		meta.setDisplayName("§cAnnuler le vote.");
		cancelItem.setItemMeta(meta);
		
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
	
	/*
	 * ============= WIN ===============
	 */
	
	public static boolean testForWinners()
	{
		List<Role> alive = new ArrayList<Role>();
		
		for(Role role : LGPlugin.getRoleManager().getRoles())
		{
			if(!role.isDead())
			{
				alive.add(role);
			}
		}
		
		boolean isSame = true;
		for(int i = 0 ; i < alive.size()-1 ; i++)
		{
			if(alive.get(i).getType() != alive.get(i+1).getType())
				isSame = false;
		}
		
		if(isSame)
		{
			Type type = alive.get(0).getType();
			String title = "{\"text\":\"ERROR !\"}";
			String subtitle = "{\"text\":\"ERROR !\"}";
			
			switch(type)
			{
			case VILLAGER:
				title = "{\"text\":\"Les Villageois\",\"color\":\"red\"}";
				subtitle = "{\"text\":\"l'emporte !\",\"color\":\"gray\"}";
				break;
			case WEREWOLF:
				Werewolf werewolf = (Werewolf) LGPlugin.getRoleManager().getRole(Roles.WEREWOLF);
				if(werewolf.getPlayers().size() > 1)
				{
					title = "{\"text\":\"Les Loups-Garous\",\"color\":\"red\"}";
					subtitle = "{\"text\":\"l'emporte !\",\"color\":\"gray\"}";
					break;
				} else
				{
					for(Player p : werewolf.getPlayers())
					{
						if(!LGGame.deadPlayers.contains(p))
						{
							if(werewolf.isWhiteWerewolf(p))
							{
								title = "{\"text\":\"Le Loup-Garou Blanc\",\"color\":\"red\"}";
								subtitle = "{\"text\":\"l'emporte !\",\"color\":\"gray\"}";
								break;
							}
						}
					}
				}
				break;
			case ALONE:
				if(LGPlugin.getRoleManager().getRoleID(alive.get(0)) == Roles.LGBT)
				{
					title = "{\"text\":\"Le LGBT\",\"color\":\"red\"}";
					subtitle = "{\"text\":\"l'emporte !\",\"color\":\"gray\"}";
				}
				break;
			default:
				System.err.println("FATAL ERROR !");
				title = "{\"text\":\"FATAL ERROR !\",\"color\":\"red\"}";
				subtitle = "{\"text\":\"No one wins\",\"color\":\"gray\"}";
				break;
			}
			
			for(Player p : Bukkit.getOnlinePlayers())
			{
				Utils.sendTitle(p, EnumTitleAction.TITLE, title);
				Utils.sendTitle(p, EnumTitleAction.SUBTITLE, subtitle);
			}
			return true;
		}
		return false;
	}

	/*
	 * ================= OTHERS ====================
	 */
	
	public static void updateDisplays(Vote vote)
	{
		Player pTarget = vote.getVoted();
		if(voteDisplays.containsKey(pTarget))
		{
			
			if(vote.getVotes() <= 0)
			{
				PacketPlayOutEntityDestroy packetR = new PacketPlayOutEntityDestroy(((CraftEntity) voteDisplays.get(pTarget)).getEntityId());
				Bukkit.getOnlinePlayers().forEach(player -> {
					((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetR);
				});
				return;
			}
			
			voteDisplays.get(pTarget).setCustomName("§eVotes : §c" + vote.getVotes());
			
			PacketPlayOutEntityDestroy packetR = new PacketPlayOutEntityDestroy(((CraftEntity) voteDisplays.get(pTarget)).getEntityId());
			PacketPlayOutSpawnEntityLiving packetS = new PacketPlayOutSpawnEntityLiving((EntityLiving) ((CraftEntity) voteDisplays.get(pTarget)).getHandle());
		
			Bukkit.getOnlinePlayers().forEach(player -> {
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetR);
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetS);
			});
		} else
		{
			ArmorStand armor = (ArmorStand) Utils.create(EntityType.ARMOR_STAND, pTarget.getLocation().add(0, 2.3, 0));
			armor.setInvulnerable(true);
			armor.setVisible(false);
			armor.setGravity(false);
			armor.setCustomNameVisible(true);
			armor.setBasePlate(false);
			armor.setMarker(true);
			armor.setCustomName("§eVotes : §c" + vote.getVotes());
			
			PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving((EntityLiving) ((CraftEntity) armor).getHandle());
			Bukkit.getOnlinePlayers().forEach(player -> {
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
			});
			
			voteDisplays.put(pTarget, armor);
		}
	}
	
	public static void clearVotes()
	{
		for(ArmorStand armor : voteDisplays.values())
		{
			PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(((CraftEntity) armor).getEntityId());
			Bukkit.getOnlinePlayers().forEach(player -> {
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
			});
		}
		
		voteDisplays.clear();
		votes.clear();
		alreadyVoted.clear();
	}
	
	//==========VILLAGE UTILS FUNCS============
	public static Vote getPlayerVoteStats(Player p)
	{
		for(Vote v : votes)
		{
			if(v.getVoted().getName().equalsIgnoreCase(p.getName()))
				return v;
		}
		return null;
	}
	
	public static Vote whoVotedPlayer(Player p)
	{
		for(Vote v : votes)
		{
			if(v.getVoters().contains(p))
				return v;
		}
		return null;
	}
	
	public static boolean playerHasBeenVoted(Player p)
	{
		for(Vote v : votes)
		{
			if(v.getVoted().getName().equalsIgnoreCase(p.getName()))
				return true;
		}
		return false;
	}

	public static Player getVoteResult() {
		Player p = (votes.isEmpty() || votes.size() == 1) ? null : votes.get(0).getVoted();
		int topVote = (votes.isEmpty() || votes.size() == 1) ? 0 : votes.get(0).getVotes();
		boolean equal = true;
		
		for(Vote v : votes)
		{
			if(topVote != v.getVotes() && topVote >= 0) equal = false;
			
			if(v.getVotes() > topVote)
			{
				topVote = v.getVotes();
				p = v.getVoted();
			}
		}
		
		if(equal)
		{
			//Returns null because we don't want to kill someone random
			// if no one voted or if there is no top vote.
			return null;
		}

		return p;
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
