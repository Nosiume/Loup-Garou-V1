package lg.countdowns;

import lg.game.LGGame;

import org.bukkit.scheduler.BukkitRunnable;

public class NightToDayTransition extends BukkitRunnable {

	private int timer = 15;
	private boolean running = true;
	
	@Override
	public void run() {
		if(running)
		{
			if(timer == 0)
			{
				LGGame.night();
				running = false;
				return;
			}
			
			timer--;
		}
	}

}
