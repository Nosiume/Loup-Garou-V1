package lg.game.events.roles;

import lg.LGPlugin;
import lg.game.LGGame;
import lg.roles.Roles;
import lg.utils.Utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class HunterEvents implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent event)
	{
		Action action = event.getAction();
		Player p = event.getPlayer();
		
		if(action == Action.LEFT_CLICK_AIR && 
				LGPlugin.getRoleManager().getPlayerRoleID(p).contains(Roles.HUNTER) &&
				LGGame.deadPlayers.contains(p))
		{
			Entity entity = Utils.getNearestEntityInSight(p, 100);
			if(entity.getType() == EntityType.PLAYER)
			{
				Player player = (Player)entity;
				if(!player.getName().equalsIgnoreCase(p.getName()))
				{
					LGPlugin.getRoleManager().kill(player);
					LGPlugin.getRoleManager().getRole(Roles.HUNTER).canUse = false;
					return;
				}
			}
		}
	}
	
}
