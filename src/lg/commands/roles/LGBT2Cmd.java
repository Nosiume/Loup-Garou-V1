package lg.commands.roles;

import lg.LGPlugin;
import lg.game.LGGame;
import lg.roles.Roles;
import lg.roles.roles.LGBT;

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

public class LGBT2Cmd implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg,
			String[] args) {
	
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			if(!LGPlugin.getRoleManager().getPlayerRoleID(p).contains(Roles.LGBT))
			{
				p.sendMessage("§cVous devez être lgbt pour éxecuter cette commande !");
				return true;
			}
			
			LGBT lgbt = (LGBT) LGPlugin.getRoleManager().getRole(Roles.LGBT);
			if(!lgbt.canKill)
			{
				p.sendMessage("§cVous n'avez pas le droit d'utiliser cette compétence.");
				return true;
			}
			
			Inventory inv = Bukkit.createInventory(null, 9*3, "§bQui est l'harceleur ?");
			
			ItemStack glass = new ItemStack(Material.GLASS_PANE, 1);
			ItemMeta glassM = glass.getItemMeta();
			glassM.setDisplayName("§0...");
			glass.setItemMeta(glassM);
			
			inv.setItem(0, glass);
			inv.setItem(8, glass);
			inv.setItem(9, glass);
			inv.setItem(17, glass);
			inv.setItem(18, glass);
			inv.setItem(26, glass);
			
			for(Player player : lgbt.lastVoters)
			{
				if(!LGGame.deadPlayers.contains(player))
				{
					//We never know :p
					if(player.getName().equalsIgnoreCase(p.getName()))
						continue;
					
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
