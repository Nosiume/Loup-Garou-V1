package lg.game.events;

import lg.LGPlugin;
import lg.roles.Role;
import lg.roles.roles.Werewolf;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class RoleCmdEvents implements Listener {

	@EventHandler
	public void onSelectRole(InventoryClickEvent event) {
		Player p = (Player) event.getWhoClicked();
		InventoryView view = event.getView();
		ItemStack it = event.getCurrentItem();
		if (it != null
				&& view.getTitle().equalsIgnoreCase("§bListe des Rôles.")) {
			for (Role role : LGPlugin.getRoleManager().getRoles()) {
				if (it.isSimilar(role.item)) {
					
					printRole(p, role.getName(), role.getDesc());
					p.closeInventory();
					break;
				}
			}
			
			if(it.isSimilar(Werewolf.ipdlItem))
			{
				printRole(p, 
						"Infect père des loups",
						"Vous devez éliminer tous les innocents (ceux qui ne sont pas loup-garou). Une fois dans la partie, vous pouvez décider d'infecter un villageois tué par les loup-garou, ce dernier passera dans le camp des loup-garou. L'infecté gardera les facultés de son rôle.");
				p.closeInventory();
			}
			
			event.setCancelled(true);
		}
	}
	
	private void printRole(Player p, String name, String desc)
	{
		p.sendMessage("§7§l================================");
		p.sendMessage("");
		p.sendMessage("        §eRôle : §c" + name);
		p.sendMessage("§eDescription : §7" + desc);
		p.sendMessage("");
		p.sendMessage("§7§l================================");
	}

}
