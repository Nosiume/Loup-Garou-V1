package lg.commands.config;

import lg.LGPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SetGameSize implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg,
			String[] args) {

		Player p = (Player) sender;
		if (args.length < 1 || args.length > 1)
			return false;

		int size;
		try {
			size = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			p.sendMessage("§cPlease enter a valid number.");
			return true;
		}

		FileConfiguration config = LGPlugin.instance.getConfig();
		config.set("setting.gamesize", size);
		LGPlugin.instance.saveConfig();

		p.sendMessage(LGPlugin.PREFIX
				+ "§aLe nombre de joueur a été défini à §e" + size);

		return true;
	}

}
