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

public class LGBT extends Role {

	public LGBT() {
		super("LGBT", 1,
				true, false, false,
				Type.ALONE, 
				Sound.ENTITY_PLAYER_BURP,
				PotionEffectType.UNLUCK,
				"Vous gagnez seul. Vous ne pas pas �tre mis en couple. Chaque nuit, vous pouvez apporter un mal�fice en appellant SOS Homophobie, au lev� du jour, la personne ayant re�u le mal�fice aura 2 votes. Vous avez une autre sp�cificit�. Si quelqu'un vous a vot� pendant le jour et garde son vote jusqu'a la fin du jour, a la nuit suivante, vous aurez le droit de faire sauter son compte, il mourra.");
	
		this.item = new ItemStack(Material.PINK_WOOL, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("�eLGBT");
		this.item.setItemMeta(meta);
	}

	@Override
	public void onActivation(Player p) {
		canUse = true;
		
		String json = "[{" + LGPlugin.JSON_PREFIX + "\"text\":\"Vous vous sentez agress� ? Pas de probl�me, appelez \",\"color\":\"gray\"},{\"text\":\"[SOS HOMOPHOBIE]\",\"color\":\"green\",\"bold\":\"true\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/lg-lgbt\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Je me sens agress� :'(\"}}]";
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a(json));
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}

	@Override
	public void onStart(Player p) {		
		p.sendMessage(LGPlugin.PREFIX + "�7Vous �tes �c" + name);
		p.sendMessage("�7 - " + getDesc());
	}

}
