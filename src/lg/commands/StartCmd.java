package lg.commands;

import lg.LGPlugin;
import lg.countdowns.StartCountdown;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg,
			String[] args) {
		
		String process = LGPlugin.instance.getConfig().getString("setting.starting");
		if(process.equalsIgnoreCase("auto"))
		{
			sender.sendMessage("§cLa commande est désactivée.");
			return true;
		}
		
		new StartCountdown().runTaskTimer(LGPlugin.instance, 0, 20);
		sender.sendMessage(LGPlugin.PREFIX + "§aLancement de la partie...");
		
		return true;
	}

}
