package lg.commands.config;

import lg.LGPlugin;
import lg.utils.Utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLobbyCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg,
			String[] args) {
		
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			Utils.setConfigLoc("setting.lobby", p.getLocation());
			p.sendMessage(LGPlugin.PREFIX + "§aLe lobby a été sauvegardé.");
			return true;
		}
		return false;
	}

}
