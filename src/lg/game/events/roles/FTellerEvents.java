package lg.game.events.roles;

import java.util.List;

import lg.LGPlugin;
import lg.roles.Role;
import lg.roles.Roles;
import lg.roles.roles.FortuneTeller;
import lg.roles.roles.Werewolf;

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
					String roleName = role.get(0).getName();

					//Check for ipdl, ano & lgb
					if(role.size() == 1 && 
							LGPlugin.getRoleManager().getRoleID(role.get(0)) == Roles.WEREWOLF)
					{
						Werewolf werewolf = (Werewolf) role.get(0);
						if(werewolf.isIPDL(player))
							roleName = "Infect père des loups";
						else if (werewolf.isAnonyme(player))
							roleName = "Simple Villageois";
						else if (werewolf.isWhiteWerewolf(player))
							roleName = "Loup-Garou Blanc";
					}
					
					p.sendMessage(LGPlugin.PREFIX + "§aVous avez espionné §c" + player.getName());
					
					if(role.size() > 1)
					{
						roleName = LGPlugin.getRoleManager().getRoleBeforeInfect(role).getName();
						Bukkit.broadcastMessage(LGPlugin.PREFIX + "§eLa voyante a espionné un §cLoup-Garou - " + roleName);
					} 
					else
						Bukkit.broadcastMessage(LGPlugin.PREFIX + "§eLa voyante a espionné une personne qui est §c" + roleName);
					
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
