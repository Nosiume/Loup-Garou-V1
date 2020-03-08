package lg.commands;

import lg.LGPlugin;
import lg.roles.Roles;
import lg.roles.roles.LGBT;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ToggleGay implements CommandExecutor {

	private static boolean isEnabled = false;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg,
			String[] args) {
		
		isEnabled = !isEnabled;
		if(isEnabled)
			LGPlugin.getRoleManager().addRole(Roles.LGBT, new LGBT());
		else
			LGPlugin.getRoleManager().removeRole(Roles.LGBT);
		
		sender.sendMessage(LGPlugin.PREFIX + (isEnabled ? "§eLe rôle 'LGBT' a été activé." : "§eLe rôle 'LGBT' a été désactivé."));
		
		return true;
	}

}
