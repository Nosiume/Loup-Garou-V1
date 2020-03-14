package lg.roles.roles;

import java.util.ArrayList;
import java.util.List;

import lg.LGPlugin;
import lg.roles.Role;
import lg.roles.Type;
import net.minecraft.server.v1_14_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_14_R1.PacketPlayOutChat;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

public class LGBT extends Role {

	public Player target = null;
	
	public boolean canKill = false;
	public List<Player> lastVoters = new ArrayList<Player>();
	
	public LGBT() {
		super("LGBT", 1,
				true, false, false,
				Type.ALONE, 
				Sound.ENTITY_PLAYER_BURP,
				PotionEffectType.UNLUCK,
				"Vous gagnez seul. Vous ne pouvez pas être mis en couple. Chaque nuit, vous pouvez apporter un maléfice en appellant SOS Homophobie, au levé du jour, la personne ayant reçu le maléfice aura 2 votes. Vous avez une autre spécificité. Si quelqu'un vous a voté pendant le jour et garde son vote jusqu'a la fin du jour, a la nuit suivante, vous aurez le droit de faire sauter son compte, il mourra.",
				6);
	
		this.item = new ItemStack(Material.PINK_WOOL, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eLGBT");
		this.item.setItemMeta(meta);
	}

	@Override
	public void onActivation(Player p) {
		canUse = true;
		target = null;
		
		String json = "[{" + LGPlugin.JSON_PREFIX + "\"text\":\"Vous vous sentez harcelé ? Pas de problème, appelez \",\"color\":\"gray\"},{\"text\":\"[SOS HOMOPHOBIE]\",\"color\":\"green\",\"bold\":\"true\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/lg-lgbt\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Je me sens agressé :'(\"}}]";
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a(json));
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}
	
	public void onKillActivation(Player p)
	{
		String json = "[{" + LGPlugin.JSON_PREFIX + "\"text\":\"Vous pouvez choisir d'éliminer un de vos harceleur. \",\"color\":\"yellow\"},{\"text\":\"[PAYER UN TUEUR A GAGE]\",\"color\":\"green\",\"bold\":\"true\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/lg-lgbt2\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Il va crever ce fils de pute.\"}}]";
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a(json));
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}

	@Override
	public void onStart(Player p) {		
		p.sendMessage(LGPlugin.PREFIX + "§7Vous êtes §c" + name);
		p.sendMessage("§7 - " + getDesc());
	}

}
