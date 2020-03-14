package lg.roles.roles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lg.LGPlugin;
import lg.game.Vote;
import lg.roles.Role;
import lg.roles.Roles;
import lg.roles.Type;
import lg.utils.Utils;
import net.minecraft.server.v1_14_R1.EntityLiving;
import net.minecraft.server.v1_14_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_14_R1.PacketPlayOutChat;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_14_R1.PacketPlayOutSpawnEntityLiving;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

public class Werewolf extends Role {

	public List<Vote> votes = new ArrayList<Vote>();
	public HashMap<Player, ArmorStand> displays = new HashMap<Player, ArmorStand>();
	
	public List<Player> alreadyVoted = new ArrayList<Player>();
	
	private HashMap<Player, ArmorStand> indicators = new HashMap<Player, ArmorStand>();
	
	//===========IPDL===========
	public Player ipdl = null;
	
	public boolean canIPDLUse = true;
	public static ItemStack ipdlItem;
	
	//===========ANO===========
	
	public Player anonyme = null;
	
	public static ItemStack anoItem;
	
	//=========WHITE WOLF=======
	public Player whiteWerewolf = null;
	public boolean wwCanUse = false;
	
	public static int wwNightCount = 0;
	public static ItemStack wwItem;
	
	public Werewolf() {
		super("Loup-Garou", 1,
				true, false, false,
				Type.WEREWOLF,
				Sound.ENTITY_WOLF_HOWL,
				PotionEffectType.BAD_OMEN,
				"Vous devez éliminer tous les innocents (ceux qui ne sont pas loup-garou). Chaque nuit, vous vous réunirez afin de déterminer qui sera tué. Vous ne devez pas vous faire remarquer.",
				4);
		
		this.item = new ItemStack(Material.SPIDER_SPAWN_EGG, 1);
		ItemMeta itM = item.getItemMeta();
		itM.setDisplayName("§eLoup-Garou");
		this.item.setItemMeta(itM);
		
		ipdlItem = new ItemStack(Material.WITHER_SKELETON_SPAWN_EGG, 1);
		ItemMeta ipdlM = ipdlItem.getItemMeta();
		ipdlM.setDisplayName("§eInfect père des loups.");
		ipdlItem.setItemMeta(ipdlM);
		
		anoItem = new ItemStack(Material.NAME_TAG, 1);
		ItemMeta anoM = anoItem.getItemMeta();
		anoM.setDisplayName("§eLoup Anonyme");
		anoItem.setItemMeta(anoM);
		
		wwItem = new ItemStack(Material.GHAST_SPAWN_EGG, 1);
		ItemMeta wMeta = wwItem.getItemMeta();
		wMeta.setDisplayName("§eLoup-Garou blanc");
		wwItem.setItemMeta(wMeta);
	}

	@Override
	public void onActivation(Player p) {
		canUse = true;
		p.sendMessage(LGPlugin.PREFIX + "§eVous devez voter pour choisir une cible.");
	}
	
	public void onIPDLActivation()
	{
		String json = "[{" + LGPlugin.JSON_PREFIX + "\"text\":\"Vous pouvez infecter un joueur mort. \",\"color\":\"yellow\"},{\"text\":\"[INFECTER]\",\"color\":\"green\",\"bold\":\"true\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/lg-ipdl\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Infecter quelqu'un.\"}}]";
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a(json));
		((CraftPlayer) ipdl).getHandle().playerConnection.sendPacket(packet);
	
		ipdl.playSound(ipdl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
	}
	
	public void onLGBActivation()
	{
		wwCanUse = true;
		
		String json = "[{" + LGPlugin.JSON_PREFIX + "\"text\":\"Vous pouvez choisir un loup-garou a eliminé. \",\"color\":\"yellow\"},{\"text\":\"[ELIMINER UN LOUP-GAROU]\",\"color\":\"green\",\"bold\":\"true\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/lg-lgb\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Eliminer un Loup-Garou.\"}}]";
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a(json));
		((CraftPlayer) whiteWerewolf).getHandle().playerConnection.sendPacket(packet);
	
		whiteWerewolf.playSound(whiteWerewolf.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
	}

	@Override
	public void onStart(Player p) {
		if(isIPDL(p))
		{
			p.sendMessage(LGPlugin.PREFIX + "§eVous êtes §cInfect père des loups§e.");
			p.sendMessage("§7 - Vous devez éliminer tous les innocents (ceux qui ne sont pas loup-garou). Une fois dans la partie, vous pouvez décider d'infecter un villageois tué par les loup-garou, ce dernier passera dans le camp des loup-garou. L'infecté gardera les facultés de son rôle");
		} else if(isAnonyme(p))
		{
			p.sendMessage(LGPlugin.PREFIX + "§eVous êtes §cLoup Anonyme§e.");
			p.sendMessage("§7 - Vous faites parti du camp des loups-garou, si vous vous faite espionner par la voyante, elle verra que vous êtes simple villageois.");
		} else if (isWhiteWerewolf(p))
		{
			p.sendMessage(LGPlugin.PREFIX + "§eVous êtes §cLoup-Garou Blanc§e.");
			p.sendMessage("§7 - Vous devez gagner SEUL. Toutes les deux nuits, vous pouvez éliminer un loup garou. Les autres compères loup garou pensent que vous êtes aussi Loup Garou simple.");
		} else
		{
			p.sendMessage(LGPlugin.PREFIX + "§eVous êtes §c" + name + "§e.");
			p.sendMessage("§7 - " + getDesc());
		}
		
		spawnIndicators();
		
		StringBuilder lgList = new StringBuilder();
		for(Player player : players)
		{
			lgList.append(player.getName() + ", ");
		}
		String toPrint = "§c" + lgList.toString().trim();
		p.sendMessage("§7Les loups-garous sont : ");
		p.sendMessage(toPrint.substring(0, toPrint.length() - 1));
	}

