package lg.roles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import lg.LGPlugin;
import lg.game.LGGame;
import lg.roles.roles.Cupidon;
import lg.roles.roles.Detective;
import lg.roles.roles.Elder;
import lg.roles.roles.FortuneTeller;
import lg.roles.roles.Werewolf;
import lg.roles.roles.WildChild;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class RoleManager {

	private static HashMap<Roles, Role> roles = new HashMap<Roles, Role>();
	
	public RoleManager()
	{
		//TODO: Create all roles class
		roles.put(Roles.WEREWOLF, new Werewolf());
		roles.put(Roles.FORTUNE_TELLER, new FortuneTeller());
		roles.put(Roles.DETECTIVE, new Detective());
	}
	
	public void addRole(Roles roleTAG, Role role)
	{
		roles.put(roleTAG, role);
	}
	
	public void removeRole(Roles role)
	{
		roles.remove(role);
	}
	
	public Role getRole(Roles id)
	{
		return roles.get(id);
	}
	
	public List<Role> getPlayerRole(Player p)
	{
		List<Role> pRoles = new ArrayList<Role>();
		for(Role role : roles.values())
		{
			if(role.isPlayerRole(p))
				pRoles.add(role);
		}
		return pRoles;
	}
	
	public List<Roles> getPlayerRoleID(Player p)
	{
		List<Roles> pRoles = new ArrayList<Roles>();
		for(Roles role : roles.keySet())
		{
			if(roles.get(role).isPlayerRole(p))
				pRoles.add(role);
		}
		return pRoles;
	}
	
	public Roles getRoleID(Role role)
	{
		for(Entry<Roles, Role> r : roles.entrySet())
		{
			if(r.getValue().name.equalsIgnoreCase(role.name))
			{
				return r.getKey();
			}
		}
		return null;
	}
	
	public Collection<Role> getRoles()
	{
		return roles.values();
	}
	
	public void giveRoles()
	{
		Random r = new Random();
		List<Player> online = new ArrayList<Player>(Bukkit.getOnlinePlayers());
		List<Integer> blackList = new ArrayList<Integer>();
		for(Role role : roles.values())
		{
			for(int i = 0 ; i < role.getTeamSize() ; i++)
			{
				int index;
				do
				{
					index = r.nextInt(online.size());
				} while(blackList.contains(index));
				role.addPlayer(online.get(index));
				
				//TODO: Uncomment when debug done
				if(LGPlugin.getRoleManager().getRoleID(role) == Roles.WEREWOLF
						/*&& role.getTeamSize() >= 4*/)
				{
					Werewolf werewolf = (Werewolf) role;
					if(i == /*2*/ 0)
						werewolf.ipdl = online.get(index);
					else if (i == /*3*/ 1)
						werewolf.whiteWerewolf = online.get(index);
				}
				
				blackList.add(index);
				if(blackList.size() == online.size())
				{
					role.triggerOnStart();
					if(role.isActivatedAtStart())
					{
						role.activate();
					}
					return;
				}
			}
			
			if(role.isActivatedAtStart())
			{
				role.activate();
			}
			role.triggerOnStart();
		}
	}

	@SuppressWarnings("deprecation")
	public void kill(Player player) {
		Role[] role = new Role[getPlayerRole(player).size()];
		getPlayerRole(player).toArray(role);
		
		Cupidon cupidon = (Cupidon) LGPlugin.getRoleManager().getRole(Roles.CUPIDON);
		WildChild wChild = (WildChild) LGPlugin.getRoleManager().getRole(Roles.WILD_CHILD);
		
		boolean couple = false;
		
		for(Role r : role)
		{
			switch(getRoleID(r))
			{
			case ELDER:
				Elder elder = (Elder) r;
				if(elder.canUse)
				{
					elder.activate();
					elder.canUse = false;
					return;
				}
				break;
				
			case WEREWOLF:
				Werewolf werewolf = (Werewolf) r;
				werewolf.onWerewolfDeath(player);
				break;
			default:
				break;
			}
			
			if(cupidon != null) if(cupidon.couple.contains(player)) couple = true;
			if(wChild != null) if(wChild.master.getName().equalsIgnoreCase(player.getName())) wChild.onMasterDeath();
			
			player.getLocation().getWorld().strikeLightning(player.getLocation());
			Bukkit.getOnlinePlayers().forEach(p -> {
				if(p.getName() != player.getName()) p.hidePlayer(player);
			});
			
			if(couple)
			{
				Player other = cupidon.couple.indexOf(player) == 0 ? cupidon.couple.get(1) : cupidon.couple.get(0);
				if(!LGGame.deadPlayers.contains(other)) 
				{
					kill(other);
					Bukkit.broadcastMessage(LGPlugin.PREFIX + "§aDans un élan de chagrin, §7" + other.getName() + " §as'est suicidé.");
				}
			}
			
			player.removePotionEffect(r.getEffect());
		}
		
		//Print out death message
		if(role.length > 1)
		{
			Role beforeInf = getRoleBeforeInfect(Arrays.asList(role));
			beforeInf.onDeath(player, couple, true);
		} else
		{
			role[0].onDeath(player, couple, false);
		}
		LGGame.deadPlayers.add(player);
		
		//Gives inventory for dead players
		PlayerInventory inv = player.getInventory();
		
		ItemStack it = new ItemStack(Material.SKELETON_SKULL, 1);
		ItemMeta meta = it.getItemMeta();
		meta.setDisplayName("§7Vous êtes mort.");
		it.setItemMeta(meta);
		
		ItemStack hud = new ItemStack(Material.CARVED_PUMPKIN, 1);
		ItemMeta hudM = hud.getItemMeta();
		hudM.setDisplayName("§7Vous êtes mort.");
		hud.setItemMeta(hudM);
		
		for(int i = 0 ; i < 9 ; i++) inv.setItem(i, it);
		inv.setHelmet(hud);
	}
	
	public Role getRoleBeforeInfect(List<Role> role)
	{
		Role beforeInf = null;
		for(Role r : role)
		{
			if(LGPlugin.getRoleManager().getRoleID(r) != Roles.WEREWOLF)
			{
				beforeInf = r;
				break;
			}
		}
		return beforeInf;
	}
	
}
