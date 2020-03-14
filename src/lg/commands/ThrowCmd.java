package lg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ThrowCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String msg, String[] args) {
		
		if(args.length < 1) return false;
		
		try {
			Player target = Bukkit.getPlayer(args[0]);
			if(args.length < 4)
			{
				target.setVelocity(target.getLocation().getDirection().multiply(10).add(new Vector(0, 5, 0)));
			} else
			{
				try
				{
					double velX = Double.parseDouble(args[1]);
					double velY = Double.parseDouble(args[2]);
					double velZ = Double.parseDouble(args[3]);
					
					target.setVelocity(new Vector(velX, velY, velZ));
				} catch (NumberFormatException e)
				{
					sender.sendMessage("§cVelocity incorrecte.");
				}
			}
			
			sender.sendMessage("§aLe joueur a été claqué très fort.");
		} catch(Exception e)
		{
			sender.sendMessage("§cDéso le joueur à pas été trouvé.");
		}
		
		return true;
	}
	

}
