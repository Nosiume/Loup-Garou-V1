package lg.game.states;

import lg.LGPlugin;
import lg.game.State;
import lg.roles.Roles;
import lg.roles.roles.Telepath;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

@SuppressWarnings("deprecation")
public class NightState implements Listener {

	@EventHandler
	public void onChat(PlayerChatEvent event)
	{
		if(LGPlugin.getStateManager().isStateActivated(State.NIGHT))
		{
			Player p = event.getPlayer();
			if(LGPlugin.getRoleManager().getPlayerRoleID(p).contains(Roles.TELEPATH))
			{
				Telepath telepath = (Telepath) LGPlugin.getRoleManager().getPlayerRole(p);
				if(!telepath.canUse && telepath.selected) return;
			}
			p.sendMessage("§cTu n'as pas le droit de parler la nuit.");
			event.setCancelled(true);
		}
	}
	
}
