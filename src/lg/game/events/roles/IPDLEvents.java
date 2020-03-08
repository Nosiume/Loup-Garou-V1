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

public class IPDLEvents implements Listener {

	@EventHandler
	public void onInfect(InventoryClickEvent event)
	{
		Player p = (Player)event.getWhoClicked();
		ItemStack it = event.getCurrentItem();
		InventoryView view = event.getView();
		if(it != null && view.getTitle().equalsIgnoreCase("§cChoix de l'infecté."))
		{
			Bukkit.getOnlinePlayers().forEach(target -> {
				if(it.getItemMeta().getDisplayName().equalsIgnoreCase("§e" + target.getName()))
				{
					Werewolf werewolf = (Werewolf) LGPlugin.getRoleManager().getRole(Roles.WEREWOLF);

					LGGame.toKill.remove(target);
					werewolf.addPlayer(target); // Makes player infected
					target.sendMessage(LGPlugin.PREFIX + "§eLes loups-garous avaient choisi de vous tuer. Cependant, vous avez été infecté et devenez l'un d'entre eux.");
					p.sendMessage(LGPlugin.PREFIX + "§eLe joueur §c" + target.getName() + " §ea été infecté.");
					
					werewolf.clearIndicators(); //RESET INDICATORS
					werewolf.spawnIndicators(); //Re-Spawn indicators
					
					werewolf.canIPDLUse = false;
					p.closeInventory();
				}
			});
			event.setCancelled(true);
		}
	}
	
}
