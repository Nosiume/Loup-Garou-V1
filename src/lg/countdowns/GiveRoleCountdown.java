package lg.countdowns;

import lg.LGPlugin;
import lg.game.LGGame;
import lg.roles.Role;
import lg.roles.Roles;
import lg.roles.Type;
import lg.roles.roles.Werewolf;
import lg.utils.Utils;
import net.minecraft.server.v1_14_R1.PacketPlayOutTitle.EnumTitleAction;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;


public class GiveRoleCountdown extends BukkitRunnable {

	private boolean running = true;
	private int timer = 5;
	
	public GiveRoleCountdown()
	{
		Bukkit.getOnlinePlayers().forEach(player -> {
			//We don't need to test for multi-role because at start, no one will have multiple roles.
			Role role = LGPlugin.getRoleManager().getPlayerRole(player).get(0);
			String winWith = role.getType() == Type.WEREWOLF ? "les Loups-Garous" : role.getType() == Type.VILLAGER ? "les Villageois" : "Seul";
			
			String name = role.getName();
			if(LGPlugin.getRoleManager().getRoleID(role) == Roles.WEREWOLF)
			{
				Werewolf werewolf = (Werewolf) role;
				if(werewolf.isIPDL(player))
					name = "Infect père des loups";
				else if(werewolf.isAnonyme(player))
					name = "Loup Anonyme";
				else if(werewolf.isWhiteWerewolf(player))
					name = "Loup-Garou Blanc";
			}
			
			Utils.sendTitle(player, EnumTitleAction.TITLE, "[{\"text\":\"Vous êtes \",\"color\":\"gray\"},{\"text\":\"" + name + "\",\"color\":\"red\",\"bold\":\"true\"}]");
			Utils.sendTitle(player, EnumTitleAction.SUBTITLE, "[{\"text\":\"Vous gagnez avec \",\"color\":\"yellow\"},{\"text\":\"" + winWith + "\",\"color\":\"gold\",\"bold\":\"true\"}]");
		});
	}
	
	@Override
	public void run() {
		if(running)
		{
			if(timer == 0)
			{
				LGGame.night();
				running = false;
			}
			
			timer--;
		}
	}

}
