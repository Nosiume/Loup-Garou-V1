package lg.game.events.roles;

import lg.LGPlugin;
import lg.roles.Roles;
import lg.roles.roles.Detective;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class DetectiveEvents implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent event)
	{
		InventoryView view = event.getView();
		Player p = (Player) event.getWhoClicked();
		ItemStack it = event.getCurrentItem();
		if(it != null && view.getTitle().equalsIgnoreCase("§eChoix des Suspects."))
		{
			Bukkit.getOnlinePlayers().forEach(player -> {
				if(it.getItemMeta().getDisplayName().equalsIgnoreCase("§e" + player.getName()))
				{
					Detective detective = (Detective) LGPlugin.getRoleManager().getRole(Roles.DETECTIVE);
					detective.suspects.add(player);
					if(detective.suspects.size() < 2)
					{
						event.setCancelled(true);
						p.closeInventory();
						p.performCommand("lg-detective");
						return;
					} else
					{
						event.setCancelled(true);
						p.closeInventory();
						detective.onSelect(p);
						detective.canUse = false;
						return;
					}
				}
			});
			event.setCancelled(true);
		}
	}
	
}
