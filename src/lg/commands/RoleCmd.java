package lg.commands;

import lg.LGPlugin;
import lg.roles.Role;
import lg.roles.roles.Werewolf;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RoleCmd implements CommandExecutor {

	public static ItemStack villager;
	
	public RoleCmd()
	{
		villager = new ItemStack(Material.BOWL, 1);
		ItemMeta meta = villager.getItemMeta();
		meta.setDisplayName("§eVillageois");
		villager.setItemMeta(meta);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg,
			String[] args) {
		
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			
			Inventory inv = Bukkit.createInventory(null, 9*3, "§bListe des Rôles.");
			
			ItemStack glass = new ItemStack(Material.GLASS, 1);
			ItemMeta meta = glass.getItemMeta();
			meta.setDisplayName("§7...");
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			glass.setItemMeta(meta);
			
			inv.setItem(0, glass);
			inv.setItem(8, glass);
			inv.setItem(9, glass);
			inv.setItem(17, glass);
			inv.setItem(18, glass);
			inv.setItem(26, glass);
			
			for(Role role : LGPlugin.getRoleManager().getRoles())
			{
				if(role.item != null)
				{
					inv.addItem(role.item);
				}
			}
			
			inv.addItem(Werewolf.ipdlItem);
			inv.addItem(Werewolf.anoItem);
			inv.addItem(Werewolf.wwItem);
			inv.addItem(villager);
			
			p.openInventory(inv);
		}
		
		return true;
	}

}
