package lg.countdowns;

import lg.game.LGGame;
import lg.utils.Utils;
import net.minecraft.server.v1_14_R1.PacketPlayOutTitle.EnumTitleAction;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class StartCountdown extends BukkitRunnable {

	private double steps = 1.6666667;
	private int timeout = 5;
	
	private boolean running = true;
	
	public StartCountdown() {}
	
	public StartCountdown(int timeout)
	{
		this.timeout = timeout;
		this.steps = timeout / 3;
	}
	
	@Override
	public void run() {
		if(running)
		{
			if(timeout > 0)
			{
				String color = timeout >= 2*steps ? "green" : timeout >= steps ? "gold" : "red";
				Bukkit.getOnlinePlayers().forEach(player -> {
					Utils.sendTitle(player, EnumTitleAction.TITLE, "{\"text\":\"" + timeout + "\",\"color\":\"" + color + "\"}");
					player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
				});
				timeout--;
			} else if (timeout == 0){
				LGGame.start();
				running = false;
			}
		}
	}
	
}