	public void updateDisplay(Vote vote) {
		Player pTarget = vote.getVoted();
		if(displays.containsKey(pTarget))
		{
			
			if(vote.getVotes() <= 0)
			{
				PacketPlayOutEntityDestroy packetR = new PacketPlayOutEntityDestroy(((CraftEntity) displays.get(pTarget)).getEntityId());
				players.forEach(player -> {
					((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetR);
				});
				return;
			}
			
			displays.get(pTarget).setCustomName("§eVotes : §c" + vote.getVotes());
			
			PacketPlayOutEntityDestroy packetR = new PacketPlayOutEntityDestroy(((CraftEntity) displays.get(pTarget)).getEntityId());
			PacketPlayOutSpawnEntityLiving packetS = new PacketPlayOutSpawnEntityLiving((EntityLiving) ((CraftEntity) displays.get(pTarget)).getHandle());
		
			players.forEach(player -> {
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
			players.forEach(player -> {
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
			});
			
			displays.put(pTarget, armor);
		}
	}
	
	public void clear()
	{
		for(ArmorStand armor : displays.values())
		{
			PacketPlayOutEntityDestroy packetR = new PacketPlayOutEntityDestroy(((CraftEntity) armor).getEntityId());
		
			players.forEach(player -> {
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetR);
			});
		}
		
		displays.clear();
		alreadyVoted.clear();
		votes.clear();
	}
	
	public void onWerewolfDeath(Player p)
	{
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(((CraftEntity) indicators.get(p)).getEntityId());
		players.forEach(player -> ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet));
		indicators.remove(p);
	}
	
	public Vote getPlayerVoteStats(Player p)
	{
		for(Vote v : votes)
		{
			if(v.getVoted().getName().equalsIgnoreCase(p.getName()))
				return v;
		}
		return null;
	}
	
	public Vote whoVotedPlayer(Player p)
	{
		for(Vote v : votes)
		{
			if(v.getVoters().contains(p))
				return v;
		}
		return null;
	}
	
	public boolean playerHasBeenVoted(Player p)
	{
		for(Vote v : votes)
		{
			if(v.getVoted().getName().equalsIgnoreCase(p.getName()))
				return true;
		}
		return false;
	}

	public Player getVoteResult() {
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
		
		//If the player is protected by the salvator then cancel vote result.
		Salvator salva = (Salvator) LGPlugin.getRoleManager().getRole(Roles.SALVATOR);
		if(salva != null && salva.protectedPlayer != null)
			return salva.protectedPlayer.getName().equalsIgnoreCase(p.getName()) ? null : p;
		else
			return p;
	}
	
	//==========OTHERS==========
	
	public void spawnIndicators()
	{
		for(Player player : players)
		{
			ArmorStand armor = (ArmorStand) Utils.create(EntityType.ARMOR_STAND, player.getLocation().add(0, 2, 0));
			armor.setInvulnerable(true);
			armor.setVisible(false);
			armor.setGravity(false);
			armor.setCustomNameVisible(true);
			armor.setBasePlate(false);
			armor.setMarker(true);
			armor.setCustomName("§cLoup-Garou");
			
			PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving((EntityLiving) ((CraftEntity) armor).getHandle());
			
			players.forEach(pl -> {
				((CraftPlayer) pl).getHandle().playerConnection.sendPacket(packet);
			});
			
			indicators.put(player, armor);
		}
	}
	
	public void clearIndicators()
	{
		for(ArmorStand armor : indicators.values())
		{
			PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(((CraftEntity) armor).getEntityId());
			players.forEach(player -> {
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
			});
		}
		
		indicators.clear();
	}
	
	public boolean isIPDL(Player p)
	{
		if(ipdl == null) return false;
		return p.getName().equalsIgnoreCase(ipdl.getName());
	}
	
	public boolean isWhiteWerewolf(Player p)
	{
		if(whiteWerewolf == null) return false;
		return p.getName().equalsIgnoreCase(whiteWerewolf.getName());
	}
	
	public boolean isAnonyme(Player p)
	{
		if(anonyme == null) return false;
		return p.getName().equalsIgnoreCase(anonyme.getName());
	}
	
}
