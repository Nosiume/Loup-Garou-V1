package lg.game.events.roles;

import lg.LGPlugin;
import lg.roles.Roles;
import lg.roles.roles.WildChild;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class WildChildEvents implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent event)
	{
		InventoryView view = event.getView();
		Player p = (Player) event.getWhoClicked();
		ItemStack it = event.getCurrentItem();
		if(it != null && view.getTitle().equalsIgnoreCase("§eChoix du Modèle."))
		{
			WildChild wildChild = (WildChild) LGPlugin.getRoleManager().getRole(Roles.WILD_CHILD);
			Bukkit.getOnlinePlayers().forEach(player -> {
				if(it.getItemMeta().getDisplayName().equalsIgnoreCase("§e" + player.getName()))
				{
					wildChild.canUse = false;
					wildChild.master = player;
					p.sendMessage(LGPlugin.PREFIX + "§aVotre maître a été choisi. Il s'agit de §c" + player.getName() + "§e.");
					p.closeInventory();
					event.setCancelled(true);
					return;
				}
			});
			event.setCancelled(true);
		}
	}
	
}
