package lg.commands.roles;

import lg.LGPlugin;
import lg.game.LGGame;
import lg.roles.Roles;
import lg.roles.roles.Cupidon;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class CupidonCmd implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg,
			String[] args) {
		
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			if(!LGPlugin.getRoleManager().getPlayerRoleID(p).contains(Roles.CUPIDON))
			{
				p.sendMessage("§cVous devez être cupidon pour utiliser cette commande.");
				return true;
			}
			
			Cupidon cupidon = (Cupidon) LGPlugin.getRoleManager().getRole(Roles.CUPIDON);
			if(!cupidon.canUse)
			{
				p.sendMessage(LGPlugin.PREFIX + "§cTu as déjà utilisé ta capacité !");
				return true;
			}
			
			Inventory inv = Bukkit.createInventory(null, 9*3, "§eChoix du Couple.");
			
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
			
			for(Player player : Bukkit.getOnlinePlayers())
			{
				if(!LGGame.deadPlayers.contains(player) && !cupidon.couple.contains(player))
				{
					ItemStack it = new ItemStack(Material.PLAYER_HEAD, 1);
					SkullMeta sMeta = (SkullMeta) it.getItemMeta();
					sMeta.setOwner(player.getName());
					sMeta.setDisplayName("§e" + player.getName());
					it.setItemMeta(sMeta);
					inv.addItem(it);
				}
			}
			
			p.openInventory(inv);
		}
		
		return true;
	}

}
