package lg.game;

import java.util.HashMap;

import lg.LGPlugin;
import lg.game.states.DayState;
import lg.game.states.LobbyState;
import lg.game.states.NightState;

import org.bukkit.event.Listener;

public class StateManager {

	private static HashMap<State, Listener> states = new HashMap<State, Listener>();
	
	public State currentState;
	
	public StateManager()
	{
		states.put(State.LOBBY, new LobbyState());
		states.put(State.DAY, new DayState());
		states.put(State.NIGHT, new NightState());
		
		for(Listener listener : states.values())
		{
			LGPlugin.instance.getServer().
				getPluginManager().
				registerEvents(listener, LGPlugin.instance);
		}
	}
	
	public boolean isStateActivated(State state)
	{
		return currentState == state;
	}
	
}
