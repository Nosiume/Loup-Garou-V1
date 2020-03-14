package lg.roles.roles;

import java.util.HashMap;

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

public class Salvator extends Role {

	public HashMap<Player, Integer> alreadyProtected = new HashMap<Player, Integer>();
	public Player protectedPlayer = null;
	
	public Salvator() {
		super("Salvateur", 1,
				true, false, false,
				Type.VILLAGER,
				Sound.ITEM_SHIELD_BLOCK,
				PotionEffectType.HERO_OF_THE_VILLAGE,
				"Toutes les nuits, vous pouvez protéger quelqu'un de l'attaque des loups. Vous ne pouvez pas protéger deux fois la même personne.",
				3);
	
		this.item = new ItemStack(Material.BLAZE_ROD, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eSalvateur");
		item.setItemMeta(meta);
	}

	@Override
	public void onActivation(Player p) {
		canUse = true;
		
		String json = "[{" + LGPlugin.JSON_PREFIX + "\"text\":\"L'heure de protéger quelqu'un est venue. \",\"color\":\"yellow\"},{\"text\":\"[PROTEGER QUELQU'UN]\",\"color\":\"green\",\"bold\":\"true\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/lg-salva\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Clique pour protéger quelqu'un.\"}}]";
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a(json));
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}

	@Override
	public void onStart(Player p) {
		p.sendMessage(LGPlugin.PREFIX + "§eVous êtes §c" + name);
		p.sendMessage("§7 - " + getDesc());
	}

}
