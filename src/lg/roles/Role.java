package lg.roles;

import java.util.ArrayList;
import java.util.List;

import lg.LGPlugin;
import lg.game.LGGame;
import lg.roles.roles.Werewolf;
import lg.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public abstract class Role {

	//Role values
	public ItemStack item;
	public boolean canUse = false;
	
	protected String name;
	protected List<Player> players;
	private int teamSize;
	private Type type;
	private boolean activateDuringNight;
	private boolean activateAfterDeath;
	private boolean activateAtStart;
	private Sound sound;
	private PotionEffectType effect;
	private String desc;
	
	//Role constructor
	public Role(String name, 
			int teamSize,
			boolean activDuringNight, 
			boolean activAfterDeath, 
			boolean activAtStart,
			Type type,
			Sound sound,
			PotionEffectType effect,
			String desc)
	{
		this.name = name;
		this.players = new ArrayList<Player>();
		this.teamSize = teamSize;
		this.activateDuringNight = activDuringNight;
		this.activateAfterDeath = activAfterDeath;
		this.activateAtStart = activAtStart;
		this.type = type;
		this.sound = sound;
		this.effect = effect;
		this.desc = desc;
	}
	
	//Events
	public abstract void onActivation(Player p);
	public abstract void onStart(Player p);
	
	//Other funcs
	public void onDeath(Player p, boolean couple, boolean infected)
	{
		if(players.contains(p))
		{
			Utils.showOtherPlayers(p);
		
			Werewolf werewolf = (Werewolf) LGPlugin.getRoleManager().getRole(Roles.WEREWOLF);
			boolean ipdl = werewolf.isIPDL(p);
			
			broadcastDeath(p.getName(), couple, infected, ipdl);
			
			if(activateAfterDeath)
			{
				activate();
			}
		}
	}
	
	public void activate()
	{
		players.forEach(player->onActivation(player));
	}
	
	public void triggerOnStart()
	{
		players.forEach(player->{
			PotionEffectType effect = this.effect;
			if(LGPlugin.getRoleManager().getRoleID(this) == Roles.WEREWOLF)
			{
				Werewolf werewolf = (Werewolf) this;
				if(werewolf.isIPDL(player))
					effect = PotionEffectType.INCREASE_DAMAGE;
				//TODO: Check lgb
			}
			
			player.addPotionEffect(new PotionEffect(effect, 999999, 0, true, true), true);
			onStart(player);
		});
	}
	
	public void addPlayer(Player p)
	{
		if(!players.contains(p))
			players.add(p);
	}
	
	public void removePlayer(Player p)
	{
		if(players.contains(p))
			players.remove(p);
	}
	
	public List<Player> getPlayers()
	{
		return players;
	}
	
	private void broadcastDeath(String name, boolean couple, boolean infected, boolean ipdl)
	{
		String role = ipdl ? "Infect père des loups" : this.name;
		
		Bukkit.broadcastMessage("------------------------------");
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("    §c" + name + (couple ? "§7(§5♥§7)" : "§7") + " est mort(e).");
		Bukkit.broadcastMessage("    §7Il/Elle était §c" + (infected ? "Infecté - " : "") + role);
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("-------------------------------");
	}
	
	public boolean isPlayerRole(Player p)
	{
		return players.contains(p);
	}
	
	public boolean isActivatedDuringNight()
	{
		return this.activateDuringNight;
	}
	
	public boolean isActivatedAtStart()
	{
		return this.activateAtStart;
	}
	
	public int getTeamSize()
	{
		return teamSize;
	}

	public String getName() {
		return name;
	}
	
	public Type getType()
	{
		return type;
	}
	
	public Sound getSound()
	{
		return sound;
	}
	
	public PotionEffectType getEffect()
	{
		return effect;
	}
	
	public String getDesc()
	{
		return desc;
	}
	
	public boolean isDead()
	{
		boolean dead = true;
		for(Player p : players)
		{
			if(!LGGame.deadPlayers.contains(p))
			{
				dead = false;
				break;
			}
		}
		return dead;
	}
	
}
