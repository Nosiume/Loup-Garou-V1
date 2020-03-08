package lg.commands.config;

import lg.LGPlugin;
import lg.utils.Utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class AddSpawnCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg,
			String[] args) {
		
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			
			FileConfiguration config = LGPlugin.instance.getConfig();
			int spawns = config.getInt("setting.savedSpawns") + 1;
			config.set("setting.savedSpawns", spawns);
			
			Utils.setConfigLoc("setting.spawn" + spawns, p.getLocation());
			p.sendMessage(LGPlugin.PREFIX + "§aLe spawn #" + spawns + " a été sauvegarder");
		}
		
		return true;
	}

}
