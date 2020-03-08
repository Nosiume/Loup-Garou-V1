package lg.commands;

import lg.LGPlugin;
import lg.game.State;
import lg.roles.Roles;
import lg.roles.roles.Werewolf;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LgCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg,
			String[] args) {
		
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			if(!LGPlugin.getStateManager().isStateActivated(State.DAY) &&
					!LGPlugin.getStateManager().isStateActivated(State.NIGHT))
			{
				p.sendMessage("§cVous devez être en partie pour éxecuter cette commande.");
				return true;
			}
			
			if(!LGPlugin.getRoleManager().getPlayerRoleID(p).contains(Roles.WEREWOLF))
			{
				p.sendMessage("§cVous devez être Loup-Garou pour éxecuter cette commande.");
				return true;
			}
			
			Werewolf werewolf = (Werewolf) LGPlugin.getRoleManager().getRole(Roles.WEREWOLF);
			StringBuilder sb = new StringBuilder();
			werewolf.getPlayers().forEach(player -> sb.append("§7 - §c" + player.getName() + "\n"));
			String message = sb.toString().trim();
			
			p.sendMessage(LGPlugin.PREFIX + "§eLes loups-garous sont : ");
			p.sendMessage(message);
		}
		
		return true;
	}

}
