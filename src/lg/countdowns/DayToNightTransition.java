package lg.countdowns;

import lg.game.LGGame;

import org.bukkit.scheduler.BukkitRunnable;

public class DayToNightTransition extends BukkitRunnable {

	private int timer = 10;
	
	@Override
	public void run() {
		
		if(timer == 0)
		{
			LGGame.night();
			this.cancel();
		}
		timer--;
		
	}

}
