package lg.commands.roles;

import lg.LGPlugin;
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

public class LGBTCmd implements CommandExecutor {
	
	public static ItemStack yes;
	public static ItemStack no;
	
	public LGBTCmd()
	{
		yes = new ItemStack(Material.GREEN_WOOL, 1);
		ItemMeta yesM = yes.getItemMeta();
		yesM.setDisplayName("§aOui");
		yes.setItemMeta(yesM);
		
		no = new ItemStack(Material.RED_WOOL, 1);
		ItemMeta noM = no.getItemMeta();
		noM.setDisplayName("§cNon");
		no.setItemMeta(noM);
	}
	
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
			if(!lgbt.canUse)
			{
				p.sendMessage("§cVous n'avez pas le droit d'utiliser cette compétence.");
				return true;
			}
			
			Inventory inv = Bukkit.createInventory(null, 9*3, "§eAppeler SOS Homophobie ?");
			
			ItemStack glass = new ItemStack(Material.GLASS_PANE, 1);
			ItemMeta glassM = glass.getItemMeta();
			glassM.setDisplayName("§0...");
			glass.setItemMeta(glassM);
			
			for(int i = 0 ; i < inv.getSize() ; i++) inv.setItem(i, glass);
			inv.setItem(11, yes);
			inv.setItem(15, no);
			
			p.openInventory(inv);
		}
		
		return true;
	}

}
