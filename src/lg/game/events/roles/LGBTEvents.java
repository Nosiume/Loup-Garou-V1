package lg.game.events.roles;

import lg.LGPlugin;
import lg.commands.roles.LGBTCmd;
import lg.game.LGGame;
import lg.roles.Roles;
import lg.roles.roles.LGBT;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class LGBTEvents implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSelect(InventoryClickEvent event) {
		Player p = (Player) event.getWhoClicked();
		ItemStack it = event.getCurrentItem();
		InventoryView view = event.getView();
		if (it != null
				&& view.getTitle().equalsIgnoreCase(
						"§eAppeler SOS Homophobie ?")) {
			if (it.isSimilar(LGBTCmd.yes)) {
				Inventory inv = Bukkit.createInventory(null, 9 * 3,
						"§eC'est qui l'homophobe !");

				ItemStack glass = new ItemStack(Material.GLASS, 1);
				ItemMeta meta = glass.getItemMeta();
				meta.setDisplayName("§7...");
				glass.setItemMeta(meta);

				inv.setItem(0, glass);
				inv.setItem(8, glass);
				inv.setItem(9, glass);
				inv.setItem(17, glass);
				inv.setItem(18, glass);
				inv.setItem(26, glass);

				for (Player player : Bukkit.getOnlinePlayers()) {
					if (!LGGame.deadPlayers.contains(player)) {
						if (player.getName().equalsIgnoreCase(p.getName()))
							continue;

						ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
						SkullMeta sMeta = (SkullMeta) skull.getItemMeta();
						sMeta.setOwner(player.getName());
						sMeta.setDisplayName("§e" + player.getName());
						skull.setItemMeta(sMeta);
						inv.addItem(skull);
					}
				}

				p.closeInventory();
				p.openInventory(inv);
			} else if (it.isSimilar(LGBTCmd.no)) {
				p.closeInventory();
				LGPlugin.getRoleManager().getRole(Roles.LGBT).canUse = false;
			}
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onChoose(InventoryClickEvent event) {
		Player p = (Player) event.getWhoClicked();
		ItemStack it = event.getCurrentItem();
		InventoryView view = event.getView();
		if (it != null
				&& view.getTitle()
						.equalsIgnoreCase("§eC'est qui l'homophobe !")) {
			Bukkit.getOnlinePlayers().forEach(
					player -> {
						if (it.getItemMeta().getDisplayName()
								.equalsIgnoreCase("§e" + player.getName())) {
							
							LGBT lgbt = (LGBT) LGPlugin.getRoleManager().getRole(Roles.LGBT);
							lgbt.target = player;
							lgbt.canUse = false;
							
							p.sendMessage(LGPlugin.PREFIX + "§eLe joueur §c" + player.getName() + " §eaura 2 votes au prochain tour.");
							event.setCancelled(true);
							p.closeInventory();
							return;
						}
					});
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onKill(InventoryClickEvent event)
	{
		Player p = (Player) event.getWhoClicked();
		ItemStack it = event.getCurrentItem();
		InventoryView view = event.getView();
		if (it != null
				&& view.getTitle()
						.equalsIgnoreCase("§bQui est l'harceleur ?")) {
			Bukkit.getOnlinePlayers().forEach(
					player -> {
						if (it.getItemMeta().getDisplayName()
								.equalsIgnoreCase("§e" + player.getName())) {

							LGGame.toKill.add(player);
							p.sendMessage(LGPlugin.PREFIX + "§eLe joueur §c" + player.getName() + " §esera retrouvé mort au lever du jour.");
							((LGBT) LGPlugin.getRoleManager().getRole(Roles.LGBT)).canKill = false;
							event.setCancelled(true);
							p.closeInventory();
							return;
						}
					});
			event.setCancelled(true);
		}
	}
}
