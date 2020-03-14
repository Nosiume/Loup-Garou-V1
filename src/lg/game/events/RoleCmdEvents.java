package lg.game.events;

import lg.LGPlugin;
import lg.commands.RoleCmd;
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
			} else if (it.isSimilar(Werewolf.anoItem))
			{
				printRole(p,
						"Loup Anonyme",
						"Vous faites parti du camp des loups-garou, si vous vous faite espionner par la voyante, elle verra que vous êtes simple villageois.");
				p.closeInventory();
			} else if (it.isSimilar(Werewolf.wwItem))
			{
				printRole(p,
						"Loup-Garou Blanc",
						"Vous devez gagner SEUL. Toutes les deux nuits, vous pouvez éliminer un loup garou. Les autres compères loup garou pensent que vous êtes aussi Loup Garou simple.");
				p.closeInventory();
			} else if (it.isSimilar(RoleCmd.villager)){
				printRole(p,
						"Villageois",
						"Vous ne pouvez que voter lors du vote du village. Sinon tu sers à rien.");
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
