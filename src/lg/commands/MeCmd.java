package lg.commands;

import java.util.List;

import lg.LGPlugin;
import lg.game.State;
import lg.roles.Role;
import lg.roles.Roles;
import lg.roles.roles.Werewolf;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MeCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg,
			String[] args) {
		
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			
			if(!LGPlugin.getStateManager().isStateActivated(State.DAY) &&
					LGPlugin.getStateManager().isStateActivated(State.NIGHT))
			{
				p.sendMessage("�cVous devez �tre en partie pour �xecuter cette commande.");
				return true;
			}
			
			List<Role> role = LGPlugin.getRoleManager().getPlayerRole(p);
			
			if(role.size() > 1)
			{
				Role beforeInf = LGPlugin.getRoleManager().getRoleBeforeInfect(role);
				p.sendMessage(LGPlugin.PREFIX + "�eVous �tes �c" + beforeInf.getName() + " - Infect�");
				p.sendMessage("�7 - " + beforeInf.getDesc());
				p.sendMessage("�cCependant, Vous �tes infect�s �7: Vous devez �liminer tous les innocents (ceux qui ne sont pas loup-garou). Chaque nuit, vous vous r�unirez afin de d�terminer qui sera tu�. Vous ne devez pas vous faire remarquer.");
			} else
			{
				if(LGPlugin.getRoleManager().getRoleID(role.get(0)) == Roles.WEREWOLF)
				{
					Werewolf werewolf = (Werewolf) LGPlugin.getRoleManager().getRole(Roles.WEREWOLF);
					if(werewolf.isIPDL(p))
					{
						p.sendMessage(LGPlugin.PREFIX + "�eVous �tes �cInfect p�re des loups�e.");
						p.sendMessage("�7 - Vous devez �liminer tous les innocents (ceux qui ne sont pas loup-garou). Une fois dans la partie, vous pouvez d�cider d'infecter un villageois tu� par les loup-garou, ce dernier passera dans le camp des loup-garou. L'infect� gardera les facult�s de son r�le");
						return true;
					}
					
					//TODO: Test for lgb
				}
				
				p.sendMessage(LGPlugin.PREFIX + "�eVous �tes �c" + role.get(0).getName());
				p.sendMessage("�7 - " + role.get(0).getDesc());
			}
			
		}
		
		return true;
	}

}
