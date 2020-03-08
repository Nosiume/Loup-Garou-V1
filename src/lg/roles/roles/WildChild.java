package lg.roles.roles;

import lg.LGPlugin;
import lg.game.LGGame;
import lg.roles.Role;
import lg.roles.Roles;
import lg.roles.Type;
import net.minecraft.server.v1_14_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_14_R1.PacketPlayOutChat;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WildChild extends Role {

	public Player master;
	
	public WildChild() {
		super("Enfant Sauvage", 1,
				false, false, true,
				Type.VILLAGER, 
				Sound.ENTITY_CAT_AMBIENT,
				PotionEffectType.SATURATION,
				"Vous devez choisir un modèle. Si celui ci vient à mourir, vous deviendrez automatiquement loup-garou.");

		this.item = new ItemStack(Material.BONE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§e" + name);
		item.setItemMeta(meta);
	}

	@Override
	public void onActivation(Player p) {
		canUse = true;
		
		String json = "[{" + LGPlugin.JSON_PREFIX + "\"text\":\"Vous devez choisir un modèle. \",\"color\":\"gray\"},{\"text\":\"[CLIQUE ICI]\",\"color\":\"green\",\"bold\":\"true\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/lg-wildchild\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Clique pour choisir ton modèle.\"}}]";
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a(json));
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}

	@Override
	public void onStart(Player p) {
		p.sendMessage(LGPlugin.PREFIX + "§eVous êtes §c" + name + ".");
		p.sendMessage("§7 - " + getDesc());
	}

	public void onMasterDeath() {
		for(int i = 0 ; i < players.size() ; i++)
		{
			Player p = players.get(i);
			
			if(LGGame.deadPlayers.contains(p)) continue;
			
			this.players.remove(p);
			Werewolf werewolf = (Werewolf) LGPlugin.getRoleManager().getRole(Roles.WEREWOLF);
			werewolf.addPlayer(p);;
			
			werewolf.clearIndicators(); // RESET INDICATORS
			werewolf.spawnIndicators(); // Re-Spawn indicators
			
			p.removePotionEffect(this.getEffect());
			p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 0, true, true), true);
			
			werewolf.getPlayers().forEach(player -> {
				player.sendMessage(LGPlugin.PREFIX + "§c" + p.getName() + " §eest devenu §cLoup-Garou.");
			});
		}
	}

}
