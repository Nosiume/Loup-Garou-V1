package lg;

import lg.commands.KillCmd;
import lg.commands.LgCmd;
import lg.commands.MeCmd;
import lg.commands.NpcCmd;
import lg.commands.RoleCmd;
import lg.commands.StartCmd;
import lg.commands.ThrowCmd;
import lg.commands.ToggleGay;
import lg.commands.chats.CoupleChat;
import lg.commands.config.AddSpawnCmd;
import lg.commands.config.SetGameSize;
import lg.commands.config.SetLobbyCmd;
import lg.commands.config.SetStartingCmd;
import lg.commands.roles.CupidonCmd;
import lg.commands.roles.DetectiveCmd;
import lg.commands.roles.FortuneTellerCmd;
import lg.commands.roles.IPDLCmd;
import lg.commands.roles.LGBCmd;
import lg.commands.roles.LGBT2Cmd;
import lg.commands.roles.LGBTCmd;
import lg.commands.roles.SalvaCmd;
import lg.commands.roles.TelepathCmd;
import lg.commands.roles.WildChildCmd;
import lg.commands.roles.WitchCmd;
import lg.game.State;
import lg.game.StateManager;
import lg.game.events.GameEvents;
import lg.game.events.RoleCmdEvents;
import lg.game.events.roles.CupidonEvents;
import lg.game.events.roles.DetectiveEvents;
import lg.game.events.roles.FTellerEvents;
import lg.game.events.roles.HunterEvents;
import lg.game.events.roles.IPDLEvents;
import lg.game.events.roles.LGBEvents;
import lg.game.events.roles.LGBTEvents;
import lg.game.events.roles.SalvaEvents;
import lg.game.events.roles.TelepathEvents;
import lg.game.events.roles.WerewolfEvents;
import lg.game.events.roles.WildChildEvents;
import lg.game.events.roles.WitchEvents;
import lg.roles.RoleManager;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LGPlugin extends JavaPlugin {

	public static LGPlugin instance;
	public static final String PREFIX = "§7[§c§lLoup-Garou§r§7] §r";
	public static final String JSON_PREFIX = "\"text\":\"[\",\"color\":\"gray\"},"
			+ "{\"text\":\"Loup-Garou\",\"color\":\"red\",\"bold\":\"true\"},"
			+ "{\"text\":\"] \",\"color\":\"gray\",\"bold\":\"true\"},{";
	
	private static StateManager stateManager;
	private static RoleManager roleManager;
	
	@Override
	public void onEnable()
	{
		saveDefaultConfig();
		
		instance = this;
		stateManager = new StateManager();
		roleManager = new RoleManager();
		
		stateManager.currentState = State.LOBBY;
		
		//Config Commands
		getCommand("lg-setlobby").setExecutor(new SetLobbyCmd());
		getCommand("lg-setgamesize").setExecutor(new SetGameSize());
		getCommand("lg-setstarting").setExecutor(new SetStartingCmd());
		getCommand("lg-addspawn").setExecutor(new AddSpawnCmd());
		getCommand("lg-kill").setExecutor(new KillCmd());
		
		//Commands
		getCommand("lg-start").setExecutor(new StartCmd());
		getCommand("lg-npc").setExecutor(new NpcCmd());
		getCommand("lg-togglegay").setExecutor(new ToggleGay());
		getCommand("lg-lg").setExecutor(new LgCmd());
		getCommand("lg-me").setExecutor(new MeCmd());
		getCommand("lg-roles").setExecutor(new RoleCmd());
		
		//Role Commands
		getCommand("lg-telepath").setExecutor(new TelepathCmd());
		getCommand("lg-cupidon").setExecutor(new CupidonCmd());
		getCommand("lg-wildchild").setExecutor(new WildChildCmd());
		getCommand("lg-fteller").setExecutor(new FortuneTellerCmd());
		getCommand("lg-detective").setExecutor(new DetectiveCmd());
		getCommand("lg-lgbt").setExecutor(new LGBTCmd());
		getCommand("lg-lgbt2").setExecutor(new LGBT2Cmd());
		getCommand("lg-witch").setExecutor(new WitchCmd());
		getCommand("lg-salva").setExecutor(new SalvaCmd());
		getCommand("lg-ipdl").setExecutor(new IPDLCmd());
		getCommand("lg-lgb").setExecutor(new LGBCmd());
		
		//Chats
		getCommand("lg-c").setExecutor(new CoupleChat());
		
		//Fun
		getCommand("throw").setExecutor(new ThrowCmd());
		
		//Game Events
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new GameEvents(), this);
		pm.registerEvents(new TelepathEvents(), this);
		pm.registerEvents(new CupidonEvents(), this);
		pm.registerEvents(new WildChildEvents(), this);
		pm.registerEvents(new FTellerEvents(), this);
		pm.registerEvents(new DetectiveEvents(), this);
		pm.registerEvents(new WerewolfEvents(), this);
		pm.registerEvents(new LGBTEvents(), this);
		pm.registerEvents(new WitchEvents(), this);
		pm.registerEvents(new HunterEvents(), this);
		pm.registerEvents(new SalvaEvents(), this);
		pm.registerEvents(new RoleCmdEvents(), this);
		pm.registerEvents(new IPDLEvents(), this);
		pm.registerEvents(new LGBEvents(), this);
		
		System.out.println("LG Plugin is running correctly.");
	}

	@Override
	public void onDisable()
	{
		System.out.println("LG Plugin has stopped.");
	}
	
	public static StateManager getStateManager()
	{
		return stateManager;
	}
	
	public static RoleManager getRoleManager()
	{
		return roleManager;
	}
	
}
