package lg.game.states;

import lg.LGPlugin;
import lg.countdowns.StartCountdown;
import lg.game.State;
import lg.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LobbyState implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		if(!LGPlugin.getStateManager().isStateActivated(State.LOBBY))
			return;
		
		int size = LGPlugin.instance.getConfig().getInt("setting.gamesize");
		Player p = event.getPlayer();
		if(Bukkit.getOnlinePlayers().size() > size)
		{
			p.kickPlayer("§cLa game est déjà complète.");
			event.setJoinMessage("");
			return;
		}
		
		if(Bukkit.getOnlinePlayers().size() == size)
		{
			Bukkit.getOnlinePlayers().forEach(player -> {
				player.playSound(player.getLocation(), Sound.ENTITY_WOLF_GROWL, 1f, 1f);
			});
			Bukkit.broadcastMessage(LGPlugin.PREFIX + "§eLa partie peut être lancée");
			String startingProcessName = LGPlugin.instance.getConfig().getString("setting.starting");
			if(startingProcessName.equalsIgnoreCase("auto"))
			{
				new StartCountdown(10).runTaskTimer(LGPlugin.instance, 0, 20);
			} else
			{
				Bukkit.getOnlinePlayers().forEach(player -> {
					if(player.isOp())
					{
						player.sendMessage(LGPlugin.PREFIX + "§eVous pouvez lancer la game avec la commande /lg-start");
					}
				});
			}
		}
		
		p.teleport(Utils.getConfigLoc("setting.lobby"));
		event.setJoinMessage(LGPlugin.PREFIX + "§4" + p.getName() + " §ca rejoint la partie §7(" + Bukkit.getOnlinePlayers().size() + "/" + size + ").");
		Bukkit.getOnlinePlayers().forEach(player -> {
			player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
		});
		
		p.setWalkSpeed(0.2f);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event)
	{
		Player p = event.getPlayer();
		int size = LGPlugin.instance.getConfig().getInt("setting.gamesize");
		event.setQuitMessage(LGPlugin.PREFIX + "§4" + p.getName() + " §ca quitté la partie §7(" + (Bukkit.getOnlinePlayers().size()-1) + "/" + size + ").");
	}
	
}
