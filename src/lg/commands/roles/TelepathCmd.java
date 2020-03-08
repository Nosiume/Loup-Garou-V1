package lg.commands.roles;

import lg.LGPlugin;
import lg.game.State;
import lg.roles.Role;
import lg.roles.Roles;
import lg.roles.roles.Telepath;

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

public class TelepathCmd implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg,
			String[] args) {
	
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			if(!LGPlugin.getRoleManager().getPlayerRoleID(p).contains(Roles.TELEPATH))
			{
				p.sendMessage("§cTu dois être télépathe pour utiliser cette commande.");
				return true;
			}
			
			if(!LGPlugin.getStateManager().isStateActivated(State.NIGHT))
			{
				p.sendMessage("§cCette commande n'est utilisable que la nuit.");
				return true;
			}
			
			if(!((Telepath)LGPlugin.getRoleManager().getRole(Roles.TELEPATH)).canUse)
			{
				p.sendMessage(LGPlugin.PREFIX + "§cTu as déjà utilisé ton pouvoir !");
				return true;
			}
			
			Inventory inv = Bukkit.createInventory(null, 9 * 3, "§eChoix Télépathe.");
		
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
				if(!role.getPlayers().isEmpty() && role.item != null && 
						LGPlugin.getRoleManager().getRoleID(role) != Roles.TELEPATH)
				{
					inv.addItem(role.item);
				}
			}
			
			p.openInventory(inv);
		}
		
		return true;
	}

}
