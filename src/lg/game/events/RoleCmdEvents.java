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
				&& view.getTitle().equalsIgnoreCase("�bListe des R�les.")) {
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
						"Infect p�re des loups",
						"Vous devez �liminer tous les innocents (ceux qui ne sont pas loup-garou). Une fois dans la partie, vous pouvez d�cider d'infecter un villageois tu� par les loup-garou, ce dernier passera dans le camp des loup-garou. L'infect� gardera les facult�s de son r�le.");
				p.closeInventory();
			}
			
			event.setCancelled(true);
		}
	}
	
	private void printRole(Player p, String name, String desc)
	{
		p.sendMessage("�7�l================================");
		p.sendMessage("");
		p.sendMessage("        �eR�le : �c" + name);
		p.sendMessage("�eDescription : �7" + desc);
		p.sendMessage("");
		p.sendMessage("�7�l================================");
	}

}
