package lg.game.events.roles;

import lg.LGPlugin;
import lg.game.LGGame;
import lg.roles.Roles;
import lg.roles.roles.Witch;


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

public class WitchEvents implements Listener {

	@EventHandler
	public void onChooseSave(InventoryClickEvent event) {
		Player p = (Player) event.getWhoClicked();
		ItemStack it = event.getCurrentItem();
		InventoryView view = event.getView();

		if (it != null
				&& view.getTitle().equalsIgnoreCase("§cChoix du chanceux.")) {
			Bukkit.getOnlinePlayers().forEach(
					player -> {
						if (it.getItemMeta().getDisplayName()
								.equalsIgnoreCase("§e" + player.getName())) {
							Witch witch = (Witch) LGPlugin.getRoleManager().getRole(Roles.WITCH);
							witch.save(p, player);
							witch.canUse = false;

							event.setCancelled(true);
							p.closeInventory();
							return;
						}
					});

			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onChooseVictim(InventoryClickEvent event) {
		Player p = (Player) event.getWhoClicked();
		ItemStack it = event.getCurrentItem();
		InventoryView view = event.getView();

		if (it != null
				&& view.getTitle().equalsIgnoreCase("§cChoix de la victime.")) {
			Bukkit.getOnlinePlayers().forEach(
					player -> {
						if (it.getItemMeta().getDisplayName()
								.equalsIgnoreCase("§e" + player.getName())) {
							
							Witch witch = (Witch) LGPlugin.getRoleManager().getRole(Roles.WITCH);
							witch.kill(p, player);
							witch.canUse = false;
							
							event.setCancelled(true);
							p.closeInventory();
							return;
						}
					});

			event.setCancelled(true);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSelect(InventoryClickEvent event) {
		Player p = (Player) event.getWhoClicked();
		ItemStack it = event.getCurrentItem();
		InventoryView view = event.getView();
		if (it != null
				&& view.getTitle().equalsIgnoreCase("§cStock de Potions.")) {

			if (it.getItemMeta().getDisplayName().equalsIgnoreCase(Witch.killPot.getItemMeta().getDisplayName())) {
				Inventory inv = Bukkit.createInventory(null, 9 * 3,
						"§cChoix de la victime.");

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
			} else if (it.getItemMeta().getDisplayName().equalsIgnoreCase(Witch.savePot.getItemMeta().getDisplayName())) {
				Inventory inv = Bukkit.createInventory(null, 9 * 3,
						"§cChoix du chanceux.");

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
					if (LGGame.toKill.contains(player)) {
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
			}
			event.setCancelled(true);
		}
	}

}
