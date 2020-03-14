package lg.countdowns;

import java.util.List;

import lg.LGPlugin;
import lg.game.LGGame;
import lg.roles.Role;
import lg.roles.Roles;
import lg.roles.roles.Werewolf;
import lg.utils.Utils;
import net.minecraft.server.v1_14_R1.PacketPlayOutTitle.EnumTitleAction;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class NightCountdown extends BukkitRunnable {

	private int roleTimeout = 30;
	private int timeout = 30;
	
	private boolean running = true;
	
	private List<Role> toPass;
	private int currentIndex = -1;
	
	public NightCountdown(List<Role> toPass, int roleTimeout)
	{
		Werewolf.wwNightCount++;
		this.toPass = toPass;
		this.roleTimeout = roleTimeout;
		this.timeout = roleTimeout;
		this.runTaskTimer(LGPlugin.instance, 0, 20);
	}
	
	public NightCountdown(List<Role> toPass)
	{
		Werewolf.wwNightCount++;
		this.toPass = toPass;
		this.runTaskTimer(LGPlugin.instance, 0, 20);
	}
	
	@Override
	public void run() {
		
		if(running)
		{
			if(timeout == roleTimeout)
			{
				currentIndex++;
				toPass.get(currentIndex).activate();
				
				toPass.get(currentIndex).getPlayers().forEach(player -> {
					player.removePotionEffect(PotionEffectType.BLINDNESS);
					Utils.showOtherPlayers(player);
				});
				
				Bukkit.getOnlinePlayers().forEach(player -> {
					player.playSound(player.getLocation(), toPass.get(currentIndex).getSound(), 1f, 1f);
					
					Utils.sendTitle(player, EnumTitleAction.TITLE, "{\"text\":\"" + toPass.get(currentIndex).getName() + "\",\"color\":\"red\",\"bold\":\"true\"}");
					Utils.sendTitle(player, EnumTitleAction.SUBTITLE, "{\"text\":\"Réveillez-vous.\",\"color\":\"gray\"}");
				});
			}
		
			toPass.get(currentIndex).getPlayers().forEach(player -> {
				Utils.sendTitle(player, EnumTitleAction.ACTIONBAR, "[{\"text\":\"Il vous reste \",\"color\":\"gold\"},{\"text\":\"" + timeout + "s\",\"color\":\"red\"}]");
			});
			
			Bukkit.getOnlinePlayers().forEach(player -> {
				if(!toPass.get(currentIndex).getPlayers().contains(player))
				{
					Utils.sendTitle(player, EnumTitleAction.ACTIONBAR, "[{\"text\":\"Tour de : \",\"color\":\"gold\"},{\"text\":\"" + toPass.get(currentIndex).getName() + "\",\"color\":\"red\",\"bold\":\"true\"}]");
				}
			});
			
			if (timeout <= 0 || !toPass.get(currentIndex).canUse)
			{
				toPass.get(currentIndex).getPlayers().forEach(player->{
					player.getInventory().clear();
					player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999999, 255, true, true), true);
					Utils.hideOtherPlayers(player);
				});
				
				if(LGPlugin.getRoleManager().getRoleID(toPass.get(currentIndex)) == Roles.WEREWOLF)
				{
					Werewolf werewolf = ((Werewolf) LGPlugin.getRoleManager().getRole(Roles.WEREWOLF));
					werewolf.canUse = false;
					Player voted = werewolf.getVoteResult();
					werewolf.clear();
					
					if(voted != null) LGGame.toKill.add(voted);
					
					//Activating IPDL Power
					if(!LGGame.toKill.isEmpty() && 
							werewolf.canIPDLUse && 
							!LGGame.deadPlayers.contains(werewolf.ipdl) &&
							werewolf.ipdl != null)
						werewolf.onIPDLActivation();
					
					//Activating WW Power
					if(!werewolf.isDead() && Werewolf.wwNightCount == 2)
					{
						if(!LGGame.deadPlayers.contains(werewolf.whiteWerewolf) &&
								werewolf.whiteWerewolf != null)
						{
							//Activates power
							werewolf.onLGBActivation();
							Werewolf.wwNightCount = 0;
						}
					}
				}
				
				if(toPass.size() == currentIndex+1)
				{
					running = false;
					new NightToDayTransition().runTaskTimer(LGPlugin.instance, 0, 20);
					return;
				}
				timeout = roleTimeout+1;
			}
			
			timeout--;
		}

	}

}
