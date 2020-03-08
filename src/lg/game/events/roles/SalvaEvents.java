package lg.game.events.roles;

import lg.LGPlugin;
import lg.roles.Roles;
import lg.roles.roles.Salvator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class SalvaEvents implements Listener {

	@EventHandler
	public void onSelect(InventoryClickEvent event)
	{
		Player p = (Player)event.getWhoClicked();
		ItemStack it = event.getCurrentItem();
		InventoryView view = event.getView();
		
		if(it != null && view.getTitle().equalsIgnoreCase("§bProtéger quelqu'un."))
		{
			Bukkit.getOnlinePlayers().forEach(player -> {
				if(it.getItemMeta().getDisplayName().equalsIgnoreCase("§e" + player.getName()))
				{
					Salvator salva = (Salvator) LGPlugin.getRoleManager().getRole(Roles.SALVATOR);
					
					salva.protectedPlayer = player;
					
					if(!salva.alreadyProtected.containsKey(salva))
						salva.alreadyProtected.put(player, 1);
					else
					{
						int count = salva.alreadyProtected.get(player) + 1;
						salva.alreadyProtected.replace(player, count);
					}
					
					salva.canUse = false;
					p.sendMessage(LGPlugin.PREFIX + "§aLe joueur " + player.getName() + " a été protégé.");
					
					event.setCancelled(true);
					p.closeInventory();
				}
			});
			event.setCancelled(true);
		}
	}
	
}
