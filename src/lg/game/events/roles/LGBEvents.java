package lg.game.events.roles;

import lg.LGPlugin;
import lg.game.LGGame;
import lg.roles.Roles;
import lg.roles.roles.Werewolf;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class LGBEvents implements Listener {

	@EventHandler
	public void onSelect(InventoryClickEvent event)
	{
		InventoryView view = event.getView();
		Player p = (Player) event.getWhoClicked();
		ItemStack it = event.getCurrentItem();
		Werewolf werewolf = (Werewolf) LGPlugin.getRoleManager().getRole(Roles.WEREWOLF);
		if(it != null && view.getTitle().equalsIgnoreCase("§bChoix du Loup-Garou."))
		{
			Bukkit.getOnlinePlayers().forEach(player -> {
				if(it.getItemMeta().getDisplayName().equalsIgnoreCase("§e" + player.getName()))
				{
					LGGame.toKill.add(player);
					p.closeInventory();
					p.sendMessage(LGPlugin.PREFIX + "§c" + player.getName() + " §emourera au lever du jour.");
					werewolf.wwCanUse = false;
				}
			});
			event.setCancelled(true);
		}
	}
	
}
