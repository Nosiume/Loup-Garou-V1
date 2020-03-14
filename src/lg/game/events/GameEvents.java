package lg.game.events;

import lg.LGPlugin;
import lg.game.LGGame;
import lg.game.State;
import lg.roles.Roles;
import lg.roles.roles.Werewolf;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class GameEvents implements Listener {
	
	//======= DEAD =========
	
	@EventHandler
	public void onClick(InventoryClickEvent event)
	{	
		if(LGPlugin.getStateManager().isStateActivated(State.DAY) ||
				LGPlugin.getStateManager().isStateActivated(State.NIGHT))
		{
			Player p = (Player)event.getWhoClicked();
			if(LGGame.deadPlayers.contains(p))
			{
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event)
	{
		if(LGPlugin.getStateManager().isStateActivated(State.DAY) ||
				LGPlugin.getStateManager().isStateActivated(State.NIGHT))
		{
			event.setCancelled(true);
		}
	}
	
	//======= GAME =========
	
	@EventHandler
	public void onMove(PlayerMoveEvent event)
	{
		if(LGPlugin.getStateManager().isStateActivated(State.DAY) ||
				LGPlugin.getStateManager().isStateActivated(State.NIGHT))
		{
			Location from = event.getFrom();
			Location to = event.getTo();
			
			if(from.getX() != to.getX() || 
					from.getY() != to.getY() ||
					from.getZ() != to.getZ())
			{
				event.setTo(from);
			}
		}
	}
	
	
	@EventHandler
	public void onDropCanceler(PlayerDropItemEvent event)
	{
		ItemStack it = event.getItemDrop().getItemStack();
		if(it.isSimilar(LGGame.cancelItem))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlock(BlockPlaceEvent event)
	{
		Werewolf werewolf = (Werewolf) LGPlugin.getRoleManager().getRole(Roles.WEREWOLF);
		Player p = event.getPlayer();
		if(werewolf.isPlayerRole(p) && werewolf.canUse)
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event)
	{
		Player p = event.getPlayer();
		if(!p.isOp())
		{
			p.sendMessage("§cTu n'as pas le droit de casser des blocs dans cette zone.");
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event)
	{
		if(event.getEntityType() == EntityType.PLAYER)
		{
			event.setCancelled(true);
		}
	}
	
}
