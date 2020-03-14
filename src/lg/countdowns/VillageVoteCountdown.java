package lg.countdowns;

import lg.LGPlugin;
import lg.game.LGGame;
import lg.game.Vote;
import lg.roles.Roles;
import lg.roles.roles.LGBT;
import lg.utils.Utils;
import net.minecraft.server.v1_14_R1.PacketPlayOutTitle.EnumTitleAction;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class VillageVoteCountdown extends BukkitRunnable {

	private int timer = 10; //200;
	private boolean running = true;
	
	public VillageVoteCountdown() {}
	
	public VillageVoteCountdown(int timer)
	{
		this.timer = timer;
	}
	
	@Override
	public void run() {
		if(running)
		{
			if(timer == 0)
			{
				Player p = LGGame.getVoteResult();
				if(p != null) LGPlugin.getRoleManager().kill(p);
				
				Bukkit.getOnlinePlayers().forEach(player -> {
					if(!LGGame.toKill.contains(player))
					{
						boolean condition = p != null;
						Utils.sendTitle(player, EnumTitleAction.TITLE, "{\"text\":\"" + (condition ? p.getName() : "Personne") + "\",\"color\":\"red\"}");
						Utils.sendTitle(player, EnumTitleAction.SUBTITLE, "{\"text\":\"" + (condition ? "a été exécuté." : "n'a été exécuté") + "\",\"color\":\"gray\"}");
						player.getInventory().clear();
					}
				});
				
				for(Vote v : LGGame.votes)
				{
					if(LGPlugin.getRoleManager().getPlayerRoleID(v.getVoted()).contains(Roles.LGBT))
					{
						LGBT lgbt = (LGBT) LGPlugin.getRoleManager().getRole(Roles.LGBT);
						lgbt.canKill = true;
						lgbt.lastVoters = v.getVoters();
					}
				}
				
				LGGame.clearVotes();
				new DayToNightTransition().runTaskTimer(LGPlugin.instance, 0, 20);
				
				running = false;
				return;
			}
			
			Bukkit.getOnlinePlayers().forEach(player -> {
				Utils.sendTitle(player, 
						EnumTitleAction.ACTIONBAR,
						"[{\"text\":\"Il vous reste \",\"color\":\"yellow\"},{\"text\":\"" + timer + " \",\"color\":\"red\",\"bold\":\"true\"},{\"text\":\"secondes.\",\"color\":\"yellow\",\"bold\":\"false\"}]");
			});
			
			timer--;
		}
	}

}
