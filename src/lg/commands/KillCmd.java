package lg.commands;

import lg.LGPlugin;
import lg.game.LGGame;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KillCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String msg, String[] args) {
		
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			
			if(args.length > 0)
			{
				try {
					Player target = Bukkit.getPlayer(args[0]);
					if(args[1] != null && args[1] == "wait")
						LGGame.toKill.add(target);
					else
						LGPlugin.getRoleManager().kill(target);
					p.sendMessage(LGPlugin.PREFIX + "§aVous avez tué §7" + args[0] + "§a.");
				} catch (Exception e)
				{
					p.sendMessage("§cLe joueur n'éxiste pas ou n'est pas connecté.");
				}
				return true;
			}
			
			LGPlugin.getRoleManager().kill(p);
			p.sendMessage(LGPlugin.PREFIX + "§aVous vous êtes suicidé.");
		}
		
		return true;
	}
	
}
