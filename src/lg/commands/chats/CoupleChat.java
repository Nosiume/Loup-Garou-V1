package lg.commands.chats;

import lg.LGPlugin;
import lg.roles.Roles;
import lg.roles.roles.Cupidon;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoupleChat implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg,
			String[] args) {
		
		if(sender instanceof Player)
		{
			Cupidon cupidon = (Cupidon) LGPlugin.getRoleManager().getRole(Roles.CUPIDON);
			Player p = (Player)sender;
			if(!cupidon.couple.contains(p))
			{
				p.sendMessage("§cIl faut être dans le couple pour éxécuter cette commande.");
				return true;
			}
			
			if(args.length == 0) return false;
			
			StringBuilder sb = new StringBuilder();
			for(String arg : args) sb.append(arg + " ");
			String message = sb.toString().replace("&", "§");
			
			cupidon.couple.forEach(player -> {
				player.sendMessage("§7[§5§lChat-Couple§r§7] §e" + p.getName() + "§r> " + message);
			});
		}
		
		return true;
	}

}
