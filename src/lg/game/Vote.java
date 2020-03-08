package lg.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class Vote {

	private List<Player> voters = new ArrayList<Player>();
	private Player voted;
	private int votes = 0;
	
	public Vote(Player voted)
	{
		this.voted = voted;
	}
	
	public void vote(Player voter)
	{
		voters.add(voter);
		votes++;
	}
	
	public void cancelVote(Player canceler)
	{
		voters.remove(canceler);
		votes--;
	}

	public Player getVoted() {
		return voted;
	}
	
	public int getVotes()
	{
		return votes;
	}
	
	public List<Player> getVoters()
	{
		return voters;
	}
	
}
