package lg.game.events.roles;

import lg.LGPlugin;
import lg.roles.Roles;
import lg.roles.roles.Cupidon;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class CupidonEvents implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent event)
	{
		InventoryView view = event.getView();
		Player p = (Player) event.getWhoClicked();
		ItemStack it = event.getCurrentItem();
		if(it != null && view.getTitle().equalsIgnoreCase("§eChoix du Couple."))
		{
			Bukkit.getOnlinePlayers().forEach(player -> {
				if(it.getItemMeta().getDisplayName().equalsIgnoreCase("§e" + player.getName()))
				{
					Cupidon cupidon = (Cupidon) LGPlugin.getRoleManager().getRole(Roles.CUPIDON);
					cupidon.couple.add(player);
					if(cupidon.couple.size() >= 2)
					{
						cupidon.canUse = false;
						event.setCancelled(true);
						p.closeInventory();
						p.sendMessage(LGPlugin.PREFIX + "§aLe couple a été choisi");
						
						cupidon.onChoose();
						return;
					} else 
					{
						event.setCancelled(true);
						p.closeInventory();
						p.performCommand("lg-cupidon");
						return;
					}
				}
			});
			event.setCancelled(true);
		}
	}
	
}
