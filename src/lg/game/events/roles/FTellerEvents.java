package lg.game.events.roles;

import java.util.List;

import lg.LGPlugin;
import lg.roles.Role;
import lg.roles.Roles;
import lg.roles.roles.FortuneTeller;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class FTellerEvents implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent event)
	{
		InventoryView view = event.getView();
		Player p = (Player) event.getWhoClicked();
		ItemStack it = event.getCurrentItem();
		if(it != null && view.getTitle().equalsIgnoreCase("§eChoix de la personne à espionner."))
		{
			Bukkit.getOnlinePlayers().forEach(player -> {
				if(it.getItemMeta().getDisplayName().equalsIgnoreCase("§e" + player.getName()))
				{
					FortuneTeller fTeller = (FortuneTeller) LGPlugin.getRoleManager().getRole(Roles.FORTUNE_TELLER);
					List<Role> role = LGPlugin.getRoleManager().getPlayerRole(player);
					
					//TODO: verif si ipdl ou lgb
					
					p.sendMessage(LGPlugin.PREFIX + "§aVous avez espionné §c" + player.getName());
					
					if(role.size() > 1)
					{
						Role beforeInf = LGPlugin.getRoleManager().getRoleBeforeInfect(role);
						Bukkit.broadcastMessage(LGPlugin.PREFIX + "§eLa voyante a espionné un §cLoup-Garou - " + beforeInf.getName());
					} 
					else
						Bukkit.broadcastMessage(LGPlugin.PREFIX + "§eLa voyante a espionné une personne qui est §c" + role.get(0).getName());
					
					p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
					
					fTeller.canUse = false;
					event.setCancelled(true);
					p.closeInventory();
				}
			});
			event.setCancelled(true);
		}
	}
	
}
