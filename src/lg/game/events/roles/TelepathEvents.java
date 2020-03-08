package lg.game.events.roles;

import lg.LGPlugin;
import lg.game.State;
import lg.roles.Role;
import lg.roles.Roles;
import lg.roles.roles.Telepath;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class TelepathEvents implements Listener {

	@EventHandler
	public void onSelect(InventoryClickEvent event) {
		if (LGPlugin.getStateManager().isStateActivated(State.NIGHT)) {
			Player p = (Player) event.getWhoClicked();
			InventoryView view = event.getView();
			ItemStack it = event.getCurrentItem();
			if (it != null
					&& view.getTitle().equalsIgnoreCase("§eChoix Télépathe.")) {
				for (Role role : LGPlugin.getRoleManager().getRoles()) {
					if (it.isSimilar(role.item)) {
						Telepath telepath = (Telepath) LGPlugin
								.getRoleManager().getRole(Roles.TELEPATH);
						telepath.toTalk = role;
						telepath.selected = true;
						telepath.canUse = false;

						p.sendMessage(LGPlugin.PREFIX
								+ "§aEnvoie ton message dans le chat.");

						event.setCancelled(true);
						p.closeInventory();
						return;
					}
				}
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onChat(PlayerChatEvent event) {
		if (LGPlugin.getStateManager().isStateActivated(State.NIGHT)) {
			Player p = event.getPlayer();
			if (LGPlugin.getRoleManager().getPlayerRoleID(p).contains(Roles.TELEPATH)) {
				Telepath telepath = (Telepath) LGPlugin.getRoleManager()
						.getRole(Roles.TELEPATH);
				if (telepath.selected) {
					String message = event.getMessage().replace("&", "§");
					telepath.toTalk
							.getPlayers()
							.forEach(
									player -> {
										player.sendMessage(LGPlugin.PREFIX
												+ "§bLe télépathe vous a envoyé un message.");
										player.sendMessage("§7[§eTélépathe§7] §7"
												+ message);
									});
					p.sendMessage(LGPlugin.PREFIX + "§aMessage envoyé !");
					telepath.selected = false;
					event.setCancelled(true);
				}
			}
		}
	}

}
