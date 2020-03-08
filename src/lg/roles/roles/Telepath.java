package lg.roles.roles;

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

public class Telepath extends Role {

	public Role toTalk;
	public boolean selected = false;
	
	public Telepath() {
		super("Télépathe", 1,
				true, false, false,
				Type.VILLAGER,
				Sound.ENTITY_GUARDIAN_ATTACK,
				PotionEffectType.LUCK,
				"Chaque nuit, vous pouvez envoyer un message anonyme au rôle que vous voulez. ");
	
		this.item = new ItemStack(Material.ENDER_PEARL, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§e" + name);
		this.item.setItemMeta(meta);
	}

	@Override
	public void onActivation(Player p) {
		canUse = true;
		
		String json = "[{" + LGPlugin.JSON_PREFIX + "\"text\":\"Veuillez sélectionner un rôle à qui parler.\",\"color\":\"yellow\"},{\"text\":\"   [CLIQUE ICI]\",\"color\":\"green\",\"bold\":\"true\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/lg-telepath\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Selection d'un rôle.\"}}]";
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a(json));
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}

	@Override
	public void onStart(Player p) {
		p.sendMessage(LGPlugin.PREFIX + "§eVous êtes §c" + name + ".");
		p.sendMessage("§7 - " + getDesc());
	}

}
