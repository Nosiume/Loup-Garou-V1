package lg.commands.config;

import lg.LGPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class SetStartingCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg,
			String[] args) {
		
		if(args.length < 1 || args.length > 1)
			return false;
		
		FileConfiguration config = LGPlugin.instance.getConfig();
		switch(args[0].toLowerCase())
		{
			case "command":
				config.set("setting.starting", args[0].toLowerCase());
				LGPlugin.instance.saveConfig();
				break;
			case "auto":
				config.set("setting.starting", args[0].toLowerCase());
				LGPlugin.instance.saveConfig();
				break;
			default:
				sender.sendMessage("§c" + args[0] + " is not a valid starting process.");
				return false;
		}
		
		sender.sendMessage(LGPlugin.PREFIX + "§aLa méthode de lancement a été modifiée.");
		return true;
	}

}
