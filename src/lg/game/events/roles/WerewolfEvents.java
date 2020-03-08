package lg.game.events.roles;

import lg.LGPlugin;
import lg.game.Vote;
import lg.roles.Roles;
import lg.roles.roles.Werewolf;
import lg.utils.Utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class WerewolfEvents implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onCancel(PlayerInteractEvent event)
	{
		Player p = event.getPlayer();
		Action action = event.getAction();
		ItemStack it = p.getItemInHand();
		
		if(it != null)
		{
			if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
			{
				if(it.isSimilar(Werewolf.cancelItem))
				{
					Werewolf werewolf = (Werewolf) LGPlugin.getRoleManager().getRole(Roles.WEREWOLF);
					Vote v = werewolf.whoVotedPlayer(p);
					v.cancelVote(p);
					werewolf.alreadyVoted.remove(p);
					werewolf.updateDisplay(v);
					
					werewolf.getPlayers().forEach(player -> {
						player.sendMessage(LGPlugin.PREFIX + "§c" + p.getName() + " §ea annulé son vote contre §c" + v.getVoted().getName());
					});
					
					p.getInventory().clear();
				}
			}
		}
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event)
	{
		Player pSelect = (Player)event.getPlayer();
		Action action = event.getAction();
		Werewolf werewolf = (Werewolf) LGPlugin.getRoleManager().getRole(Roles.WEREWOLF);
		if(werewolf.canUse && werewolf.isPlayerRole(pSelect))
		{
			if(action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)
			{
				//Player left clicked
				Entity rawTarget = Utils.getNearestEntityInSight(pSelect, 100);
				if(rawTarget == null) return;
				
				if(rawTarget.getType() == EntityType.PLAYER)
				{
					Player pTarget = (Player)rawTarget;
					
					if(werewolf.alreadyVoted.contains(pSelect))
					{
						if(pTarget.getName().equalsIgnoreCase(werewolf.whoVotedPlayer(pSelect).getVoted().getName()))
						{
							Vote v = werewolf.whoVotedPlayer(pSelect);
							v.cancelVote(pSelect);
							werewolf.alreadyVoted.remove(pSelect);
							werewolf.updateDisplay(v);
							
							//Remove item canceler from inv
							pSelect.getInventory().clear();
							
							werewolf.getPlayers().forEach(player -> {
								player.sendMessage(LGPlugin.PREFIX + "§c" + pSelect.getName() + " §ea annulé son vote contre §c" + v.getVoted().getName());
							});
							
							return;
						}
						
						pSelect.sendMessage("§cVous avez déjà voté. Si vous voulez changer votre vote, vous devez d'abord annuler celui en cours.");
						return;
					}
					
					//If not then compute new vote
					if(!werewolf.playerHasBeenVoted(pTarget))
					{
						werewolf.votes.add(new Vote(pTarget));
					}
					
					werewolf.getPlayerVoteStats(pTarget).vote(pSelect);
					werewolf.updateDisplay(werewolf.getPlayerVoteStats(pTarget));
					
					//Add item to remove vote to selector
					pSelect.getInventory().setItem(8, Werewolf.cancelItem);
					
					//Add player to vote list
					werewolf.alreadyVoted.add(pSelect);
					
					//Print a message to every werewolf
					for(Player p : werewolf.getPlayers())
					{
						p.sendMessage(LGPlugin.PREFIX + "§c" + pSelect.getName() + " §ea voté pour §c" + pTarget.getName());
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event)
	{
		ItemStack it = event.getItemDrop().getItemStack();
		if(it.isSimilar(Werewolf.cancelItem))
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
}
