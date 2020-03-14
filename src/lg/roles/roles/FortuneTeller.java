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

public class FortuneTeller extends Role {
	
	public FortuneTeller() {
		super("Voyante", 1,
				true, false, false,
				Type.VILLAGER,
				Sound.BLOCK_BEACON_ACTIVATE,
				PotionEffectType.NIGHT_VISION,
				"Chaque nuit, vous pouvez voir le rôle de quelqu'un, le rôle de la personne qui a été regardée pendant la nuit sera révélée a tous les villageois, sans le pseudo de la personne.",
				1);
	
		this.item = new ItemStack(Material.ENDER_EYE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§e" + name);
		item.setItemMeta(meta);
	}

	@Override
	public void onActivation(Player p) {
		canUse = true;
		
		String json = "[{" + LGPlugin.JSON_PREFIX + "\"text\":\"Veuillez choisir une personne à espionner. \",\"color\":\"yellow\"},{\"text\":\"[CLIQUE ICI]\",\"color\":\"green\",\"bold\":\"true\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/lg-fteller\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Choisir une personne à espionner.\"}}]";
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a(json));
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}

	@Override
	public void onStart(Player p) {
		p.sendMessage(LGPlugin.PREFIX + "§eVous êtes §c" + name + "§e.");
		p.sendMessage("§7 - " + getDesc());
	}

}
