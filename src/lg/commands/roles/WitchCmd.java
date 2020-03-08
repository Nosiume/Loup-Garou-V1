package lg.commands.roles;

import lg.LGPlugin;
import lg.game.LGGame;
import lg.roles.Roles;
import lg.roles.roles.Witch;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WitchCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg,
			String[] args) {
		
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			if(!LGPlugin.getRoleManager().getPlayerRoleID(p).contains(Roles.WITCH))
			{
				p.sendMessage("§cVous n'êtes pas sorcière !");
				return true;
			}
			
			Witch witch = (Witch) LGPlugin.getRoleManager().getRole(Roles.WITCH);
			if(!witch.canUse)
			{
				p.sendMessage("§cVous ne pouvez pas utiliser votre pouvoir pour le moment.");
				return true;
			}
			
			if(!witch.canKill() && !witch.canSave())
			{
				p.sendMessage("§cVous avez déjà utilisé toutes vos potions.");
				return true;
			}
			
			//If witch can use it's abilities then
			Inventory inv = Bukkit.createInventory(null, 9*3, "§cStock de Potions.");
			
			ItemStack glass = new ItemStack(Material.GLASS, 1);
			ItemMeta meta = glass.getItemMeta();
			meta.setDisplayName("§0...");
			glass.setItemMeta(meta);
			
			
			for(int i = 0 ; i < inv.getSize() ; i++) inv.setItem(i, glass);
			
			//Checks if power is useless or not usable
			//If not then give it as a possibility to the user
			if(witch.canSave() && !LGGame.toKill.isEmpty()) 
				inv.setItem(11, Witch.savePot);
			
			//Checks if power is usable
			//If not then give it as a possibility to the user
			if(witch.canKill())
				inv.setItem(15, Witch.killPot);
			
			p.openInventory(inv);
		}
		
		return true;
	}

}
