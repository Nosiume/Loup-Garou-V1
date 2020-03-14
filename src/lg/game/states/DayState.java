package lg.game.states;

import lg.LGPlugin;
import lg.game.LGGame;
import lg.game.State;
import lg.game.Vote;
import lg.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class DayState implements Listener{

	@EventHandler
	public void onCancel(PlayerInteractEvent event)
	{
		if(LGPlugin.getStateManager().isStateActivated(State.DAY))
		{
			Player p = event.getPlayer();
			Action action = event.getAction();
			ItemStack it = event.getItem();
			
			if(it != null)
			{
				if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
				{
					if(it.isSimilar(LGGame.cancelItem))
					{
						Vote v = LGGame.whoVotedPlayer(p);
						v.cancelVote(p);
						LGGame.alreadyVoted.remove(p);
						LGGame.updateDisplays(v);
						
						p.getInventory().clear();
						
						Bukkit.broadcastMessage(LGPlugin.PREFIX + "§c" + p.getName() + " §ea annulé son vote pour §c" + v.getVoted().getName());
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event)
	{
		if(LGPlugin.getStateManager().isStateActivated(State.DAY)
				&& LGGame.villageVote)
		{
			Player p = event.getPlayer();
			Action action = event.getAction();
			if(!LGGame.deadPlayers.contains(p) &&
					(action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK))
			{
				Entity rawTarget = Utils.getNearestEntityInSight(p, 100);
				if(rawTarget == null) return;
				
				if(rawTarget.getType() == EntityType.PLAYER)
				{
					Player target = (Player)rawTarget;
					
					if(LGGame.alreadyVoted.contains(p))
					{
						if(target.getName().equalsIgnoreCase(LGGame.whoVotedPlayer(p).getVoted().getName()))
							return;
						
						Vote v = LGGame.whoVotedPlayer(p);
						v.cancelVote(p);
						LGGame.alreadyVoted.remove(p);
						LGGame.updateDisplays(v);
							
						Bukkit.broadcastMessage(LGPlugin.PREFIX + "§c" + p.getName() + " §ea annulé son vote pour §c" + v.getVoted().getName());
					}
					
					if(!LGGame.playerHasBeenVoted(target))
					{
						LGGame.votes.add(new Vote(target));
					}
					
					LGGame.getPlayerVoteStats(target).vote(p);
					LGGame.updateDisplays(LGGame.getPlayerVoteStats(target));
					
					p.getInventory().setItem(8, LGGame.cancelItem);
					
					LGGame.alreadyVoted.add(p);
					
					Bukkit.broadcastMessage(LGPlugin.PREFIX + "§c" + p.getName() + "§e a voté pour §c" + target.getName());
				}
			}
		}
	}
	
}
